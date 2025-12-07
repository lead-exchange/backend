package lead.exchange.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lead.exchange.dto.LeadCreateDto;
import lead.exchange.dto.LeadUpdateDto;
import lead.exchange.entity.Lead;
import lead.exchange.exception.ResourceNotFoundException;
import lead.exchange.mapper.LeadMapper;
import lead.exchange.model.LeadStatus;
import lead.exchange.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LeadService {
    private final LeadRepository leadRepository;
    private final UserService userService;
    private final RecommendationAsyncService recommendationAsyncService;
    private final Clock clock;
    private final LeadMapper leadMapper;

    public List<Lead> findByUserId(UUID userId) {
        userService.getUserById(userId);
        return leadRepository.findByUserId(userId);
    }

    public Lead findById(UUID leadId) {
        return leadRepository.findById(leadId).orElseThrow(
                () -> new ResourceNotFoundException("Lead not found with id: " + leadId)
        );
    }

    public Lead createLead(LeadCreateDto lead, UUID id) {
        userService.getUserById(id);
        Lead toSave = leadMapper.toEntity(lead);

        toSave.setStatus(LeadStatus.ACTIVE);
        toSave.setUserId(id);

        LocalDateTime timestamp = LocalDateTime.now(clock);
        toSave.setCreatedAt(timestamp);
        toSave.setUpdatedAt(timestamp);

        Lead created = leadRepository.save(toSave);

        recommendationAsyncService.initiateRecommendationsForLead(created.getId());
        return created;
    }

    public Lead updateLead(UUID leadId, LeadUpdateDto leadUpdate) {
        Lead existingLead = findById(leadId);

        existingLead.setName(leadUpdate.name());
        existingLead.setRequirements(leadUpdate.requirements());
        existingLead.setCommissionShare(leadUpdate.commissionShare());
        existingLead.setUpdatedAt(LocalDateTime.now(clock));

        Lead updated = leadRepository.save(existingLead);
        recommendationAsyncService.recalcForLead(updated.getId());

        return updated;
    }

    public void deleteLead(UUID leadId) {
        leadRepository.deleteById(leadId);
        recommendationAsyncService.deleteRecommendationsByLead(leadId);
    }

    public Lead archiveLead(UUID leadId) {
        Lead lead = findById(leadId);
        lead.setStatus(LeadStatus.ARCHIVE);
        lead.setUpdatedAt(LocalDateTime.now(clock));
        return leadRepository.save(lead);
    }

    public Lead unarchiveLead(UUID leadId) {
        Lead lead = findById(leadId);
        lead.setStatus(LeadStatus.ACTIVE);
        lead.setUpdatedAt(LocalDateTime.now(clock));
        return leadRepository.save(lead);
    }
}
