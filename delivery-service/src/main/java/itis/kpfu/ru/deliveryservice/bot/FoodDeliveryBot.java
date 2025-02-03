package itis.kpfu.ru.deliveryservice.bot;

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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FoodDeliveryBot extends TelegramLongPollingBot {
    private final Map<Long, List<String>> cart = new HashMap<>();
    private final Map<Long, List<String>> tempCart = new HashMap<>();
    private final List<String> menu = Arrays.asList("Пицца", "Суши", "Бургер", "Паста", "Салат");
    private final Map<Long, String> userState = new HashMap<>();

    public FoodDeliveryBot(@Value("${telegram-bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.hasMessage() ? update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId();
        String data = update.hasMessage() ? update.getMessage().getText() : update.getCallbackQuery().getData();

        if (data.equals("/start") || data.equals("Главное меню")) {
            sendMainMenu(chatId);
        } else if (data.equals("Меню")) {
            sendMenuWithButtons(chatId);
        } else if (data.equals("Подтвердить")) {
            cart.put(chatId, new ArrayList<>(tempCart.getOrDefault(chatId, new ArrayList<>())));
            tempCart.remove(chatId);
            sendMainMenu(chatId);
        } else if (data.equals("Отменить")) {
            tempCart.remove(chatId);
            sendMainMenu(chatId);
        } else if (menu.contains(data)) {
            tempCart.computeIfAbsent(chatId, k -> new ArrayList<>());
            if (tempCart.get(chatId).contains(data)) {
                tempCart.get(chatId).remove(data);
            } else {
                tempCart.get(chatId).add(data);
            }
            updateMenuWithButtons(chatId, update.getCallbackQuery().getMessage().getMessageId().longValue());
        } else if(data.equals("Заказ")){
            sendOrder(chatId);
        }else if(data.equals("Заказать")){
            confirmOrder(chatId);
        }
    }

    private void sendMainMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Главное меню");
        message.setReplyMarkup(getMainMenuKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup getMainMenuKeyboard() {
        List<List<InlineKeyboardButton>> keyboard = Arrays.asList(
                Arrays.asList(createButton("Заказ"), createButton("Меню"))
        );

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    private void sendMenuWithButtons(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Выберите блюда:");
        message.setReplyMarkup(getMenuKeyboard(chatId));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void updateMenuWithButtons(Long chatId, Long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId.toString());
        message.setMessageId(Math.toIntExact(messageId));
        message.setText("Выберите блюда:");
        message.setReplyMarkup(getMenuKeyboard(chatId));

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
        List<String> order = cart.getOrDefault(chatId, new ArrayList<>());
        String text; //= order.isEmpty() ? "Ваша корзина пуста." : "Ваш заказ: \n" + String.join("\n", order);

        if(order.isEmpty()){
            text = "Ваша корзина пуста";
        }else{
            text = "Ваш текущий заказ: \n" + String.join("\n", order) + "\n\n Нажмите 'Подтвердить' в меню";
        }
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        //Кнопка заказать
        if(!order.isEmpty()){
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(Collections.singletonList(Collections.singletonList(createButton("Заказать"))));
            message.setReplyMarkup(markup);
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Метод для подтверждения заказа
    private void confirmOrder(Long chatId){
        tempCart.put(chatId, new ArrayList<>(cart.getOrDefault(chatId, Collections.emptyList())));
        cart.remove(chatId);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Заказ сформирован");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "FoodDeliveryClubBot";
    }
}
