package itis.kpfu.ru.deliveryservice.db.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    DRAFT("Черновик"),
    FORMED_AND_PAID("Сформирован и оплачен"),
    RESTAURANT_APPROVED("Одобрен рестораном"),
    IN_COOKING("В процессе приготовления"),
    RESTAURANT_FINISHED("Приготовлен"),
    IN_DELIVERY("В пути"),
    DELIVERED("Доставлен");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}