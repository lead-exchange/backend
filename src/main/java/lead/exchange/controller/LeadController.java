package lead.exchange.controller;

import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.UUID;
import lead.exchange.dto.LeadCreateDto;
import lead.exchange.dto.LeadUpdateDto;
import lead.exchange.entity.Lead;
import lead.exchange.security.models.CurrentUser;
import lead.exchange.service.LeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @GetMapping
    public ResponseEntity<List<Lead>> getMyLeads(@Parameter(hidden = true) CurrentUser currentUser) {
        return ResponseEntity.ok(leadService.findByUserId(currentUser.getId()));
    }

    @GetMapping("/{leadId}")
    public ResponseEntity<Lead> getLeadById(@PathVariable UUID leadId) {
        return ResponseEntity.ok(leadService.findById(leadId));
    }

    @PostMapping
    public ResponseEntity<Lead> createLead(
        @RequestBody LeadCreateDto lead,
        @Parameter(hidden = true) CurrentUser currentUser
    ) {
        return ResponseEntity.ok(leadService.createLead(lead, currentUser.getId()));
    }

    @PutMapping("/{leadId}")
    public ResponseEntity<Lead> updateLead(@PathVariable UUID leadId, @RequestBody LeadUpdateDto lead) {
        return ResponseEntity.ok(leadService.updateLead(leadId, lead));
    }

    @DeleteMapping("/{leadId}")
    public ResponseEntity<Void> deleteLead(@PathVariable UUID leadId) {
        leadService.deleteLead(leadId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{leadId}/archive")
    public ResponseEntity<Lead> archiveLead(@PathVariable UUID leadId) {
        return ResponseEntity.ok(leadService.archiveLead(leadId));
    }

    @PostMapping("/{leadId}/unarchive")
    public ResponseEntity<Lead> unarchiveLead(@PathVariable UUID leadId) {
        return ResponseEntity.ok(leadService.unarchiveLead(leadId));
    }
}
