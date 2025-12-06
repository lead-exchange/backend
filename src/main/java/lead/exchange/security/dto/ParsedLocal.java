package lead.exchange.security.dto;

import lead.exchange.security.models.TelegramChat;
import lead.exchange.security.models.TelegramUser;


public record ParsedLocal(
    TelegramUser user,
    TelegramChat chat
) {

}
