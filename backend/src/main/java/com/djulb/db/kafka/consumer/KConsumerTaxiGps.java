package com.djulb.db.kafka.consumer;

import com.djulb.db.elastic.dto.EGps;
import com.djulb.db.elastic.ElasticSearchRepository;
import com.djulb.db.kafka.KafkaCommon;
//import com.djulb.db.redis.RedisTaxiRepository;

import com.djulb.db.kafka.model.TaxiKGps;
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
public class KConsumerTaxiGps {
    private final ElasticSearchRepository elasticSearchRepository;

    @KafkaListener(topics = KafkaCommon.TOPIC_GPS_TAXI, groupId = "taxiListener", containerFactory = "kafkaListenerContainerFactoryTaxiGps")
    public void listenGroupFoo(List<TaxiKGps> messages) {
        List<EGps> gpss = new ArrayList<>();
        for (TaxiKGps value : messages) {
            gpss.add(objToElastic(value));

        }
        elasticSearchRepository.saveAll(gpss);
    }
}
