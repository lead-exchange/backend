package lead.exchange.controller;

import lead.exchange.entity.Lead;
import lead.exchange.service.LeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
@RestController
@RequiredArgsConstructor
public class LeadController {
    private final LeadService leadService;
    @GetMapping("/lead/{userId}")
    public ResponseEntity<List<Lead>> getLeadByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(leadService.findByUserId(userId));
    }
}
