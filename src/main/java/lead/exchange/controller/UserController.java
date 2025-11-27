package lead.exchange.controller;

import lead.exchange.entity.User;
import lead.exchange.service.TelegramNotificationService;
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
    private final TelegramNotificationService telegramNotificationService;

    @GetMapping("/users/{telegramId}")
    public ResponseEntity<User> getUserByTelegramId(@PathVariable String telegramId) {
        return ResponseEntity.ok(userService.getUserByTelegramId(telegramId));
    }

    @PostMapping("/testSending/{chatId}")
    public ResponseEntity<Void> testSending(@PathVariable long chatId) {
        telegramNotificationService.sendNotification("test hi", chatId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/testPhoneRequest/{chatId}")
    public ResponseEntity<Void> testPhoneRequest(@PathVariable long chatId) {
        telegramNotificationService.requestPhoneShare(chatId);
        return ResponseEntity.ok().build();
    }
}
