package lead.exchange.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lead.exchange.entity.User;
import lead.exchange.security.models.CurrentUser;
import lead.exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<User> getUserByTelegramId(@Parameter(hidden = true) CurrentUser currentUser) {
        return ResponseEntity.ok(userService.getUserByTelegramId(currentUser.getTelegramId()));
    }

    @PatchMapping("/phone")
    public ResponseEntity<User> updatePhone(
            @Parameter(hidden = true) CurrentUser currentUser,
            @RequestParam("phone") String newPhone
    ) {
        return ResponseEntity.ok(userService.updatePhone(currentUser.getId(), newPhone));
    }

    // TODO: нужно при создании пользователя подтягивать его объекты
}
