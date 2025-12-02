package lead.exchange.security.dto;

import java.util.Map;
import lead.exchange.security.models.TelegramChat;
import lead.exchange.security.models.TelegramUser;


public record Parsed(
        String hash,
        String dataCheckString,
        Map<String, String> params,
        TelegramUser user,
        TelegramChat chat
) {
}
