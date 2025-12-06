package lead.exchange.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lead.exchange.entity.Estate;
import lead.exchange.entity.Lead;
import lead.exchange.entity.Recommendation;
import lead.exchange.exception.ResourceNotFoundException;
import lead.exchange.model.EstateAttributes;
import lead.exchange.model.Requirements;
import lead.exchange.model.ScoreCalculationResult;
import lead.exchange.repository.EstateRepository;
import lead.exchange.repository.LeadRepository;
import lead.exchange.repository.RecommendationsRepository;
import lead.exchange.request.PagingBody;
import lead.exchange.response.EstateRecommendationResponse;
import lead.exchange.response.LeadRecommendationResponse;
import lead.exchange.response.RecommendationResponseWithPaging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private static final double BEDROOM_SCORE_CONST = 3.0;
    private static final double PERCENT = 100.0;
    private final static double W_TYPE = 0.1;
    private final static double W_PRICE = 0.35;
    private final static double W_AREA = 0.25;
    private final static double W_LOCATION = 0.2;
    private final static double W_BEDROOMS = 0.1;
    private final static double W_COMMISSION = 0.15;
    private final static double W_VECTOR_SIMILARITY = 0.15;

    private final static int HARD_LIMIT = 10;
    public static final String LEAD = "lead";

    private final RecommendationsRepository recommendationRepository;
    private final LeadRepository leadRepository;
    private final EstateRepository estateRepository;
    private final EmbeddingService embeddingService;


    public LeadRecommendationResponse getRecosByLeadId(UUID leadId, PagingBody pagingBody) {
        List<Recommendation> recommendations;
        if (pagingBody.lastScore == null || pagingBody.lastId == null) {
            recommendations = recommendationRepository.getListForLead(leadId, pagingBody.count);
        } else {
            recommendations = recommendationRepository.getListForLeadWithPaging(leadId, pagingBody.count, pagingBody.lastScore, pagingBody.lastId);
        }
        LeadRecommendationResponse response = new LeadRecommendationResponse();
        fillPagingResponse(recommendations, response);

        if (CollectionUtils.isEmpty(recommendations)) {
            response.items = Collections.emptyList();
            return response;
        }
        List<UUID> estateIds = recommendations.stream().map(Recommendation::getTargetId).toList();
        List<Estate> estates = estateRepository.findAllById(estateIds);

        Map<UUID, Estate> estateMap = estates.stream()
                .collect(Collectors.toMap(Estate::getId, e -> e));

        response.items = estateIds.stream()
                .map(estateMap::get)
                .filter(Objects::nonNull)
                .toList();
        return response;
    }

    public EstateRecommendationResponse getRecosByEstateId(UUID estateId, PagingBody pagingBody) {
        List<Recommendation> recommendations;
        if (pagingBody.lastScore == null || pagingBody.lastId == null) {
            recommendations = recommendationRepository.getListForEstate(estateId, pagingBody.count);
        } else {
            recommendations = recommendationRepository.getListForEstateWithPaging(estateId, pagingBody.count, pagingBody.lastScore, pagingBody.lastId);
        }
        EstateRecommendationResponse estateRecommendationResponse = new EstateRecommendationResponse();
        fillPagingResponse(recommendations, estateRecommendationResponse);

        if (CollectionUtils.isEmpty(recommendations)) {
            estateRecommendationResponse.items = Collections.emptyList();
            return estateRecommendationResponse;
        }

        List<UUID> leadIds = recommendations.stream()
                .map(Recommendation::getSourceId)
                .toList();

        List<Lead> leads = leadRepository.findAllById(leadIds);
        leads.forEach(lead -> lead.setName("not accessed from recommender"));

        Map<UUID, Lead> leadMap = leads.stream()
                .collect(Collectors.toMap(Lead::getId, l -> l));

        estateRecommendationResponse.items = leadIds.stream()
                .map(leadMap::get)
                .filter(Objects::nonNull)
                .toList();
        return estateRecommendationResponse;
    }

    public void initiateRecommendations() {
        recommendationRepository.deleteAll();
        List<Lead> leads = leadRepository.findAll();
        List<Estate> estates = estateRepository.findAll();

        for (Lead lead : leads) {
            for (Estate estate : estates) {
                if (estate.getUserId().equals(lead.getUserId())) {
                    continue;
                }
                ScoreCalculationResult score = calculateSimilarityScore(lead, estate);

                Recommendation rec = new Recommendation();
                rec.setSourceId(lead.getId());
                rec.setSourceType(LEAD);
                rec.setTargetId(estate.getId());
                rec.setScore(score.score());
                rec.setReason(score.reason());
                recommendationRepository.save(rec);
            }
        }
    }

    public void initiateRecommendationsForLead(UUID leadId) {
        Lead lead = leadRepository.findById(leadId).orElse(null);
        if (lead == null) {
            throw new ResourceNotFoundException("Not found lead with id: %s, while recommendations initialization"
                                                        .formatted(leadId));
        }

        List<Estate> estates = estateRepository.findEstatesFromOtherUsers(lead.getUserId());

        for (Estate estate : estates) {
            recommendationRepository.save(createRecommendationEntity(lead, estate));
        }
    }

    public void recalculateRecommendationsForEstate(UUID estateId) {
        recommendationRepository.deleteAllByEstate(estateId);
        initiateRecommendationsForEstate(estateId);
    }

    public void recalculateRecommendationsForLead(UUID leadId) {
        deleteRecommendationsForLead(leadId);
        initiateRecommendationsForLead(leadId);
    }

    public void deleteRecommendationsForLead(UUID leadId) {
        recommendationRepository.deleteAllByLead(leadId);
    }

    public void initiateRecommendationsForEstate(UUID estateId) {
        Estate estate = estateRepository.findById(estateId).orElse(null);
        if (estate == null) {
            throw new ResourceNotFoundException("Not found estate with id: %s, while recommendations initialization"
                                                        .formatted(estate));
        }

        List<Lead> leads = leadRepository.findLeadsFromOtherUsers(estate.getUserId());

        for (Lead lead : leads) {
            recommendationRepository.save(createRecommendationEntity(lead, estate));
        }
    }

    private void fillPagingResponse(List<Recommendation> recommendations, RecommendationResponseWithPaging responseWithPaging) {
        if (CollectionUtils.isEmpty(recommendations)) {
            return;
        }
        Recommendation lastOne = recommendations.get(recommendations.size() - 1);
        responseWithPaging.lastId = lastOne.getId();
        responseWithPaging.lastScore = lastOne.getScore();
    }
    private Recommendation createRecommendationEntity(Lead lead, Estate estate) {
        Recommendation rec = new Recommendation();
        rec.setSourceId(lead.getId());
        rec.setSourceType(LEAD);
        rec.setTargetId(estate.getId());

        ScoreCalculationResult score = calculateSimilarityScore(lead, estate);
        rec.setScore(score.score());
        rec.setReason(score.reason());
        return rec;
    }

    private ScoreCalculationResult calculateSimilarityScore(Lead lead, Estate estate) {
        double score = 0.0;
        double totalWeight = 0.0;

        List<AttributeResult> results = List.of(
                calcPriceScore(lead, estate),
                calcAreaScore(lead, estate),
                calcBedroomScore(lead, estate),
                calcCommissionScore(lead, estate),
                calcEmbeddingScore(lead, estate)
        );

        String maxAttribute = null;
        double maxValue = 0.0;

        for (AttributeResult r : results) {
            score += r.score();
            totalWeight += r.weight();

            if (r.score() > maxValue) {
                maxValue = r.score();
                maxAttribute = r.attributeName();
            }
        }

        double normalized = totalWeight > 0
                ? Math.round((score / totalWeight) * PERCENT) / PERCENT
                : 0.0;

        return new ScoreCalculationResult(normalized, maxAttribute);
    }

    private AttributeResult calcAreaScore(Lead lead, Estate estate) {
        Requirements req = lead.getRequirements();
        EstateAttributes attrs = estate.getAttributes();

        if (req.getMinArea() == null || req.getMaxArea() == null || attrs.getAreaCommon() == null) {
            return AttributeResult.empty();
        }

        double avg = (req.getMinArea() + req.getMaxArea()) / 2.0;
        double range = req.getMaxArea() - req.getMinArea();
        double diff = Math.abs(attrs.getAreaCommon() - avg);
        double factor = 1.0 - Math.min(diff / range, 1.0);

        double score = W_AREA * factor;
        return new AttributeResult(W_AREA, score, "area");
    }

    private AttributeResult calcPriceScore(Lead lead, Estate estate) {
        Requirements req = lead.getRequirements();
        EstateAttributes attrs = estate.getAttributes();

        if (req.getMinPrice() == null || req.getMaxPrice() == null || attrs.getPrice() == null) {
            return AttributeResult.empty();
        }

        double avg = (req.getMinPrice() + req.getMaxPrice()) / 2.0;
        double range = req.getMaxPrice() - req.getMinPrice();
        double diff = Math.abs(attrs.getPrice() - avg);
        double factor = 1.0 - Math.min(diff / range, 1.0);

        double score = W_PRICE * factor;
        return new AttributeResult(W_PRICE, score, "price");
    }

    private AttributeResult calcBedroomScore(Lead lead, Estate estate) {
        Requirements req = lead.getRequirements();
        EstateAttributes attrs = estate.getAttributes();

        if (req.getBedrooms() == null || attrs.getRooms() == null) {
            return AttributeResult.empty();
        }

        double diff = Math.abs(req.getBedrooms() - attrs.getRooms());
        double factor = Math.max(0.0, 1.0 - diff / BEDROOM_SCORE_CONST);

        double score = W_BEDROOMS * factor;
        return new AttributeResult(W_BEDROOMS, score, "bedrooms");
    }

    private AttributeResult calcCommissionScore(Lead lead, Estate estate) {
        if (lead.getCommissionShare() == null || estate.getCommissionShare() == null) {
            return AttributeResult.empty();
        }

        double diff = Math.abs(lead.getCommissionShare() - estate.getCommissionShare());
        double factor;

        if (diff <= 10) {
            factor = 1.0;
        } else if (diff >= 30) {
            factor = 0.0;
        } else {
            factor = 1.0 - ((diff - 10) / 20.0);
        }

        double score = W_COMMISSION * factor;
        return new AttributeResult(W_COMMISSION, score, "commissionShare");
    }

    private AttributeResult calcEmbeddingScore(Lead lead, Estate estate) {
        try {
            Double similarity = embeddingService.compareObjectsDescription(lead, estate);
            if (similarity == null) {
                return AttributeResult.empty();
            }

            double score = W_VECTOR_SIMILARITY * similarity;
            return new AttributeResult(W_VECTOR_SIMILARITY, score, "embedding(description)");
        } catch (Exception ex) {
            System.err.println("Embedding compare failed: " + ex.getMessage());
            return AttributeResult.empty();
        }
    }

    private record AttributeResult(double weight, double score, String attributeName) {
        static AttributeResult empty() {
            return new AttributeResult(0.0, 0.0, null);
        }
    }
}
