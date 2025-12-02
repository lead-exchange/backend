package lead.exchange.controller;

import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Estate;
import lead.exchange.service.EstateService;
import lead.exchange.service.UserService;
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
    private final UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Estate>> getEstateByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(estateService.getEstateByUserId(userId));
    }

    @GetMapping("/{estateId}")
    public ResponseEntity<Estate> getEstateById(@PathVariable UUID estateId) {
        return ResponseEntity.ok(estateService.findById(estateId));
    }

    @PostMapping("/{estateId}/archive")
    public ResponseEntity<Estate> archiveEstate(@PathVariable UUID estateId) {
        return ResponseEntity.ok(estateService.archiveEstate(estateId));
    }

    @PostMapping("/refresh/{userId}")
    public ResponseEntity<Void> refresh(@PathVariable UUID userId) {
        userService.fillUserEstates(userId);
        return ResponseEntity.ok().build();
    }

}
