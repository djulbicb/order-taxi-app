package com.djulb.db.kafka.consumer;

import com.djulb.db.elastic.ElasticGps;
import com.djulb.db.elastic.FoodPOIRepository;
import com.djulb.db.kafka.KafkaCommon;

//import com.djulb.db.redis.RedisPassangerRepository;
import com.djulb.engine.ZoneService;
import com.djulb.way.elements.PassangerGps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.djulb.way.elements.GpsConvertor.toRedisGps;

@Component
@RequiredArgsConstructor
@Slf4j
public class KConsumerPassengerGps {
    @Qualifier("mongoPassangerDb")
    private final MongoTemplate mongoPassangerDb;
    private final FoodPOIRepository foodPOIRepository;

    @KafkaListener(topics = KafkaCommon.TOPIC_GPS_PASSENGER, groupId = "passangerListener", containerFactory = "kafkaListenerContainerFactoryPassangerGps")
    public void listenGroupFoo(List<PassangerGps> messages) {
        List<ElasticGps> gpss = new ArrayList<>();
        for (PassangerGps value : messages) {

//        System.out.println("Passanger " + value.getId());
            mongoPassangerDb.save(value, ZoneService.getZone(value.getCoordinate()));
//        redisGpsRepository.save(toRedisGps(value));
            ElasticGps build = ElasticGps.builder()
                    .id(value.getId())
                    .status(value.getStatus())
                    .type(ElasticGps.Type.PASSANGER)
                    .location(new GeoPoint(value.getCoordinate().getLat(), value.getCoordinate().getLng()))
                    .build();
            gpss.add(build);
        }


        foodPOIRepository.saveAll(gpss);
//        System.out.println("Received Passanger in group foo: " + value);
    }
}
