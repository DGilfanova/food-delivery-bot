package itis.kpfu.ru.deliveryservice.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

// Dummy location generator
public class LocationUtils {

    private static Double LONGITUDE = 55.795287;
    private static Double LATITUDE = 49.124148;

    public static Pair<Double, Double> getCurrentLocation() {
        LATITUDE += 0.001;
        LONGITUDE += 0.001;
        return new ImmutablePair<>(LATITUDE, LONGITUDE);
    }
}
