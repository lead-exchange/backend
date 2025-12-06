package lead.exchange.controller;

import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Estate;
import lead.exchange.entity.Lead;
import lead.exchange.request.PagingBody;
import lead.exchange.response.EstateRecommendationResponse;
import lead.exchange.response.LeadRecommendationResponse;
import lead.exchange.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/recommendations/forLead/{leadId}")
    public ResponseEntity<LeadRecommendationResponse> getRecommendationsForLead(@PathVariable UUID leadId, @RequestBody PagingBody pagingBody) {
        return ResponseEntity.ok(recommendationService.getRecosByLeadId(leadId, pagingBody));
    }

    @GetMapping("/recommendations/forEstate/{estateId}")
    public ResponseEntity<EstateRecommendationResponse> getRecommendationsForEstate(@PathVariable UUID estateId, @RequestBody PagingBody pagingBody) {
        return ResponseEntity.ok(recommendationService.getRecosByEstateId(estateId, pagingBody));
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
