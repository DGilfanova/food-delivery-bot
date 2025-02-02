package itis.kpfu.ru.deliveryservice.controller;

import itis.kpfu.ru.deliveryservice.db.repository.DishRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final DishRepository dishRepository;

    public TestController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping("/test")
    public void get() {
        System.out.println(dishRepository.getAllByMenuId(1L));
    }
}
