package lead.exchange.service;

import lead.exchange.entity.MatchLog;
import lead.exchange.repository.MatchLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchLogService {

    private final MatchLogRepository matchLogRepository;

    public MatchLog createMatchLog(MatchLog matchLog) {
        log.info("Creating match log for match: {}", matchLog.getMatchId());

        // Set timestamp if not provided
        if (matchLog.getCreatedAt() == null) {
            matchLog.setCreatedAt(LocalDateTime.now());
        }

        return matchLogRepository.save(matchLog);
    }

    public List<MatchLog> getMatchLogsByMatchId(UUID matchId) {
        log.debug("Fetching match logs by match id: {}", matchId);
        return matchLogRepository.findByMatchId(matchId);
    }

}