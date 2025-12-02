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

    public static final double BEDROOM_SCORE_CONST = 3.0;
    public static final double PERSENT = 100.0;
    private final RecommendationsRepository recommendationRepository;
    private final LeadRepository leadRepository;
    private final EstateRepository estateRepository;
    private final static double W_TYPE = 0.1;
    private final static double W_PRICE = 0.35;
    private final static double W_AREA = 0.25;
    private final static double W_LOCATION = 0.2;
    private final static double W_BEDROOMS = 0.1;

    public List<Estate> getRecosByLeadId(UUID leadId) {
        List<Recommendation> recommendations = recommendationRepository.getListForLead(leadId);
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
        List<Recommendation> recommendations = recommendationRepository.getListForEstate(estateId);
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
        //todo filter entity with matches
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

    public ScoreCalculationResult calculateSimilarityScore(Lead lead, Estate estate) {
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
//        if (
//            requirements.getMinArea() != null
//                && requirements.getMaxArea() != null
//                && estateAttributes.getArea() != null
//        ) {
//            totalWeight += W_AREA;
//            double avgArea = (requirements.getMinArea() + requirements.getMaxArea()) / 2.0;
//            double range = requirements.getMaxArea() - requirements.getMinArea();
//            double diff = Math.abs(estateAttributes.getArea() - avgArea);
//            double areaScore = 1.0 - Math.min(diff / range, 1.0);
//            score += W_AREA * areaScore;
//
//            if (W_AREA * areaScore > maxSimilarityValue) {
//                maxSimilarityValue = W_AREA * areaScore;
//                maxSimilarityAttribute = "area";
//            }
//        }

        // --- Локации ---
//        if (requirements.getLocations() != null && !requirements.getLocations().isEmpty()
//        && estateAttributes.getLocation() != null) {
//            totalWeight += W_LOCATION;
//            boolean match = requirements.getLocations().stream()
//                    .anyMatch(loc -> loc.equalsIgnoreCase(estateAttributes.getLocation()));
//            score += W_LOCATION * (match ? 1.0 : 0.0);
//        }

        // --- Количество спален ---
//        if (requirements.getBedrooms() != null && estateAttributes.getBedrooms() != null) {
//            totalWeight += W_BEDROOMS;
//            double diff = Math.abs(requirements.getBedrooms() - estateAttributes.getBedrooms());
//            double bedroomScore = Math.max(0, 1.0 - diff / BEDROOM_SCORE_CONST);
//            score += W_BEDROOMS * bedroomScore;
//
//            if (W_BEDROOMS * bedroomScore > maxSimilarityValue) {
//                maxSimilarityValue = W_BEDROOMS * bedroomScore;
//                maxSimilarityAttribute = "bedrooms quantity";
//            }
//        }

        double normalizedScore = totalWeight > 0 ? Math.round((score / totalWeight) * PERSENT) / PERSENT : 0.0;
        return new ScoreCalculationResult(normalizedScore, maxSimilarityAttribute);
    }
}
