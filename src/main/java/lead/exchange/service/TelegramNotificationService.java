package lead.exchange.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TelegramNotificationService {

    private final TelegramBot telegramBot;

    @Autowired
    public TelegramNotificationService(TelegramBot telegramBot, MyUpdateListener myUpdateListener) {
        this.telegramBot = telegramBot;
        telegramBot.setUpdatesListener(myUpdateListener);
    }

    public void sendNotification(String message, long chatId) {
        SendMessage request = new SendMessage(chatId, message);
        telegramBot.execute(request);
    }

    public void requestPhoneShare(long chatId) {
        KeyboardButton btn = new KeyboardButton("Отправить номер 📱").requestContact(true);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(btn);
        keyboard.resizeKeyboard(true).oneTimeKeyboard(true);

        SendMessage msg = new SendMessage(chatId, "Чтобы продолжить, поделитесь номером 👇");
        msg.replyMarkup(keyboard);

        telegramBot.execute(msg);
    }

    @Component
    private class MyUpdateListener implements UpdatesListener {

        @Override
        public int process(List<Update> list) {
            for (Update update : list) {
                log.info(update.toString());
                if (update.message() != null && "/start".equals(update.message().text())) {
                    requestPhoneShare(update.message().chat().id());
                }
            }
            return 0;
        }
    }
}