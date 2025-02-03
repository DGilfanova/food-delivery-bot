package itis.kpfu.ru.deliveryservice.job.location;

import itis.kpfu.ru.deliveryservice.config.properties.KafkaProperties;
import itis.kpfu.ru.deliveryservice.db.repository.CourierRepository;
import itis.kpfu.ru.deliveryservice.event.Location;
import itis.kpfu.ru.deliveryservice.util.LocationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourierLocationDummySender {

    private final KafkaTemplate<String, Location> kafkaTemplate;
    private final KafkaProperties kafkaProperties;
    private final CourierRepository courierRepository;
    private Set<Long> courierIds;

    @Scheduled(fixedRate = 5000)
    public void sendLocationEventToKafka() {
        if (CollectionUtils.isEmpty(courierIds)) {
            courierIds = courierRepository.findAllIds();
        }

        courierIds.forEach(courierId -> {
            var locationPair = LocationUtils.getCurrentLocation();
            sendLocationEvent(new Location(courierId, locationPair.getLeft(), locationPair.getRight()));
        });

        log.info("Send location for couriers: %s".formatted(courierIds));
    }

    public void sendLocationEvent(Location location) {
        kafkaTemplate.send(kafkaProperties.sendCourierLocation().topic(), location);
    }
}
