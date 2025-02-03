package itis.kpfu.ru.deliveryservice.service;

public interface OrderService {
    void createOrder(Long chatId);
    void acceptOrder(Long courierId, Long orderId);
    void deliverOrder(Long courierId, Long orderId);
}
