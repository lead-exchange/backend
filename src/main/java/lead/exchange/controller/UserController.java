package lead.exchange.controller;

import java.util.UUID;
import lead.exchange.entity.User;
import lead.exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{telegramId}")
    public ResponseEntity<User> getUserByTelegramId(@PathVariable String telegramId) {
        return ResponseEntity.ok(userService.getUserByTelegramId(telegramId));
    }

    @PostMapping("/users/estates/fill/{userId}")
    public ResponseEntity<User> fillUserEstates(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.fillUserEstates(userId));
    }
}
