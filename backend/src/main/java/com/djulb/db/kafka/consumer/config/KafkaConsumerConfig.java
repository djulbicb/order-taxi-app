package com.djulb.db.kafka.consumer.config;

import com.djulb.db.kafka.KafkaCommon;
import com.djulb.db.kafka.model.NotificationK;
import com.djulb.db.kafka.model.PassangerKGps;
import com.djulb.db.kafka.model.TaxiKGps;
import com.djulb.publishers.contracts.model.KMContract;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${service.kafka.url}")
    private String serviceKafkaUrl;
    @Bean
    public ConsumerFactory<String, TaxiKGps> consumerFactoryTaxi() {
        JsonDeserializer<TaxiKGps> deserializer = new JsonDeserializer<>(TaxiKGps.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serviceKafkaUrl);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaxiKGps> kafkaListenerContainerFactoryTaxiGps() {
        ConcurrentKafkaListenerContainerFactory<String, TaxiKGps> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryTaxi());
        // for batch processing
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PassangerKGps> consumerFactoryPassanger() {
        JsonDeserializer<PassangerKGps> deserializer = new JsonDeserializer<>(PassangerKGps.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serviceKafkaUrl);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1000");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PassangerKGps> kafkaListenerContainerFactoryPassangerGps() {
        ConcurrentKafkaListenerContainerFactory<String, PassangerKGps> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryPassanger());
        // for batch processing
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, KMContract> consumerFactoryContract() {
        JsonDeserializer<KMContract> deserializer = new JsonDeserializer<>(KMContract.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serviceKafkaUrl);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "20");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KMContract> kafkaListenerContainerFactoryContract() {
        ConcurrentKafkaListenerContainerFactory<String, KMContract> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryContract());
        // for batch processing
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, NotificationK> consumerFactoryNotifications() {
        JsonDeserializer<NotificationK> deserializer = new JsonDeserializer<>(NotificationK.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serviceKafkaUrl);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "20");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationK> kafkaListenerContainerFactoryNotifications() {
        ConcurrentKafkaListenerContainerFactory<String, NotificationK> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryNotifications());
        // for batch processing
        factory.setBatchListener(true);
        return factory;
    }
}