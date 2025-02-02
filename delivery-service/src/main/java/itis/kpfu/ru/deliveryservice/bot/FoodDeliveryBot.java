package itis.kpfu.ru.deliveryservice.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class FoodDeliveryBot extends TelegramLongPollingBot {

    public FoodDeliveryBot(@Value("${telegram-bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            String response = "Вы сказали: " + messageText;
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(response);

            try {
                execute(message); // Отправка сообщения
            } catch (TelegramApiException e) {
                log.error("Telegram bot error", e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "FoodDeliveryClubBot";
    }
}
