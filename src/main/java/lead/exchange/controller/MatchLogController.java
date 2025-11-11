package lead.exchange.controller;

import lead.exchange.entity.MatchLog;
import lead.exchange.service.MatchLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/match-logs")
@RequiredArgsConstructor
public class MatchLogController {

    private final MatchLogService matchLogService;

    @GetMapping("/{matchId}")
    public ResponseEntity<List<MatchLog>> getMatchLogsByMatchId(@PathVariable UUID matchId) {
        List<MatchLog> logs = matchLogService.getMatchLogsByMatchId(matchId);
        return ResponseEntity.ok(logs);
    }
}