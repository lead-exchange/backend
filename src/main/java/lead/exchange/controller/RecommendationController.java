package lead.exchange.controller;

import lead.exchange.entity.Estate;
import lead.exchange.entity.Lead;
import lead.exchange.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/recommendations/forLead/{leadId}")
    public ResponseEntity<List<Estate>> getRecommendationsForLead(@PathVariable UUID leadId) {
        return ResponseEntity.ok(recommendationService.getRecosByLeadId(leadId));
    }

    @GetMapping("/recommendations/forEstate/{estateId}")
    public ResponseEntity<List<Lead>> getRecommendationsForEstate(@PathVariable UUID estateId) {
        return ResponseEntity.ok(recommendationService.getRecosByEstateId(estateId));
    }

    @GetMapping("/recommendations/initiateRecomms")
    public boolean getRecommendationsInitiateRecomms() {
        try {
            recommendationService.initiateRecommendations();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
