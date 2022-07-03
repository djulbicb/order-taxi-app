package com.djulb.db.kafka.consumer;

import com.djulb.db.elastic.dto.EGps;
import com.djulb.db.elastic.ElasticSearchRepository;
import com.djulb.db.kafka.KafkaCommon;
//import com.djulb.db.redis.RedisTaxiRepository;

import com.djulb.way.elements.TaxiKGps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.djulb.db.elastic.ElasticConvertor.objToElastic;
import static com.djulb.way.elements.GpsConvertor.toRedisGps;

@Component
@RequiredArgsConstructor
@Slf4j
public class KConsumerTaxiGps {
    @Qualifier("mongoTaxiDb")
    private final MongoTemplate mongoTaxiDb;
    private final ElasticSearchRepository elasticSearchRepository;

    @KafkaListener(topics = KafkaCommon.TOPIC_GPS_TAXI, groupId = "taxiListener", containerFactory = "kafkaListenerContainerFactoryTaxiGps")
    //  public void listenGroupFoo(ConsumerRecord<String, TaxiGps> message) {
    public void listenGroupFoo(List<TaxiKGps> messages) {
        List<EGps> gpss = new ArrayList<>();
        for (TaxiKGps value : messages) {
//            mongoTaxiDb.save(value, ZoneService.getZone(value.getCoordinate()));
            gpss.add(objToElastic(value));

        }
        elasticSearchRepository.saveAll(gpss);

//        redisGpsRepository.save(toRedisGps(value));
//        studentRepository.save(RedisStudent.builder().id(value.getId()).name(value.getCoordinate().formatted()).gender(RedisStudent.Gender.MALE).build());
//        System.out.println("Received Taxi in group foo: " + message.value());
    }
}
