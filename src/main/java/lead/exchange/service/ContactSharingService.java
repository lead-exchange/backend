package lead.exchange.service;

import lead.exchange.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactSharingService {

    private final TelegramNotificationService telegramNotificationService;

    public void sendTelegramContact(Long recipientChatId, User contactUser) {
        try {

            if (StringUtils.isBlank(contactUser.getPhone())) {
                sendAsContact(recipientChatId, contactUser);
            } else if (StringUtils.isBlank(contactUser.getTelegramUsername())) {
                log.warn("user with chatId {} don't have phone", recipientChatId);
                sendAsTelegramLink(recipientChatId, contactUser);
            } else {
                log.error("Failed to send contact to chatId: {}; user don't have phone and username", recipientChatId);
            }

        } catch (Exception e) {
            log.error("Failed to send contact to chatId: {}; user don't have phone and username", recipientChatId, e);
        }
    }

    private void sendAsContact(Long recipientChatId, User contactUser) {
        telegramNotificationService.sendContact(
                recipientChatId,
                contactUser.getPhone(),
                contactUser.getFirstName(),
                contactUser.getLastName()
        );
    }

    private void sendAsTelegramLink(Long recipientChatId, User contactUser) {
        String fullName = buildFullName(contactUser);
        String telegramLink = String.format("https://t.me/%s", contactUser.getTelegramUsername());

        String message = String.format(
                "👤 %s\n📱 Telegram: %s\n🔗 Ссылка: %s",
                fullName,
                "@" + contactUser.getTelegramUsername(),
                telegramLink
        );

        telegramNotificationService.sendNotification(message, recipientChatId);
    }

    private String buildFullName(User user) {
        StringBuilder name = new StringBuilder();
        if (user.getFirstName() != null) {
            name.append(user.getFirstName());
        }
        if (user.getLastName() != null) {
            if (!name.isEmpty()) {
                name.append(" ");
            }
            name.append(user.getLastName());
        }
        return name.isEmpty() ? "Пользователь" : name.toString();
    }
}
