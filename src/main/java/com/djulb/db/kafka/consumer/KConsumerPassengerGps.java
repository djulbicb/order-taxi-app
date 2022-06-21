package com.djulb.db.kafka.consumer;

import com.djulb.db.kafka.KafkaCommon;

//import com.djulb.db.redis.RedisPassangerRepository;
import com.djulb.db.redis.RedisGpsRepository;
import com.djulb.utils.ZoneService;
import com.djulb.way.elements.PassangerGps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.djulb.way.elements.GpsConvertor.toRedisGps;

@Component
@RequiredArgsConstructor
@Slf4j
public class KConsumerPassengerGps {
    @Qualifier("mongoPassangerDb")
    private final MongoTemplate mongoPassangerDb;
    private final RedisGpsRepository redisGpsRepository;
    @KafkaListener(topics = KafkaCommon.TOPIC_GPS_PASSENGER, groupId = "passangerListener", containerFactory = "kafkaListenerContainerFactoryPassangerGps")
    public void listenGroupFoo(ConsumerRecord<String, PassangerGps> message) {
        PassangerGps value = message.value();
        mongoPassangerDb.save(value, ZoneService.getZone(value.getCoordinate()));
        redisGpsRepository.save(toRedisGps(value));

//        System.out.println("Received Passanger in group foo: " + value);
    }
}
