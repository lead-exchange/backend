package lead.exchange.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Lead;
import lead.exchange.exception.ResourceNotFoundException;
import lead.exchange.model.LeadStatus;
import lead.exchange.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LeadService {
    private final LeadRepository leadRepository;
    private final UserService userService;

    private Clock clock;

    public List<Lead> findByUserId(UUID userId) {
        userService.checkUserExistByUserId(userId);
        return leadRepository.findByUserId(userId);
    }

    public Lead findById(UUID leadId) {
        return leadRepository.findById(leadId).orElseThrow(
                () -> new ResourceNotFoundException("Lead not found with id: " + leadId)
        );
    }

    public Lead createLead(Lead lead) {
        userService.checkUserExistByUserId(lead.getUserId());

        lead.setStatus(LeadStatus.ACTIVE);

        LocalDateTime timestamp = LocalDateTime.now(clock);
        lead.setCreatedAt(timestamp);
        lead.setUpdatedAt(timestamp);

        return leadRepository.save(lead);
    }

    public Lead updateLead(UUID leadId, Lead leadUpdate) {
        Lead existingLead = findById(leadId);

        existingLead.setName(leadUpdate.getName());
        existingLead.setRequirements(leadUpdate.getRequirements());
        existingLead.setCommissionShare(leadUpdate.getCommissionShare());
        existingLead.setUpdatedAt(LocalDateTime.now(clock));

        return leadRepository.save(existingLead);
    }

    public void deleteLead(UUID leadId) {
        leadRepository.deleteById(leadId);
    }

    public Lead archiveLead(UUID leadId) {
        Lead lead = findById(leadId);
        lead.setStatus(LeadStatus.ARCHIVE);
        lead.setUpdatedAt(LocalDateTime.now(clock));
        return leadRepository.save(lead);
    }
}
