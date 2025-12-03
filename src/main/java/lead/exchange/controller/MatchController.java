package lead.exchange.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lead.exchange.dto.CreateMatchDto;
import lead.exchange.dto.ResponseMatchWithEstateDto;
import lead.exchange.dto.ResponseMatchWithLeadDto;
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
    public ResponseEntity<Match> createMatch(@RequestBody @Valid CreateMatchDto matchDto) {
        Match createdMatch = matchService.createMatch(matchDto);
        return ResponseEntity.ok(createdMatch);
    }

    @PutMapping
    public ResponseEntity<Match> updateMatch(@RequestBody UpdateMatchDto matchDto) {
        Match createdMatch = matchService.updateMatch(matchDto);
        return ResponseEntity.ok(createdMatch);
    }

    @GetMapping("/lead/{leadId}")
    public ResponseEntity<List<ResponseMatchWithEstateDto>> getMatchesByLeadId(@PathVariable UUID leadId) {
        return ResponseEntity.ok(matchService.getMatchesByLeadId(leadId));
    }

    @GetMapping("/estate/{estateId}")
    public ResponseEntity<List<ResponseMatchWithLeadDto>> getMatchesByEstateId(@PathVariable UUID estateId) {
        return ResponseEntity.ok(matchService.getMatchesByEstateId(estateId));
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<Match> getMatchById(@PathVariable UUID matchId) {
        Match match = matchService.getMatchById(matchId);
        return ResponseEntity.ok(match);
    }

}
