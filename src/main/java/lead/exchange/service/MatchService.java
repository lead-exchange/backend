package lead.exchange.service;

import lead.exchange.entity.Match;
import lead.exchange.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    @Transactional
    public Match createMatch(Match match) {
        log.info("Creating new match for lead: {} and estate: {}", match.getLeadId(), match.getEstateId());

        // Set timestamps if not provided
        if (match.getCreatedAt() == null) {
            match.setCreatedAt(LocalDateTime.now());
        }
        if (match.getUpdatedAt() == null) {
            match.setUpdatedAt(LocalDateTime.now());
        }
        if (match.getMatchedAt() == null) {
            match.setMatchedAt(LocalDateTime.now());
        }

        return matchRepository.save(match);
    }

    public List<Match> getMatchesByLeadId(UUID leadId) {
        log.debug("Fetching matches by lead id: {}", leadId);
        return matchRepository.findByLeadId(leadId);
    }

    public List<Match> getMatchesByEstateId(UUID estateId) {
        log.debug("Fetching matches by estate id: {}", estateId);
        return matchRepository.findByEstateId(estateId);
    }

}