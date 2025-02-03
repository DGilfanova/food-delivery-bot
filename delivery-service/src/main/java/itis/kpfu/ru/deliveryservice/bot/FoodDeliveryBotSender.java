package itis.kpfu.ru.deliveryservice.bot;

import itis.kpfu.ru.deliveryservice.db.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.StopMessageLiveLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageLiveLocation;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class FoodDeliveryBotSender extends DefaultAbsSender {

    protected FoodDeliveryBotSender(@Value("${telegram-bot.token}") String botToken) {
        super(new DefaultBotOptions(), botToken);
    }

    public Integer sendInitialLocation(Long chatId, Double latitude, Double longitude) {
        SendLocation location = new SendLocation();
        location.setChatId(String.valueOf(chatId));
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setLivePeriod(3600);

        try {
            Message sentMessage = execute(location);
            return sentMessage.getMessageId();
        } catch (TelegramApiException e) {
            log.error("Telegram API error", e);
            throw new RuntimeException("Telegram API error");
        }
    }

    public void updateLocation(Integer messageId, Long chatId, Double newLatitude, Double newLongitude) {
        if (messageId == null) {
            return;
        }

        var editLocation = new EditMessageLiveLocation();
        editLocation.setChatId(String.valueOf(chatId));
        editLocation.setMessageId(messageId);
        editLocation.setLatitude(newLatitude);
        editLocation.setLongitude(newLongitude);

        try {
            execute(editLocation);
        } catch (TelegramApiException e) {
            log.error("Telegram API error", e);
        }
    }

    public void stopLocationTranslation(Integer messageId, Long chatId) {
        if (messageId == null) {
            return;
        }

        var editLocation = new StopMessageLiveLocation();
        editLocation.setChatId(String.valueOf(chatId));
        editLocation.setMessageId(messageId);

        try {
            execute(editLocation);
        } catch (TelegramApiException e) {
            log.error("Telegram API error", e);
        }
    }

    public void sendStatusChangedMessage(Long chatId, OrderStatus status) {
        var message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("✨ Статус вашего заказа обновлен на «%s»".formatted(status.getDescription()));

        try {
            execute(message);
            log.info("Send new status = %s for chat = %s".formatted(status.name(), chatId));
        } catch (TelegramApiException e) {
            log.error("Telegram API error", e);
        }
    }
}
