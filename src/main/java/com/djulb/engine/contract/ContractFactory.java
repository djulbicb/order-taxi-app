package com.djulb.engine.contract;

import com.djulb.common.objects.Passanger;
import com.djulb.db.elastic.ElasticSearchRepository;
import com.djulb.db.elastic.ElasticSearchRepositoryCustomImpl;
import com.djulb.engine.EngineManager;
import com.djulb.engine.contract.steps.RNotificationService;
import com.djulb.engine.contract.steps._0HoldStep;
import com.djulb.engine.contract.steps._1OrderTaxiStep;
import com.djulb.osrm.OsrmBackendApi;
import com.djulb.publishers.contracts.ContractServiceMRepository;
import lombok.Getter;

import java.time.Duration;

@Getter
public class ContractFactory {
    private final EngineManager engineManager;
    private final OsrmBackendApi osrmBackendApi;
    private final ElasticSearchRepositoryCustomImpl foodPOIRepositoryCustom;
    private final ElasticSearchRepository elasticSearchRepository;
    private final RNotificationService notificationService;
    private final ContractServiceMRepository contractServiceMRepository;

    public ContractFactory(EngineManager engineManager, OsrmBackendApi osrmBackendApi, RNotificationService notificationService, ElasticSearchRepositoryCustomImpl foodPOIRepositoryCustom, ElasticSearchRepository elasticSearchRepository, ContractServiceMRepository contractServiceMRepository) {
        this.engineManager = engineManager;
        this.osrmBackendApi = osrmBackendApi;
        this.notificationService = notificationService;
        this.foodPOIRepositoryCustom = foodPOIRepositoryCustom;
        this.elasticSearchRepository = elasticSearchRepository;
        this.contractServiceMRepository = contractServiceMRepository;
    }

    public void holdPassangerAndOrder(String contractId, Passanger passanger) {
        _0HoldStep holdStep = new _0HoldStep(this, contractId, passanger, Duration.ofSeconds(10));
        _1OrderTaxiStep step = new _1OrderTaxiStep(this, contractId, passanger);
    }

    //    public _0HoldStep passangerIdle(Passanger passanger, Duration duration) {
//        return new _0HoldStep(osrmBackendApi, redisNotificationService, passanger, duration);
//    }
//
//    public _1OrderTaxiStep orderTaxi(Passanger passanger) {
//        Taxi taxi = managerTaxi.getCarById("T-00001").get();
//
//        Coordinate start = Coordinate.builder().lng(taxi.getCurrentPosition().getLng()).lat(taxi.getCurrentPosition().getLat()).build();
//        Coordinate end = Coordinate.builder().lng(passanger.getCurrentPosition().getLng()).lat(passanger.getCurrentPosition().getLat()).build();
//
//        RoutePath routePath = osrmBackendApi.getRoute(start, end);
//
//        return new _1OrderTaxiStep(osrmBackendApi, passanger, taxi);
//    }
}
