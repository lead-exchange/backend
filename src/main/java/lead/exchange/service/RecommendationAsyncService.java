package lead.exchange.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationAsyncService {

    private final RecommendationService recommendationService;

    @Async("recPool")
    @Transactional
    public void recalcForEstate(UUID estateId) {
        recommendationService.recalculateRecommendationsForEstate(estateId);
        log.info("Async recalc finished for estate {}", estateId);
    }

    @Async("recPool")
    @Transactional
    public void recalcForLead(UUID leadId) {
        recommendationService.recalculateRecommendationsForLead(leadId);
        log.info("Async recalc finished for lead {}", leadId);

    }

    @Async("recPool")
    @Transactional
    public void initiateRecommendationsForLead(UUID leadId) {
        recommendationService.initiateRecommendationsForLead(leadId);
        log.info("Async initiation finished for lead {}", leadId);
    }

    @Async("recPool")
    @Transactional
    public void deleteRecommendationsByLead(UUID leadId) {
        recommendationService.deleteRecommendationsForLead(leadId);
        log.info("Async deletion finished for lead {}", leadId);
    }
}
