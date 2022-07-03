package com.djulb.db.kafka.consumer;

import com.djulb.db.elastic.ElasticGps;
import com.djulb.db.elastic.FoodPOIRepository;
import com.djulb.db.kafka.KafkaCommon;
//import com.djulb.db.redis.RedisTaxiRepository;

import com.djulb.way.elements.TaxiGps;
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
public class KConsumerTaxiGps {
    @Qualifier("mongoTaxiDb")
    private final MongoTemplate mongoTaxiDb;
    private final FoodPOIRepository foodPOIRepository;

    @KafkaListener(topics = KafkaCommon.TOPIC_GPS_TAXI, groupId = "taxiListener", containerFactory = "kafkaListenerContainerFactoryTaxiGps")
    //  public void listenGroupFoo(ConsumerRecord<String, TaxiGps> message) {
    public void listenGroupFoo(List<TaxiGps> messages) {
        List<ElasticGps> gpss = new ArrayList<>();
        for (TaxiGps value : messages) {
//            mongoTaxiDb.save(value, ZoneService.getZone(value.getCoordinate()));
            ElasticGps gps = ElasticGps.builder()
                    .id(value.getId())
                    .status(value.getStatus())
                    .type(ElasticGps.Type.TAXI)
                    .location(new GeoPoint(value.getCoordinate().getLat(), value.getCoordinate().getLng()))
                    .build();
            gpss.add(gps);

        }
        foodPOIRepository.saveAll(gpss);

//        redisGpsRepository.save(toRedisGps(value));
//        studentRepository.save(RedisStudent.builder().id(value.getId()).name(value.getCoordinate().formatted()).gender(RedisStudent.Gender.MALE).build());
//        System.out.println("Received Taxi in group foo: " + message.value());
    }
}
