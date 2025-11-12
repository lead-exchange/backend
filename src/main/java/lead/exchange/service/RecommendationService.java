package lead.exchange.service;

import lead.exchange.entity.Estate;
import lead.exchange.entity.Lead;
import lead.exchange.entity.Recommendation;
import lead.exchange.exception.ResourceNotFoundException;
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
}
