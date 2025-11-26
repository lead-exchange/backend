package lead.exchange.controller;

import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Lead;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeadController {
    private final LeadService leadService;

    @GetMapping("/lead/{userId}") // TODO: change to /leads (s at the end): move /leads to @RequestMapping
    public ResponseEntity<List<Lead>> getLeadByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(leadService.findByUserId(userId));
    }

    @GetMapping("/leads/{leadId}")
    public ResponseEntity<Lead> getLeadById(@PathVariable UUID leadId) {
        return ResponseEntity.ok(leadService.findById(leadId));
    }

    @PostMapping("/leads")
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        return ResponseEntity.ok(leadService.createLead(lead));
    }

    @PutMapping("/leads/{leadId}")
    public ResponseEntity<Lead> updateLead(@PathVariable UUID leadId, @RequestBody Lead lead) {
        return ResponseEntity.ok(leadService.updateLead(leadId, lead));
    }

    @DeleteMapping("/leads/{leadId}")
    public ResponseEntity<Void> deleteLead(@PathVariable UUID leadId) {
        leadService.deleteLead(leadId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/leads/{leadId}/archive")
    public ResponseEntity<Lead> archiveLead(@PathVariable UUID leadId) {
        return ResponseEntity.ok(leadService.archiveLead(leadId));
    }
}
