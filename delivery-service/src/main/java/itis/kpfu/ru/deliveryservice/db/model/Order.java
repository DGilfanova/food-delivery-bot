package itis.kpfu.ru.deliveryservice.db.model;

import itis.kpfu.ru.deliveryservice.db.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record Order(
    Long id,
    Long restaurantId,
    Long userId,
    Long courierId,
    Long chatId,
    Integer messageLocationId,
    BigDecimal totalPrice,
    OrderStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Set<Item> items
) {

    @Builder
    public record Item(
        Long id,
        Long dishId,
        Integer quantity,
        Double price
    ) {
    }
}
