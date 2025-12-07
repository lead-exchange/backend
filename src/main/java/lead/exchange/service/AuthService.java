package lead.exchange.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import lead.exchange.entity.User;
import lead.exchange.security.models.CurrentUser;
import lead.exchange.security.models.TelegramChat;
import lead.exchange.security.models.TelegramUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final Clock clock;

    @Transactional
    public CurrentUser ensureAndLoadCurrentUser(TelegramUser tgUser, TelegramChat tgChat) {
        if (tgUser == null || tgUser.id == null) {
            throw new IllegalArgumentException("Telegram user is missing");
        }

        Optional<User> userOpt = userService.getOptionalUserByTelegramId(tgUser.id);

        LocalDateTime now = LocalDateTime.now(clock);

        User user = userOpt.orElseGet(() -> {
            User u = new User();
            u.setTelegramId(tgUser.id);
            u.setChatId(tgChat != null ? tgChat.id : null);
            u.setCreatedAt(now);
            u.setUpdatedAt(now);

            return userService.saveUser(u);
        });

        return new CurrentUser(user.getId(), user.getTelegramId(), tgUser.username);
    }
}
