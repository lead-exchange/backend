package lead.exchange.service;

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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationsRepository recommendationRepository;
    private final LeadRepository leadRepository;
    private final EstateRepository estateRepository;

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
    public ScoreCalculationResult calculateSimilarityScore(Lead lead, Estate estate) {
        double score = 0.0;
        double totalWeight = 0.0;
        Requirements requirements = lead.getRequirements();
        EstateAttributes estateAttributes = estate.getAttributes();

        // Веса признаков
        double wType = 0.1;
        double wPrice = 0.35;
        double wArea = 0.25;
        double wLocation = 0.2;
        double wBedrooms = 0.1;

//        --- Тип недвижимости ---
//        if (req.getPropertyType() != null && estateAttributes.getPropertyType() != null) {
//            totalWeight += wType;
//            if (req.getPropertyType().equalsIgnoreCase(estateAttributes.getPropertyType())) {
//                score += wType;
//            }
//        }

        String maxSimilarityAttribute = null;
        double maxSimilarityValue = 0;

        // --- Цена ---
        if (requirements.getMinPrice() != null && requirements.getMaxPrice() != null && estateAttributes.getPrice() != null) {
            totalWeight += wPrice;
            double avgPrice = (requirements.getMinPrice() + requirements.getMaxPrice()) / 2.0;
            double range = requirements.getMaxPrice() - requirements.getMinPrice();
            double diff = Math.abs(estateAttributes.getPrice() - avgPrice);
            double priceScore = 1.0 - Math.min(diff / range, 1.0);
            score += wPrice * priceScore;

            if (wPrice * priceScore > maxSimilarityValue) {
                maxSimilarityValue = wPrice * priceScore;
                maxSimilarityAttribute = "price";
            }
        }

        // --- Площадь ---
        if (requirements.getMinArea() != null && requirements.getMaxArea() != null && estateAttributes.getArea() != null) {
            totalWeight += wArea;
            double avgArea = (requirements.getMinArea() + requirements.getMaxArea()) / 2.0;
            double range = requirements.getMaxArea() - requirements.getMinArea();
            double diff = Math.abs(estateAttributes.getArea() - avgArea);
            double areaScore = 1.0 - Math.min(diff / range, 1.0);
            score += wArea * areaScore;

            if (wArea * areaScore > maxSimilarityValue) {
                maxSimilarityValue = wArea * areaScore;
                maxSimilarityAttribute = "area";
            }
        }

        // --- Локации ---
//        if (requirements.getLocations() != null && !requirements.getLocations().isEmpty() && estateAttributes.getLocation() != null) {
//            totalWeight += wLocation;
//            boolean match = requirements.getLocations().stream()
//                    .anyMatch(loc -> loc.equalsIgnoreCase(estateAttributes.getLocation()));
//            score += wLocation * (match ? 1.0 : 0.0);
//        }

        // --- Количество спален ---
        if (requirements.getBedrooms() != null && estateAttributes.getBedrooms() != null) {
            totalWeight += wBedrooms;
            double diff = Math.abs(requirements.getBedrooms() - estateAttributes.getBedrooms());
            double bedroomScore = Math.max(0, 1.0 - diff / 3.0);
            score += wBedrooms * bedroomScore;

            if (wBedrooms * bedroomScore > maxSimilarityValue) {
                maxSimilarityValue = wBedrooms * bedroomScore;
                maxSimilarityAttribute = "bedrooms quantity";
            }
        }

        int normalizedScore = (int) ( totalWeight > 0 ? Math.round((score / totalWeight) * 100.0) / 100.0 : 0.0);
        return new ScoreCalculationResult(normalizedScore, maxSimilarityAttribute);
    }
}
