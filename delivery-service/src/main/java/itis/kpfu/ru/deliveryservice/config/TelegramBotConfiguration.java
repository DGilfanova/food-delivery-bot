package itis.kpfu.ru.deliveryservice.config;

import itis.kpfu.ru.deliveryservice.bot.FoodDeliveryBotListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(FoodDeliveryBotListener foodDeliveryBotListener) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(foodDeliveryBotListener);
        return api;
    }
}
