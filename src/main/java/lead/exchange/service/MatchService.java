package lead.exchange.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lead.exchange.dto.MatchDto;
import lead.exchange.dto.UserType;
import lead.exchange.entity.Match;
import lead.exchange.entity.MatchLog;
import lead.exchange.mapper.MatchMapper;
import lead.exchange.model.MatchStatus;
import lead.exchange.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchLogService matchLogService;
    private final Clock clock;

    public List<Match> getMatchesByLeadId(UUID leadId) {
        log.debug("Fetching matches by lead id: {}", leadId);
        return matchRepository.findByLeadId(leadId);
    }

    public Match getMatchById(UUID matchId) {
        log.debug("Fetching match by id: {}", matchId);
        return matchRepository.findById(matchId)
            .orElseThrow(() -> new IllegalArgumentException("Match with this id not found"));
    }

    public List<Match> getMatchesByEstateId(UUID estateId) {
        log.debug("Fetching matches by estate id: {}", estateId);
        return matchRepository.findByEstateId(estateId);
    }

    @Transactional
    public Match createMatch(MatchDto dto) {
        log.info("Creating new match for lead: {} and estate: {}", dto.leadId(), dto.estateId());

        Match match = MatchMapper.toEntity(dto);
        LocalDateTime now = LocalDateTime.now(clock);

        initializeTimestamps(match, now);
        MatchStatus logStatus = determineLogStatus(match, dto.userType());

        Match savedMatch = matchRepository.save(match);
        matchLogService.createMatchLog(buildMatchLog(savedMatch, logStatus, now, dto.userType()));

        return savedMatch;
    }

    private MatchStatus determineLogStatus(Match newMatch, UserType userType) {
        if (isNewMatch(newMatch)) {
            return determineStatusForNewMatch(newMatch, userType);
        } else {
            return determineStatusForExistingMatch(newMatch, userType);
        }
    }

    private boolean isNewMatch(Match match) {
        return match.getId() == null;
    }

    private MatchStatus determineStatusForExistingMatch(Match newMatch, UserType userType) {
        Match existingMatch = matchRepository.findById(newMatch.getId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Match not found with id: " + newMatch.getId()));

        return resolveStatusForMatchUpdate(newMatch, existingMatch, userType);
    }

    private MatchStatus resolveStatusForMatchUpdate(Match newMatch, Match existingMatch, UserType userType) {
        boolean leadStatusChanged = newMatch.getLeadStatus() != existingMatch.getLeadStatus();
        boolean estateStatusChanged = newMatch.getEstateStatus() != existingMatch.getEstateStatus();

        validateSingleStatusChange(leadStatusChanged, estateStatusChanged);

        if (leadStatusChanged && userType == UserType.LEAD) {
            validateStatusTransition(existingMatch.getLeadStatus(), newMatch.getLeadStatus(), UserType.LEAD);
            return newMatch.getLeadStatus();
        }

        if (estateStatusChanged && userType == UserType.ESTATE) {
            validateStatusTransition(existingMatch.getEstateStatus(), newMatch.getEstateStatus(), UserType.ESTATE);
            return newMatch.getEstateStatus();
        }

        throw new IllegalStateException(
            "Invalid user type " + userType + " for the changed status");
    }

    private void validateSingleStatusChange(boolean leadChanged, boolean estateChanged) {
        if (!exactlyOneChange(leadChanged, estateChanged)) {
            throw new IllegalArgumentException(
                "Exactly one of lead or estate status must change during update");
        }
    }

    private MatchStatus determineStatusForNewMatch(Match match, UserType userType) {
        boolean estateStatusDefined = match.getEstateStatus() != MatchStatus.UNDEFINED;
        boolean leadStatusDefined = match.getLeadStatus() != MatchStatus.UNDEFINED;

        validateSingleDefinedStatus(estateStatusDefined, leadStatusDefined);

        MatchStatus userStatus = getUserSpecificStatus(match, userType);
        if (userStatus == MatchStatus.UNDEFINED) {
            throw new IllegalArgumentException(
                "User type " + userType + " cannot set UNDEFINED status in new match");
        }

        return userStatus;
    }

    private void validateSingleDefinedStatus(boolean estateDefined, boolean leadDefined) {
        if (!exactlyOneChange(estateDefined, leadDefined)) {
            throw new IllegalArgumentException(
                "Exactly one of lead or estate status must be defined for new match");
        }
    }

    private MatchStatus getUserSpecificStatus(Match match, UserType userType) {
        return userType == UserType.LEAD ? match.getLeadStatus() : match.getEstateStatus();
    }

    private void validateStatusTransition(MatchStatus currentStatus, MatchStatus newStatus, UserType userType) {
        boolean isValidTransition = switch (currentStatus) {
            case UNDEFINED -> isAllowedUndefinedTransition(newStatus);
            case COMMISSION -> isAllowedCommissionTransition(newStatus);
            case LIKED -> isAllowedLikedTransition(newStatus);
            default -> false;
        };

        if (!isValidTransition) {
            throw new IllegalArgumentException(
                "Invalid status transition from " + currentStatus + " to " + newStatus + " for " + userType);
        }
    }

    private boolean isAllowedUndefinedTransition(MatchStatus newStatus) {
        return newStatus == MatchStatus.LIKED
            || newStatus == MatchStatus.DISLIKE
            || newStatus == MatchStatus.COMMISSION;
    }

    private boolean isAllowedCommissionTransition(MatchStatus newStatus) {
        return newStatus == MatchStatus.COMMISSION
            || newStatus == MatchStatus.ACCEPTED
            || newStatus == MatchStatus.DECLINED;
    }

    private boolean isAllowedLikedTransition(MatchStatus newStatus) {
        return newStatus == MatchStatus.LIKED
            || newStatus == MatchStatus.DISLIKE
            || newStatus == MatchStatus.COMMISSION;
    }

    private boolean exactlyOneChange(boolean change1, boolean change2) {
        return change1 ^ change2;
    }

    private void initializeTimestamps(Match match, LocalDateTime now) {
        if (match.getCreatedAt() == null) {
            match.setCreatedAt(now);
        }
        if (match.getUpdatedAt() == null) {
            match.setUpdatedAt(now);
        }
        if (match.getMatchedAt() == null) {
            match.setMatchedAt(now);
        }
    }

    private MatchLog buildMatchLog(Match match, MatchStatus status, LocalDateTime timestamp, UserType userType) {
        return MatchLog.builder()
            .matchId(match.getId())
            .status(status)
            .leadCommission(match.getLeadCommission())
            .updatedBy(match.getUpdatedBy())
            .comment(match.getComment())
            .createdAt(timestamp)
            .userType(userType.name())
            .build();
    }
}
