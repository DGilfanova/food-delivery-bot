package itis.kpfu.ru.deliveryservice.job.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderReport {
    private Long orderId;
    private BigDecimal totalPrice;
    private BigDecimal discountPrice;
    private String status;
    private LocalDateTime createdAt;
    private String dish;
    private BigDecimal dishPrice;
    private Long dishQuantity;
    private String restaurantName;
    private String courierName;
    private String courierLastName;
}
