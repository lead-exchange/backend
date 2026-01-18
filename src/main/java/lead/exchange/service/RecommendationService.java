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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private static final Map<String, String> CITY_SYNONYMS = Map.of(
            "мск", "москва",
            "msk", "москва",
            "москва з", "москва",
            "москва зао", "москва",
            "москва цао", "москва",
            "московская", "москва",
            "центр питера", "санкт петербург",
            "питер", "санкт петербург"
    );

    private static final double BEDROOM_SCORE_CONST = 3.0;
    private static final double PERCENT = 100.0;
    private final static double W_PRICE = 3;
    private final static double W_AREA = 2.5;
    private final static double W_KITCHEN_AREA = 1.5;
    private final static double W_BEDROOMS = 1;
    private final static double W_COMMISSION = 1.5;
    private final static double W_VECTOR_SIMILARITY = 1.5;

    private final static int HARD_LIMIT = 10;
    public static final String LEAD = "lead";

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
                .map(Recommendation::getSourceId)
                .toList();

        List<Lead> leads = leadRepository.findAllById(leadIds);
        leads.forEach(lead -> lead.setName("Имя скрыто"));

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
                if (estate.getUserId().equals(lead.getUserId())) {
                    continue;
                }
                ScoreCalculationResult score = calculateSimilarityScore(lead, estate);

                if (score.score() <= 0.0) {
                    continue;
                }

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
            Recommendation recommendation = createRecommendationEntity(lead, estate);
            if (recommendation != null) {
                recommendationRepository.save(recommendation);
            }
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
            Recommendation recommendation = createRecommendationEntity(lead, estate);
            if (recommendation != null) {
                recommendationRepository.save(recommendation);
            }
        }
    }

    private Recommendation createRecommendationEntity(Lead lead, Estate estate) {
        Recommendation rec = new Recommendation();
        rec.setSourceId(lead.getId());
        rec.setSourceType(LEAD);
        rec.setTargetId(estate.getId());

        if (lead.getUserId().equals(estate.getUserId())) {
            return null;
        }

        ScoreCalculationResult score = calculateSimilarityScore(lead, estate);

        if (score.score() == 0.0) {
            return null;
        }
        rec.setScore(score.score());
        rec.setReason(score.reason());
        return rec;
    }

    private ScoreCalculationResult calculateSimilarityScore(Lead lead, Estate estate) {
        double score = 0.0;
        double totalWeight = 0.0;

        Requirements req = lead.getRequirements();
        EstateAttributes attrs = estate.getAttributes();

        HardMatchResult hardMatchResult = checkHardConstraints(req, attrs);

        if (hardMatchResult.score == 0) {
            return new ScoreCalculationResult(
                    0,
                    !hardMatchResult.typeMatch ? "non-matched types" : "non-matched locations"
            );
        }

        List<AttributeResult> results = List.of(
                calcPriceScore(req, attrs),
                calcAreaScore(req, attrs),
                calcBedroomScore(req, attrs),
                calcKitchenAreaScore(req, attrs),
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

        normalized *= hardMatchResult.locationMatch;

        return new ScoreCalculationResult(normalized, maxAttribute);
    }

    private HardMatchResult checkHardConstraints(Requirements req, EstateAttributes attrs) {
        boolean typeMatch = req.getPropertyType() == null
                || attrs.getRealtyType() == null
                || req.getPropertyType().equals(attrs.getRealtyType());

        double locationMatch = locationMatch(req.getLocations(), attrs.getAddress());

        double hardScore;
        if (!typeMatch) {
            hardScore = 0.0;
        } else {
            hardScore = locationMatch;
        }

        return new HardMatchResult(typeMatch, locationMatch, hardScore);
    }

    private double locationMatch(List<String> locations, EstateAttributes.Address address) {
        List<String> normalizedLocations = locations.stream().map(this::normalizeLocationString).toList();
        String estateCity = extractEstateCity(address);

        if (estateCity == null || CollectionUtils.isEmpty(normalizedLocations)) {
            return 0.5; // неизвестно — нейтрально
        }

        if (normalizedLocations.contains(estateCity)) {
            return 1.0;
        }
        return 0;
    }

    private String normalizeLocationString(String location) {
        if (location == null) {
            return null;
        }

        String normalizedLocation = location.toLowerCase()
                .replaceAll("[^а-яa-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
        return CITY_SYNONYMS.getOrDefault(normalizedLocation, normalizedLocation);
    }

    private String extractEstateCity(EstateAttributes.Address address) {
        if (address == null) {
            return null;
        }

        if (!StringUtils.isEmpty(address.getCityName())) {
            return normalizeLocationString(address.getCityName());
        }
        if (!StringUtils.isEmpty(address.getPlaceName())) {
            return normalizeLocationString(address.getPlaceName());
        }
        if (!StringUtils.isEmpty(address.getRegionName())) {
            return normalizeLocationString(address.getRegionName());
        }
        return null;
    }

    private AttributeResult calcAreaScore(Requirements req, EstateAttributes attrs) {

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

    private AttributeResult calcKitchenAreaScore(Requirements req, EstateAttributes attrs) {

        if (req.getMinKitchenArea() == null || req.getMaxKitchenArea() == null || attrs.getAreaKitchen() == null) {
            return AttributeResult.empty();
        }

        double avg = (req.getMinKitchenArea() + req.getMaxKitchenArea()) / 2.0;
        double range = req.getMaxKitchenArea() - req.getMinKitchenArea();
        double diff = Math.abs(attrs.getAreaKitchen() - avg);
        double factor = 1.0 - Math.min(diff / range, 1.0);

        double score = W_KITCHEN_AREA * factor;
        return new AttributeResult(W_AREA, score, "area");
    }

    private AttributeResult calcPriceScore(Requirements req, EstateAttributes attrs) {
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

    private AttributeResult calcBedroomScore(Requirements req, EstateAttributes attrs) {
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
            double similarity = embeddingService.compareObjectsDescription(lead, estate);
            if (similarity == -1) {
                return AttributeResult.empty();
            }

            double score = W_VECTOR_SIMILARITY * similarity;
            return new AttributeResult(W_VECTOR_SIMILARITY, score, "embedding(description)");
        } catch (Exception ex) {
            System.err.println("Embedding compare failed: " + ex.getMessage());
            return AttributeResult.empty();
        }
    }

    record HardMatchResult(boolean typeMatch, double locationMatch, double score) {
    }

    private record AttributeResult(double weight, double score, String attributeName) {
        static AttributeResult empty() {
            return new AttributeResult(0.0, 0.0, null);
        }
    }
}
