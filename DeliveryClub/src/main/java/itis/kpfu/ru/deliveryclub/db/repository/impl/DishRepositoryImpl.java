package itis.kpfu.ru.deliveryclub.db.repository.impl;

import itis.kpfu.ru.deliveryclub.db.model.Dish;
import itis.kpfu.ru.deliveryclub.db.repository.DishRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DishRepositoryImpl implements DishRepository {

    private static final String SELECT_ALL_BY_RESTAURANT_ID_SQL = """
        SELECT *
        FROM dish
        where menu_id = :menuId;;
        """;

    private static final RowMapper<Dish> MAPPER = (rs, rowNum) ->
        Dish.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .build();

    private final NamedParameterJdbcOperations jdbc;

    public DishRepositoryImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Dish> getAllByMenuId(Long menuId) {
        return jdbc.query(
            SELECT_ALL_BY_RESTAURANT_ID_SQL,
            new MapSqlParameterSource().addValue("menuId", menuId),
            MAPPER
        );
    }
}
