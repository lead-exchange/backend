package lead.exchange.controller;

import lead.exchange.entity.Estate;
import lead.exchange.service.EstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EstateController {
    private final EstateService estateService;

    @GetMapping("/estate/{userId}")
    public ResponseEntity<List<Estate>> getEstateByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(estateService.getEstateByUserId(userId));
    }
}
