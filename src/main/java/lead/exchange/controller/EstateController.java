package lead.exchange.controller;

import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Estate;
import lead.exchange.security.models.CurrentUser;
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

    @GetMapping
    public ResponseEntity<List<Estate>> getEstateByUserId(@Parameter(hidden = true) CurrentUser currentUser) {
        return ResponseEntity.ok(estateService.getEstateByUserId(currentUser.getId()));
    }

    @GetMapping("/{estateId}")
    public ResponseEntity<Estate> getEstateById(@PathVariable UUID estateId) {
        return ResponseEntity.ok(estateService.findById(estateId));
    }

    @PostMapping("/{estateId}/archive")
    public ResponseEntity<Estate> archiveEstate(@PathVariable UUID estateId) {
        return ResponseEntity.ok(estateService.archiveEstate(estateId));
    }

    @PostMapping("/{estateId}/unarchive")
    public ResponseEntity<Estate> unarchiveEstate(@PathVariable UUID estateId) {
        return ResponseEntity.ok(estateService.unarchiveEstate(estateId));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@Parameter(hidden = true) CurrentUser currentUser) {
        userService.fillUserEstates(currentUser.getId());
        return ResponseEntity.ok().build();
    }

}
