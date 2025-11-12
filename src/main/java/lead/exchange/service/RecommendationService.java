package lead.exchange.service;

import lead.exchange.entity.Estate;
import lead.exchange.entity.Lead;
import lead.exchange.repository.RecommendationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationsRepository recommendationRepository;

    public List<Estate> getRecosByLeadId(UUID leadId) {
        return null;
    }

    public List<Lead> getRecosByEstateId(UUID estateId) {
        return null;
    }
}
