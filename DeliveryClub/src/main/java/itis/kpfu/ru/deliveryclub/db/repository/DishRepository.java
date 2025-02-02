package itis.kpfu.ru.deliveryclub.db.repository;

import itis.kpfu.ru.deliveryclub.db.model.Dish;

import java.util.List;

public interface DishRepository {
    List<Dish> getAllByMenuId(Long menuId);
}
