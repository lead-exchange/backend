package lead.exchange.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class TelegramNotificationService {

    private final TelegramBot telegramBot;
    private final Set<Long> requestedPhone = ConcurrentHashMap.newKeySet();
    private final UserService userService;

    @Autowired
    public TelegramNotificationService(TelegramBot telegramBot, UserService userService) {
        this.telegramBot = telegramBot;
        this.userService = userService;
        MyUpdateListener myUpdateListener = new MyUpdateListener();
        telegramBot.setUpdatesListener(myUpdateListener);
    }

    public void sendNotification(String message, long chatId) {
        SendMessage request = new SendMessage(chatId, message);
        telegramBot.execute(request);
    }

    public void requestPhoneShare(long chatId) {
        if (requestedPhone.contains(chatId)) {
            return;
        }

        KeyboardButton btn = new KeyboardButton("Отправить номер 📱").requestContact(true);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(btn);
        keyboard.resizeKeyboard(true).oneTimeKeyboard(true);

        SendMessage msg = new SendMessage(chatId, "Чтобы продолжить, поделитесь номером 👇");
        msg.replyMarkup(keyboard);

        telegramBot.execute(msg);

        // отмечаем, что уже отправили
        requestedPhone.add(chatId);
    }

    private class MyUpdateListener implements UpdatesListener {

        @Override
        public int process(List<Update> updates) {
            for (Update update : updates) {
                if (update.message() != null && "/start".equals(update.message().text())) {
                    requestPhoneShare(update.message().chat().id());
                }

                if (update.message() != null && update.message().contact() != null) {
                    String phone = update.message().contact().phoneNumber();
                    Long chatId = update.message().chat().id();
                    String telegramId = update.message().from().username();

                    userService.savePhoneAndChat(telegramId, chatId, phone);

                    SendMessage msg = new SendMessage(chatId, "Спасибо! Телефон сохранён 📱");
                    telegramBot.execute(msg);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }
    }
}