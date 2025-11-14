package lead.exchange.service;

import lead.exchange.dto.MatchDto;
import lead.exchange.dto.UserType;
import lead.exchange.entity.Match;
import lead.exchange.entity.MatchLog;
import lead.exchange.mapper.MatchMapper;
import lead.exchange.model.MatchStatus;
import lead.exchange.repository.EstateRepository;
import lead.exchange.repository.LeadRepository;
import lead.exchange.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    private final MatchLogService matchLogService;

    public List<Match> getMatchesByLeadId(UUID leadId) {
        log.debug("Fetching matches by lead id: {}", leadId);
        return matchRepository.findByLeadId(leadId);
    }

    public List<Match> getMatchesByEstateId(UUID estateId) {
        log.debug("Fetching matches by estate id: {}", estateId);
        return matchRepository.findByEstateId(estateId);
    }

    @Transactional
    public Match createMatch(MatchDto dto) {
        log.info("Creating new match for lead: {} and estate: {}", dto.leadId(), dto.estateId());

        Match match = MatchMapper.toEntity(dto);

        LocalDateTime now = LocalDateTime.now();
        ensureTimestamps(match, now);

        MatchStatus logStatus = resolveLogStatus(match, dto.userType());

        Match saved = matchRepository.save(match);
        matchLogService.createMatchLog(buildLog(saved, logStatus, now));

        return saved;
    }

    private static void ensureTimestamps(Match match, LocalDateTime now) {
        if (match.getCreatedAt() == null) match.setCreatedAt(now);
        if (match.getUpdatedAt() == null) match.setUpdatedAt(now);
        if (match.getMatchedAt() == null) match.setMatchedAt(now);
    }

    private static MatchLog buildLog(Match match, MatchStatus status, LocalDateTime now) {
        return MatchLog.builder()
                .matchId(match.getId())
                .status(status)
                .leadCommission(match.getLeadCommission())
                .updatedBy(match.getUpdatedBy())
                .comment(match.getComment())
                .createdAt(now)
                .build();
    }

    private MatchStatus resolveLogStatus(Match newMatch, UserType userType) {
        if (newMatch.getId() == null) {
            return resolveForNew(newMatch, userType);
        }

        Match oldMatch = matchRepository.findById(newMatch.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No match with id=" + newMatch.getId()));

        return resolveForUpdate(newMatch, oldMatch, userType);
    }

    private static MatchStatus resolveForUpdate(Match n, Match o, UserType userType) {
        boolean leadChanged = n.getLeadStatus() != o.getLeadStatus();
        boolean estateChanged = n.getEstateStatus() != o.getEstateStatus();

        if (!exactlyOneTrue(leadChanged, estateChanged)) {
            throw new IllegalArgumentException(
                    "Exactly one of lead/estate statuses must change for update");
        }

        if (leadChanged && userType == UserType.LEAD) return n.getLeadStatus();
        if (estateChanged && userType == UserType.ESTATE) return n.getEstateStatus();

        throw new IllegalStateException("Illegal user type: " + userType + " for changing status");
    }

    private static MatchStatus resolveForNew(Match m, UserType userType) {
        boolean estateDefined = m.getEstateStatus() != MatchStatus.UNDEFINED;
        boolean leadDefined = m.getLeadStatus() != MatchStatus.UNDEFINED;

        if (!exactlyOneTrue(estateDefined, leadDefined)) {
            throw new IllegalArgumentException(
                    "Exactly one of lead/estate statuses must be defined for a new match");
        }

        MatchStatus status = statusByUser(m, userType);
        if (status == MatchStatus.UNDEFINED) {
            throw new IllegalArgumentException(
                    "Illegal user type: " + userType + " for setting status in new match");
        }

        return status;
    }

    private static MatchStatus statusByUser(Match m, UserType t) {
        return t == UserType.LEAD ? m.getLeadStatus() : m.getEstateStatus();
    }

    private static boolean exactlyOneTrue(boolean a, boolean b) {
        return a ^ b;
    }
}