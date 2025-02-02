package itis.kpfu.ru.deliveryservice.db.model;

import lombok.Builder;

@Builder
public record Dish(
    Long id,
    String name
) {
}
