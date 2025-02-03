package itis.kpfu.ru.deliveryservice.bot;

import itis.kpfu.ru.deliveryservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FoodDeliveryBotListener extends TelegramLongPollingBot {

    private final OrderService orderService;

    private final Map<Long, List<String>> cart = new HashMap<>();
    private final Map<Long, List<String>> tempCart = new HashMap<>();

    private final List<String> menu = Arrays.asList("Пицца", "Суши", "Бургер", "Паста", "Салат");

    public FoodDeliveryBotListener(@Value("${telegram-bot.token}") String botToken, OrderService orderService) {
        super(botToken);
        this.orderService = orderService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.hasMessage() ? update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId();
        String data = update.hasMessage() ? update.getMessage().getText() : update.getCallbackQuery().getData();
        log.info("Get chat message with data: " + data);

        switch (data) {
            case ("/start"), ("🍳 Главное меню") -> sendMainMenu(chatId);
            case ("🗒 Меню") -> sendMenuWithButtons(chatId);
            case ("Заказ") -> sendOrder(chatId);
            case ("🚫 Отменить") -> {
                tempCart.remove(chatId);
                sendMainMenu(chatId);
            }
            case ("Подтвердить") -> {
                cart.put(chatId, new ArrayList<>(tempCart.getOrDefault(chatId, new ArrayList<>())));
                tempCart.remove(chatId);
                sendMainMenu(chatId);
            }
            case ("Заказать") -> {
                tempCart.put(chatId, new ArrayList<>(cart.getOrDefault(chatId, Collections.emptyList())));
                cart.remove(chatId);
                orderService.createOrder(chatId);
            }
            default -> {
                if (menu.contains(data)) {
                    tempCart.computeIfAbsent(chatId, k -> new ArrayList<>());
                    if (tempCart.get(chatId).contains(data)) {
                        tempCart.get(chatId).remove(data);
                    } else {
                        tempCart.get(chatId).add(data);
                    }
                    updateMenuWithButtons(chatId, update.getCallbackQuery().getMessage().getMessageId().longValue());
                }
            }
        }
    }

    private void sendMainMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("🍳 Главное меню");
        message.setReplyMarkup(getMainMenuKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Telegram API error", e);
        }
    }

    private InlineKeyboardMarkup getMainMenuKeyboard() {
        List<List<InlineKeyboardButton>> keyboard = List.of(
            Arrays.asList(createButton("Заказ"), createButton("🗒 Меню"))
        );

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    private InlineKeyboardMarkup getOrderApproveKeyboard() {
        List<List<InlineKeyboardButton>> keyboard = List.of(
            List.of(createButton("Заказать"))
        );

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    private void sendMenuWithButtons(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("✔ Выберите блюда:");
        message.setReplyMarkup(getMenuKeyboard(chatId));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Telegram API error", e);
        }
    }

    private void updateMenuWithButtons(Long chatId, Long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(Math.toIntExact(messageId));
        message.setText("✔ Выберите блюда:");
        message.setReplyMarkup(getMenuKeyboard(chatId));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Telegram API error", e);
        }
    }

    private InlineKeyboardMarkup getMenuKeyboard(Long chatId) {
        List<List<InlineKeyboardButton>> keyboard = menu.stream()
            .map(item -> {
                InlineKeyboardButton button = new InlineKeyboardButton();
                boolean selected = tempCart.getOrDefault(chatId, new ArrayList<>()).contains(item);
                button.setText((selected ? "✅ " : "") + item);
                button.setCallbackData(item);
                return Collections.singletonList(button);
            })
            .collect(Collectors.toList());

        keyboard.add(Arrays.asList(
            createButton("Подтвердить"),
            createButton("🚫 Отменить")
        ));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    private InlineKeyboardButton createButton(String text) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(text);
        return button;
    }

    private void sendOrder(Long chatId) {
        List<String> orders = cart.getOrDefault(chatId, new ArrayList<>());
        String text = orders.isEmpty()
            ? "\uD83D\uDDD1 Ваша корзина пуста."
            : "\uD83D\uDDD2 Ваш заказ: \n" + orders.stream().map(e -> "➖" + e).collect(Collectors.joining("\n"));

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setReplyMarkup(getOrderApproveKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Telegram API error", e);
        }
    }

    @Override
    public String getBotUsername() {
        return "food-delivery-bot";
    }
}
