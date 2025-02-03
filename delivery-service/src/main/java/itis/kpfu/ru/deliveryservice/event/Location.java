package itis.kpfu.ru.deliveryservice.event;

import lombok.Builder;

@Builder
public record Location(
    Long courierId,
    Double latitude,
    Double longitude
) {
}
