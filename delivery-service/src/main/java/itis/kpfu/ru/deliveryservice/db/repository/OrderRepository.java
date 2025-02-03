package itis.kpfu.ru.deliveryservice.db.repository;

import itis.kpfu.ru.deliveryservice.db.enums.OrderStatus;
import itis.kpfu.ru.deliveryservice.db.model.Order;

import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findByOrderId(Long id);

    Optional<Order> findByCourierIdAndStatus(Long courierId, OrderStatus status);

    void updateStatusAndCourierId(Long orderId, OrderStatus status, Long courierId);

    void updateMessageLocationId(Long orderId, Long messageLocationId);

    void create(Order order);
}