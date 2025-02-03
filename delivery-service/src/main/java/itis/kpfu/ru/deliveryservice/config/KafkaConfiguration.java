package itis.kpfu.ru.deliveryservice.config;

import itis.kpfu.ru.deliveryservice.config.properties.KafkaProperties;
import itis.kpfu.ru.deliveryservice.event.Location;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfiguration {

    private final KafkaProperties kafkaProperties;

    public KafkaConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ConsumerFactory<String, Location> consumerFactoryCourierLocation() {
        Map<String, Object> props = new HashMap<>();
        KafkaProperties.GetCourierLocation locationProperties = kafkaProperties.getCourierLocation();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, locationProperties.servers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-location-consumer-group");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, locationProperties.securityProtocol());
        props.put(SaslConfigs.SASL_MECHANISM, locationProperties.saslMechanism());
        props.put(SaslConfigs.SASL_JAAS_CONFIG,
            locationProperties.security() +
                " required username='" + locationProperties.login() + "' password='" +
                locationProperties.password() + "';");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
            new JsonDeserializer<>(Location.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Location> kafkaCourierLocationContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Location>();
        factory.setConsumerFactory(consumerFactoryCourierLocation());
        return factory;
    }

    @Bean
    public ProducerFactory<String, Location> courierLocationProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        KafkaProperties.SendCourierLocation locationProperties = kafkaProperties.sendCourierLocation();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, locationProperties.servers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, locationProperties.securityProtocol());
        props.put(SaslConfigs.SASL_MECHANISM, locationProperties.saslMechanism());
        props.put(SaslConfigs.SASL_JAAS_CONFIG, locationProperties.security() +
            " required username='" + locationProperties.login() + "' password='" +
            locationProperties.password() + "';");
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Location> kafkaCourierLocationTemplate() {
        return new KafkaTemplate<>(courierLocationProducerFactory());
    }
}
