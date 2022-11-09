package com.djulb.db.kafka.consumer;

import com.djulb.db.kafka.KafkaCommon;
import com.djulb.publishers.contracts.ContractServiceMRepository;
import com.djulb.publishers.contracts.model.KMContract;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.djulb.db.elastic.ElasticConvertor.objToElastic;

@Component
@RequiredArgsConstructor
@Slf4j
public class KConsumerContract {
    @Qualifier("mongoTaxiDb")
    private final MongoTemplate mongoTaxiDb;
    private final ContractServiceMRepository contractServiceMRepository;

    @KafkaListener(topics = KafkaCommon.TOPIC_CONTRACT, groupId = "contractListener", containerFactory = "kafkaListenerContainerFactoryContract")
    public void listenGroupFoo(List<KMContract> contractMS) {
        // System.out.println(contractMS);

        for (KMContract contractM : contractMS) {
            contractServiceMRepository.updateFullContract(contractM);
        }


    }
}
