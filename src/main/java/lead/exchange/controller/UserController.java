package lead.exchange.controller;

import java.util.UUID;
import lead.exchange.entity.User;
import lead.exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{telegramId}")
    public ResponseEntity<User> getUserByTelegramId(@PathVariable String telegramId) {
        return ResponseEntity.ok(userService.getUserByTelegramId(telegramId));
    }

    @PatchMapping("/users/{userId}/phone")
    public ResponseEntity<User> updatePhone(
            @PathVariable UUID userId,
            @RequestParam("phone") String newPhone
    ) {
        return ResponseEntity.ok(userService.updatePhone(userId, newPhone));
    }


    // TODO: нужно при создании пользователя подтягивать его объекты
}
