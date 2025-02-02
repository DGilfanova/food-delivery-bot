package itis.kpfu.ru.deliveryservice.db.repository;

import itis.kpfu.ru.deliveryservice.db.model.Dish;

import java.util.List;

public interface DishRepository {
    List<Dish> getAllByMenuId(Long menuId);
}
