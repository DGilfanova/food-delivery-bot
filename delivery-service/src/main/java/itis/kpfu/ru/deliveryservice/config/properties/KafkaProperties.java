package itis.kpfu.ru.deliveryservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kafka")
public record KafkaProperties(
    SendCourierLocation sendCourierLocation,
    GetCourierLocation getCourierLocation
) {

    public record SendCourierLocation(
        String topic,
        String servers,
        String login,
        String password,
        String saslMechanism,
        String security,
        String securityProtocol
    ) {
    }

    public record GetCourierLocation(
        String topic,
        String servers,
        String login,
        String password,
        String saslMechanism,
        String security,
        String securityProtocol
    ) {
    }
}
