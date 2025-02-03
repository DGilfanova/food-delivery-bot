package itis.kpfu.ru.deliveryservice.db.repository.impl;

import itis.kpfu.ru.deliveryservice.db.repository.CourierRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class CourierRepositoryImpl implements CourierRepository {

    private static final String SELECT_ALL_IDS_SQL = """
        SELECT id
        FROM courier;
        """;

    private final NamedParameterJdbcOperations jdbc;

    public CourierRepositoryImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Set<Long> findAllIds() {
        return new HashSet<>(
            jdbc.queryForList(
                SELECT_ALL_IDS_SQL,
                new MapSqlParameterSource(),
                Long.class
            )
        );
    }
}
