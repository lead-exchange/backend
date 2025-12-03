package lead.exchange.service;

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

    private final RecommendationsRepository recommendationRepository;
    private final LeadRepository leadRepository;
    private final EstateRepository estateRepository;
    private final EmbeddingService embeddingService;


    public List<Estate> getRecosByLeadId(UUID leadId) {
        List<Recommendation> recommendations = recommendationRepository.getListForLead(leadId, HARD_LIMIT);
        if (CollectionUtils.isEmpty(recommendations)) {
            throw new ResourceNotFoundException("Not found with recommendations for lead: " + leadId);
        }
        List<UUID> estateIds = recommendations.stream().map(Recommendation::getTargetId).toList();
        List<Estate> estates = estateRepository.findAllById(estateIds);

        Map<UUID, Estate> estateMap = estates.stream()
                .collect(Collectors.toMap(Estate::getId, e -> e));

        return estateIds.stream()
                .map(estateMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<Lead> getRecosByEstateId(UUID estateId) {
        List<Recommendation> recommendations = recommendationRepository.getListForEstate(estateId, HARD_LIMIT);
        if (CollectionUtils.isEmpty(recommendations)) {
            throw new ResourceNotFoundException("Not found with recommendations for estate: " + estateId);
        }

        List<UUID> leadIds = recommendations.stream()
                .map(Recommendation::getTargetId)
                .toList();

        List<Lead> leads = leadRepository.findAllById(leadIds);
        leads.forEach(lead -> lead.setName("not accessed from recommender"));

        Map<UUID, Lead> leadMap = leads.stream()
                .collect(Collectors.toMap(Lead::getId, l -> l));

        return leadIds.stream()
                .map(leadMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public void initiateRecommendations() {
        recommendationRepository.deleteAll();
        List<Lead> leads = leadRepository.findAll();
        List<Estate> estates = estateRepository.findAll();

        for (Lead lead : leads) {
            for (Estate estate : estates) {
                ScoreCalculationResult score = calculateSimilarityScore(lead, estate);

                Recommendation rec = new Recommendation();
                rec.setSourceId(lead.getId());
                rec.setSourceType("lead");
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
            throw new ResourceNotFoundException("Not found lead with id: %s, while recommendations initialization".formatted(leadId));
        }

        List<Estate> estates = estateRepository.findAll();

        for (Estate estate : estates) {
            recommendationRepository.save(createRecommendationEntity(lead, estate));
        }
    }

    public void initiateRecommendationsForEstate(UUID estateId) {
        Estate estate = estateRepository.findById(estateId).orElse(null);
        if (estate == null) {
            throw new ResourceNotFoundException("Not found estate with id: %s, while recommendations initialization".formatted(estate));
        }

        List<Lead> leads = leadRepository.findAll();

        for (Lead lead : leads) {
            recommendationRepository.save(createRecommendationEntity(lead, estate));
        }
    }

    private Recommendation createRecommendationEntity(Lead lead, Estate estate) {
        Recommendation rec = new Recommendation();
        rec.setSourceId(lead.getId());
        rec.setSourceType("lead");
        rec.setTargetId(estate.getId());

        ScoreCalculationResult score = calculateSimilarityScore(lead, estate);
        rec.setScore(score.score());
        rec.setReason(score.reason());
        return rec;
    }

    private ScoreCalculationResult calculateSimilarityScore(Lead lead, Estate estate) {
        double score = 0.0;
        double totalWeight = 0.0;
        Requirements requirements = lead.getRequirements();
        EstateAttributes estateAttributes = estate.getAttributes();

        // Веса признаков

//        --- Тип недвижимости ---
//        if (req.getPropertyType() != null && estateAttributes.getPropertyType() != null) {
//            totalWeight += W_TYPE;
//            if (req.getPropertyType().equalsIgnoreCase(estateAttributes.getPropertyType())) {
//                score += W_TYPE;
//            }
//        }

        String maxSimilarityAttribute = null;
        double maxSimilarityValue = 0;

        // --- Цена ---
        if (
            requirements.getMinPrice() != null
                && requirements.getMaxPrice() != null
                && estateAttributes.getPrice() != null
        ) {
            totalWeight += W_PRICE;
            double avgPrice = (requirements.getMinPrice() + requirements.getMaxPrice()) / 2.0;
            double range = requirements.getMaxPrice() - requirements.getMinPrice();
            double diff = Math.abs(estateAttributes.getPrice() - avgPrice);
            double priceScore = 1.0 - Math.min(diff / range, 1.0);
            score += W_PRICE * priceScore;

            if (W_PRICE * priceScore > maxSimilarityValue) {
                maxSimilarityValue = W_PRICE * priceScore;
                maxSimilarityAttribute = "price";
            }
        }

        // --- Площадь ---
        if (
            requirements.getMinArea() != null
                && requirements.getMaxArea() != null
                && estateAttributes.getAreaCommon() != null
        ) {
            totalWeight += W_AREA;
            double avgArea = (requirements.getMinArea() + requirements.getMaxArea()) / 2.0;
            double range = requirements.getMaxArea() - requirements.getMinArea();
            double diff = Math.abs(estateAttributes.getAreaCommon() - avgArea);
            double areaScore = 1.0 - Math.min(diff / range, 1.0);
            score += W_AREA * areaScore;

            if (W_AREA * areaScore > maxSimilarityValue) {
                maxSimilarityValue = W_AREA * areaScore;
                maxSimilarityAttribute = "area";
            }
        }

        // --- Локации ---
//        if (requirements.getLocations() != null && !requirements.getLocations().isEmpty()
//        && estateAttributes.getLocation() != null) {
//            totalWeight += W_LOCATION;
//            boolean match = requirements.getLocations().stream()
//                    .anyMatch(loc -> loc.equalsIgnoreCase(estateAttributes.getLocation()));
//            score += W_LOCATION * (match ? 1.0 : 0.0);
//        }

        // --- Количество спален ---
        if (requirements.getBedrooms() != null && estateAttributes.getRooms() != null) {
            totalWeight += W_BEDROOMS;
            double diff = Math.abs(requirements.getBedrooms() - estateAttributes.getRooms());
            double bedroomScore = Math.max(0, 1.0 - diff / BEDROOM_SCORE_CONST);
            score += W_BEDROOMS * bedroomScore;

            if (W_BEDROOMS * bedroomScore > maxSimilarityValue) {
                maxSimilarityValue = W_BEDROOMS * bedroomScore;
                maxSimilarityAttribute = "bedrooms quantity";
            }
        }

        // --- Commission Share ---
        if (lead.getCommissionShare() != null && estate.getCommissionShare() != null) {
            totalWeight += W_COMMISSION;

            double leadComm = lead.getCommissionShare();
            double estateComm = estate.getCommissionShare();
            double diff = Math.abs(leadComm - estateComm); // разница в процентах

            double commissionScore;
            if (diff <= 10) {
                commissionScore = 1.0; // идеально
            } else if (diff >= 30) {
                commissionScore = 0.0; // критично
            } else {
                // линейное падение от 10% до 30%
                commissionScore = 1.0 - ((diff - 10) / 20.0); // 20 = 30-10
            }

            score += W_COMMISSION * commissionScore;

            if (W_COMMISSION * commissionScore > maxSimilarityValue) {
                maxSimilarityValue = W_COMMISSION * commissionScore;
                maxSimilarityAttribute = "commissionShare";
            }
        }

        // --- Описание объекта / семантическая близость (Embedding) ---
        try {
            Double similarity = embeddingService.compareObjectsDescription(lead, estate);
            if (similarity != null) {
                totalWeight += W_VECTOR_SIMILARITY;

                // similarity ∈ [0..1] — готовый коэффициент
                score += W_VECTOR_SIMILARITY * similarity;

                if (W_VECTOR_SIMILARITY * similarity > maxSimilarityValue) {
                    maxSimilarityValue = W_VECTOR_SIMILARITY * similarity;
                    maxSimilarityAttribute = "embedding(description)";
                }
            }
        } catch (Exception ex) {
            System.err.println("Embedding compare failed: " + ex.getMessage());
        }

        double normalizedScore = totalWeight > 0 ? Math.round((score / totalWeight) * PERCENT) / PERCENT : 0.0;
        return new ScoreCalculationResult(normalizedScore, maxSimilarityAttribute);
    }
}
