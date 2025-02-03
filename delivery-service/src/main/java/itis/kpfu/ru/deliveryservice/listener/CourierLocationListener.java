package itis.kpfu.ru.deliveryservice.listener;

import itis.kpfu.ru.deliveryservice.bot.FoodDeliveryBotSender;
import itis.kpfu.ru.deliveryservice.db.enums.OrderStatus;
import itis.kpfu.ru.deliveryservice.db.repository.OrderRepository;
import itis.kpfu.ru.deliveryservice.event.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourierLocationListener {

    private final FoodDeliveryBotSender foodDeliveryBotSender;
    private final OrderRepository orderRepository;

    @KafkaListener(
        topics = "${kafka.get-courier-location.topic}",
        containerFactory = "kafkaCourierLocationContainerFactory"
    )
    public void getCourierLocationEvent(@Payload Location location) {
        log.info("Get courier location event: " + location);

        var optionalOrder = orderRepository.findByCourierIdAndStatus(location.courierId(), OrderStatus.IN_DELIVERY);
        if (optionalOrder.isEmpty()) {
            return;
        }
        var order = optionalOrder.get();

        if (order.messageLocationId() == null) {
            var messageId = foodDeliveryBotSender.sendInitialLocation(order.chatId(), location.latitude(), location.longitude());
            orderRepository.updateMessageLocationId(order.id(), Long.valueOf(messageId));
        } else {
            foodDeliveryBotSender.updateLocation(order.messageLocationId(), order.chatId(), location.latitude(), location.longitude());
        }
    }
}
