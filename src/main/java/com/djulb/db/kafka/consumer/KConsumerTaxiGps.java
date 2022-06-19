package com.djulb.db.kafka.consumer;

import com.djulb.db.kafka.KafkaCommon;
import com.djulb.utils.ZoneService;
import com.djulb.way.elements.TaxiGps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KConsumerTaxiGps {
    @Qualifier("mongoTaxiDb")
    private final MongoTemplate mongoTaxiDb;

    @KafkaListener(topics = KafkaCommon.TOPIC_GPS_TAXI, groupId = "taxiListener", containerFactory = "kafkaListenerContainerFactoryTaxiGps")
    public void listenGroupFoo(ConsumerRecord<String, TaxiGps> message) {
        TaxiGps value = message.value();
        mongoTaxiDb.save(value, "users");//ZoneService.getZone(value.getCoordinate()));
        System.out.println("Received Taxi in group foo: " + message.value());
    }
}
