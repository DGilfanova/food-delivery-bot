package itis.kpfu.ru.deliveryservice.db.repository.impl;

import itis.kpfu.ru.deliveryservice.db.enums.OrderStatus;
import itis.kpfu.ru.deliveryservice.db.model.Order;
import itis.kpfu.ru.deliveryservice.db.repository.OrderRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private static final String SELECT_BY_ID_SQL = """
        SELECT *
        FROM orders
        where id = :id;
        """;

    private static final String SELECT_BY_COURIER_ID_AND_STATUS_SQL = """
        SELECT *
        FROM orders
        WHERE courier_id = :courierId AND status = :status;
        """;

    private static final String UPDATE_COURIER_ID_AND_STATUS_BY_ID_SQL = """
        UPDATE orders
        SET courier_id = :courierId, status = :status
        WHERE id = :id;
        """;

    private static final String UPDATE_MESSAGE_LOCATION_ID_BY_ID_SQL = """
        UPDATE orders
        SET message_location_id = :messageLocationId
        WHERE id = :id;
        """;

    private static final String INSERT_SQL = """
        INSERT INTO orders (user_id, restaurant_id, total_price, status, courier_id, message_location_id, chat_id)
        VALUES (:userId, :restaurantId, :totalPrice, :status, :courierId, :messageLocationId, :chatId)
        RETURNING id;
        """;

    private static final String INSERT_ITEM_SQL = """
            INSERT INTO order_item (order_id, dish_id, quantity, price)
            VALUES (:orderId, :dishId, :quantity, :price);
        """;

    private static final RowMapper<Order> MAPPER = (rs, rowNum) ->
        Order.builder()
            .id(rs.getLong("id"))
            .restaurantId(rs.getObject("restaurant_id", Long.class))
            .chatId(rs.getObject("chat_id", Long.class))
            .messageLocationId(Optional.ofNullable(rs.getObject("message_location_id", Long.class))
                .map(Long::intValue)
                .orElse(null))
            .totalPrice(rs.getObject("total_price", BigDecimal.class))
            .courierId(rs.getObject("courier_id", Long.class))
            .status(OrderStatus.valueOf(rs.getString("status")))
            .createdAt(rs.getObject("created_at", LocalDateTime.class))
            .updatedAt(rs.getObject("updated_at", LocalDateTime.class))
            .build();

    private final NamedParameterJdbcOperations jdbc;

    public OrderRepositoryImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Order> findByOrderId(Long id) {
        return jdbc.query(
            SELECT_BY_ID_SQL,
            new MapSqlParameterSource().addValue("id", id),
            MAPPER
        ).stream().findFirst();
    }

    @Override
    public Optional<Order> findByCourierIdAndStatus(Long courierId, OrderStatus status) {
        return jdbc.query(
            SELECT_BY_COURIER_ID_AND_STATUS_SQL,
            new MapSqlParameterSource()
                .addValue("courierId", courierId)
                .addValue("status", status.name()),
            MAPPER
        ).stream().findFirst();
    }

    @Override
    public void updateStatusAndCourierId(Long orderId, OrderStatus status, Long courierId) {
        jdbc.update(
            UPDATE_COURIER_ID_AND_STATUS_BY_ID_SQL,
            new MapSqlParameterSource()
                .addValue("id", orderId)
                .addValue("courierId", courierId)
                .addValue("status", status.name())
        );
    }

    @Override
    public void updateMessageLocationId(Long orderId, Long messageLocationId) {
        jdbc.update(
            UPDATE_MESSAGE_LOCATION_ID_BY_ID_SQL,
            new MapSqlParameterSource()
                .addValue("id", orderId)
                .addValue("messageLocationId", messageLocationId)
        );
    }

    @Override
    public void create(Order order) {
        var id = jdbc.execute(
            INSERT_SQL,
            new MapSqlParameterSource()
                .addValue("userId", order.userId())
                .addValue("restaurantId", order.restaurantId())
                .addValue("status", order.status().name())
                .addValue("chatId", order.chatId())
                .addValue("courierId", order.courierId())
                .addValue("totalPrice", order.totalPrice())
                .addValue("messageLocationId", order.messageLocationId()),
            ps -> {
                ResultSet rs = ps.executeQuery();
                rs.next();
                return rs.getLong("id");
            });

        order.items().forEach(item ->
            jdbc.update(
                INSERT_ITEM_SQL,
                new MapSqlParameterSource()
                    .addValue("orderId", id)
                    .addValue("dishId", item.dishId())
                    .addValue("quantity", item.quantity())
                    .addValue("price", item.price())
            )
        );
    }
}