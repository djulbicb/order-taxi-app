package com.djulb.db.kafka.producer;

import com.djulb.db.kafka.KafkaCommon;
import com.djulb.db.kafka.model.NotificationK;
import com.djulb.db.kafka.model.PassangerKGps;
import com.djulb.db.kafka.model.TaxiKGps;
import com.djulb.publishers.contracts.model.KMContract;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

//    private final ObjectMapper objectMapper;

    @Value("${service.kafka.url}")
    private String serviceKafkaUrl;

    @Bean
    public ProducerFactory<String, TaxiKGps> producerFactoryTaxiGps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serviceKafkaUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, "20");
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(50*1024)); // 30kb
        configProps.put(ProducerConfig.ACKS_CONFIG, Integer.toString(0));

        return new DefaultKafkaProducerFactory<>(configProps);
        //return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), new JsonSerializer<>(objectMapper));
    }



    @Bean
    public KafkaTemplate<String, TaxiKGps> kafkaTemplateTaxiGps() {
        return new KafkaTemplate<>(producerFactoryTaxiGps());
    }
    @Bean
    public ProducerFactory<String, PassangerKGps> producerFactoryPassangerGps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serviceKafkaUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, "20");
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(50*1024)); // 30kb
        configProps.put(ProducerConfig.ACKS_CONFIG, Integer.toString(0));
        return new DefaultKafkaProducerFactory<>(configProps);
//        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), new JsonSerializer<>(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, PassangerKGps> kafkaTemplatePassangerGps() {
        return new KafkaTemplate<>(producerFactoryPassangerGps());
    }

    @Bean
    public ProducerFactory<String, KMContract> producerFactoryContract() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serviceKafkaUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, "20");
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(50*1024)); // 30kb
        configProps.put(ProducerConfig.ACKS_CONFIG, Integer.toString(0));
        return new DefaultKafkaProducerFactory<>(configProps);
//        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), new JsonSerializer<>(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, KMContract> kafkaTemplateContract() {
        return new KafkaTemplate<>(producerFactoryContract());
    }

    @Bean
    public ProducerFactory<String, NotificationK> producerFactoryNotifications() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serviceKafkaUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, "20");
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(50*1024)); // 30kb
        configProps.put(ProducerConfig.ACKS_CONFIG, Integer.toString(0));
        return new DefaultKafkaProducerFactory<>(configProps);
//        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), new JsonSerializer<>(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, NotificationK> kafkaTemplateNotifications() {
        return new KafkaTemplate<>(producerFactoryNotifications());
    }
}