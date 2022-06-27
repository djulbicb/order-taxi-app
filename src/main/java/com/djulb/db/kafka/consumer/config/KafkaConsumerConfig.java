package com.djulb.db.kafka.consumer.config;

import com.djulb.db.kafka.KafkaCommon;
import com.djulb.way.elements.PassangerGps;
import com.djulb.way.elements.TaxiGps;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, TaxiGps> consumerFactoryTaxi() {
        JsonDeserializer<TaxiGps> deserializer = new JsonDeserializer<>(TaxiGps.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaCommon.BOOTSTRAP_SERVER);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1000");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaxiGps> kafkaListenerContainerFactoryTaxiGps() {
        ConcurrentKafkaListenerContainerFactory<String, TaxiGps> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryTaxi());
        // for batch processing
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PassangerGps> consumerFactoryPassanger() {
        JsonDeserializer<PassangerGps> deserializer = new JsonDeserializer<>(PassangerGps.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaCommon.BOOTSTRAP_SERVER);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1000");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PassangerGps> kafkaListenerContainerFactoryPassangerGps() {
        ConcurrentKafkaListenerContainerFactory<String, PassangerGps> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryPassanger());
        // for batch processing
        factory.setBatchListener(true);
        return factory;
    }
}