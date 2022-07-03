package com.djulb.db.kafka.producer.config;

import com.djulb.db.kafka.KafkaCommon;
import com.djulb.way.elements.PassangerKGps;
import com.djulb.way.elements.TaxiKGps;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
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

    @Bean
    public ProducerFactory<String, TaxiKGps> producerFactoryTaxiGps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaCommon.BOOTSTRAP_SERVER);
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
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaCommon.BOOTSTRAP_SERVER);
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
}