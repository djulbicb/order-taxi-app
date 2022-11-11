package com.djulb.db.kafka.consumer;

import com.djulb.db.elastic.dto.EGps;
import com.djulb.db.elastic.ElasticSearchRepository;
import com.djulb.db.kafka.KafkaCommon;

//import com.djulb.db.redis.RedisPassangerRepository;
import com.djulb.engine.ZoneService;
import com.djulb.db.kafka.model.PassangerKGps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.djulb.db.elastic.ElasticConvertor.objToElastic;
import static com.djulb.common.objects.GpsConvertor.convertKafkaGpsUi;

@Component
@RequiredArgsConstructor
@Slf4j
public class KConsumerPassengerGps {
    private final ElasticSearchRepository elasticSearchRepository;

    @KafkaListener(topics = KafkaCommon.TOPIC_GPS_PASSENGER, groupId = "passangerListener", containerFactory = "kafkaListenerContainerFactoryPassangerGps")
    public void listenGroupFoo(List<PassangerKGps> messages) {
        List<EGps> gpss = new ArrayList<>();
        for (PassangerKGps value : messages) {

            EGps build = objToElastic(value);
            gpss.add(build);
        }


        elasticSearchRepository.saveAll(gpss);
//        System.out.println("Received Passanger in group foo: " + value);
    }


}