package lead.exchange.controller;

import java.util.List;
import java.util.UUID;
import lead.exchange.dto.CreateMatchDto;
import lead.exchange.dto.UpdateMatchDto;
import lead.exchange.entity.Match;
import lead.exchange.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody CreateMatchDto matchDto) {
        Match createdMatch = matchService.createMatch(matchDto);
        return ResponseEntity.ok(createdMatch);
    }

    @PutMapping
    public ResponseEntity<Match> updateMatch(@RequestBody UpdateMatchDto matchDto) {
        Match createdMatch = matchService.updateMatch(matchDto);
        return ResponseEntity.ok(createdMatch);
    }

    @GetMapping("/lead/{leadId}")
    public ResponseEntity<List<Match>> getMatchesByLeadId(@PathVariable UUID leadId) {
        List<Match> matches = matchService.getMatchesByLeadId(leadId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/estate/{estateId}")
    public ResponseEntity<List<Match>> getMatchesByEstateId(@PathVariable UUID estateId) {
        List<Match> matches = matchService.getMatchesByEstateId(estateId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<Match> getMatchById(@PathVariable UUID matchId) {
        Match match = matchService.getMatchById(matchId);
        return ResponseEntity.ok(match);
    }

}
