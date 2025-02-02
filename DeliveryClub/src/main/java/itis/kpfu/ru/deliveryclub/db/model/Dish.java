package itis.kpfu.ru.deliveryclub.db.model;

import lombok.Builder;

@Builder
public record Dish(
    Long id,
    String name
) {
}
