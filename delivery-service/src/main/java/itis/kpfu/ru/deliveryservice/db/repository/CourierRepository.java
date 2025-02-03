package itis.kpfu.ru.deliveryservice.db.repository;

import java.util.Set;

public interface CourierRepository {
    Set<Long> findAllIds();
}
