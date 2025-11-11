package lead.exchange.controller;

import lead.exchange.dto.MatchDto;
import lead.exchange.entity.Match;
import lead.exchange.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PutMapping
    public ResponseEntity<Match> createMatch(@RequestBody MatchDto matchDto) {
        Match createdMatch = matchService.createMatch(matchDto);
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


}