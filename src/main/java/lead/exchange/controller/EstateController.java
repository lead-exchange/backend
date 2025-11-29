package lead.exchange.controller;

import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Estate;
import lead.exchange.service.EstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/estates")
@RequiredArgsConstructor
public class EstateController {
    private final EstateService estateService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Estate>> getEstateByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(estateService.getEstateByUserId(userId));
    }

    @PostMapping("/{estateId}/archive")
    public ResponseEntity<Estate> archiveEstate(@PathVariable UUID estateId) {
        return ResponseEntity.ok(estateService.archiveEstate(estateId));
    }
}
