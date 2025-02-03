package itis.kpfu.ru.deliveryservice.service.impl;

import itis.kpfu.ru.deliveryservice.bot.FoodDeliveryBotSender;
import itis.kpfu.ru.deliveryservice.db.enums.OrderStatus;
import itis.kpfu.ru.deliveryservice.db.model.Order;
import itis.kpfu.ru.deliveryservice.db.repository.OrderRepository;
import itis.kpfu.ru.deliveryservice.exception.BadRequestException;
import itis.kpfu.ru.deliveryservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final FoodDeliveryBotSender foodDeliveryBotSender;

    @Override
    public void createOrder(Long chatId) {
        var order = mapToEntity(chatId);
        orderRepository.create(order);

        mockOrderProcessingSteps(chatId);
    }

    @Override
    public void acceptOrder(Long courierId, Long orderId) {
        var order = orderRepository.findByOrderId(orderId)
            .orElseThrow(() -> new BadRequestException("Order not found"));
        checkOnStatus(order, OrderStatus.RESTAURANT_FINISHED);
        checkIfCourierAcceptAnotherOrder(order);

        var newStatus = OrderStatus.IN_DELIVERY;
        orderRepository.updateStatusAndCourierId(orderId, newStatus, courierId);

        foodDeliveryBotSender.sendStatusChangedMessage(order.chatId(), newStatus);
    }

    @Override
    public void deliverOrder(Long courierId, Long orderId) {
        var order = orderRepository.findByOrderId(orderId)
            .orElseThrow(() -> new BadRequestException("Order not found"));
        checkOnStatus(order, OrderStatus.IN_DELIVERY);

        var newStatus = OrderStatus.DELIVERED;
        orderRepository.updateStatusAndCourierId(orderId, newStatus, courierId);

        foodDeliveryBotSender.stopLocationTranslation(order.messageLocationId(), order.chatId());
        foodDeliveryBotSender.sendStatusChangedMessage(order.chatId(), newStatus);
    }

    private void checkOnStatus(Order order, OrderStatus status) {
        if (!status.equals(order.status())) {
            throw new BadRequestException("Invalid order status for courier action. Expected = %s, actual = %s"
                .formatted(status.name(), order.status()));
        }
    }

    private void checkIfCourierAcceptAnotherOrder(Order order) {
        if (orderRepository.findByCourierIdAndStatus(order.courierId(), OrderStatus.IN_DELIVERY).isPresent()) {
            throw new BadRequestException("Courier already has order for delivering");
        }
    }

    @SneakyThrows
    private void mockOrderProcessingSteps(Long chatId) {
        foodDeliveryBotSender.sendStatusChangedMessage(chatId, OrderStatus.FORMED_AND_PAID);
        Thread.sleep(2000);
        foodDeliveryBotSender.sendStatusChangedMessage(chatId, OrderStatus.RESTAURANT_APPROVED);
        Thread.sleep(2000);
        foodDeliveryBotSender.sendStatusChangedMessage(chatId, OrderStatus.IN_COOKING);
        Thread.sleep(5000);
        foodDeliveryBotSender.sendStatusChangedMessage(chatId, OrderStatus.RESTAURANT_FINISHED);
    }

    private Order mapToEntity(Long chatId) {
        return Order.builder()
            .chatId(chatId)
            .restaurantId(1L)
            .status(OrderStatus.RESTAURANT_FINISHED)
            .totalPrice(BigDecimal.valueOf(2344.0))
            .userId(1L)
            .items(Set.of(
                Order.Item.builder()
                    .dishId(1L)
                    .price(458.0)
                    .quantity(1)
                    .build(),
                Order.Item.builder()
                    .dishId(2L)
                    .price(998.0)
                    .quantity(1)
                    .build()
            ))
            .build();
    }
}
