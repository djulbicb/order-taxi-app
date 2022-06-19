package com.djulb.db.kafka.consumer;

import com.djulb.db.kafka.KafkaCommon;
import com.djulb.way.elements.PassangerGps;
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
public class KConsumerPassengerGps {
    @Qualifier("mongoPassangerDb")
    private final MongoTemplate mongoPassangerDb;

    @KafkaListener(topics = KafkaCommon.TOPIC_GPS_PASSENGER, groupId = "passangerListener", containerFactory = "kafkaListenerContainerFactoryPassangerGps")
    public void listenGroupFoo(ConsumerRecord<String, PassangerGps> message) {
        PassangerGps value = message.value();
        mongoPassangerDb.save(value);
        System.out.println("Received Taxi in group foo: " + value);
    }
}
