package itis.kpfu.ru.deliveryservice.controller;

import itis.kpfu.ru.deliveryservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/v1/courier")
@RequiredArgsConstructor
public class CourierController {

    private final OrderService orderService;

    @PatchMapping("/{courierId}/order/{orderId}/accept")
    @ResponseStatus(HttpStatus.OK)
    public void acceptOrder(@PathVariable("courierId") Long courierId, @PathVariable("orderId") Long orderId) {
        orderService.acceptOrder(courierId, orderId);
    }

    @PatchMapping("/{courierId}/order/{orderId}/deliver")
    @ResponseStatus(HttpStatus.OK)
    public void deliverOrder(@PathVariable("courierId") Long courierId, @PathVariable("orderId") Long orderId) {
        orderService.deliverOrder(courierId, orderId);
    }
}
