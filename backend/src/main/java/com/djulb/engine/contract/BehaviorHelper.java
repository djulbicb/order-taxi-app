package com.djulb.engine.contract;

import com.djulb.db.elastic.ElasticSearchRepository;
import com.djulb.db.elastic.ElasticSearchRepositoryCustomImpl;
import com.djulb.db.kafka.model.NotificationK;
import com.djulb.db.kafka.model.PassangerKGps;
import com.djulb.db.kafka.model.TaxiKGps;
import com.djulb.db.redis.RTaxiStatusRepository;
import com.djulb.engine.EngineManager;
import com.djulb.osrm.OsrmBackendApi;
import com.djulb.publishers.contracts.ContractServiceMRepository;
import com.djulb.publishers.contracts.model.KMContract;
import lombok.Getter;
import org.springframework.kafka.core.KafkaTemplate;

@Getter
public class BehaviorHelper {
    private final OsrmBackendApi osrmBackendApi;
    private final ElasticSearchRepositoryCustomImpl elasticSearchRepositoryCustomImpl;
    private final ElasticSearchRepository elasticSearchRepository;
    private final KafkaTemplate<String, NotificationK> kafkaNotificationTemplate;
    private final RTaxiStatusRepository taxiStatusRepository;
    private final ContractServiceMRepository contractServiceMRepository;
    private final KafkaTemplate<String, PassangerKGps> kafkaPassangerTemplate;
    private final KafkaTemplate<String, TaxiKGps> kafkaTaxiTemplate;
    private final KafkaTemplate<String, KMContract> kafkaContractTemplate;
    private final EngineManager engineManager;
    public BehaviorHelper(EngineManager engineManager, OsrmBackendApi osrmBackendApi, KafkaTemplate<String, NotificationK>  kafkaNotificationTemplate, ElasticSearchRepositoryCustomImpl elasticSearchRepositoryCustomImpl, ElasticSearchRepository elasticSearchRepository, RTaxiStatusRepository taxiStatusRepository, ContractServiceMRepository contractServiceMRepository, KafkaTemplate<String, PassangerKGps> kafkaPassangerTemplate, KafkaTemplate<String, TaxiKGps> kafkaTaxiTemplate, KafkaTemplate<String, KMContract> kafkaContractTemplate) {
        this.osrmBackendApi = osrmBackendApi;
        this.kafkaNotificationTemplate = kafkaNotificationTemplate;
        this.elasticSearchRepositoryCustomImpl = elasticSearchRepositoryCustomImpl;
        this.elasticSearchRepository = elasticSearchRepository;
        this.taxiStatusRepository = taxiStatusRepository;
        this.contractServiceMRepository = contractServiceMRepository;
        this.kafkaPassangerTemplate = kafkaPassangerTemplate;
        this.kafkaTaxiTemplate = kafkaTaxiTemplate;
        this.kafkaContractTemplate = kafkaContractTemplate;
        this.engineManager = engineManager;
    }
}
