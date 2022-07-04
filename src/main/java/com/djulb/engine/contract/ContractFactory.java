package com.djulb.engine.contract;

import com.djulb.common.objects.Passanger;
import com.djulb.db.elastic.ElasticSearchRepository;
import com.djulb.db.elastic.ElasticSearchRepositoryCustomImpl;
import com.djulb.engine.EngineManager;
import com.djulb.engine.contract.steps._0HoldStep;
import com.djulb.engine.contract.steps._1OrderTaxiStep;
import com.djulb.osrm.OsrmBackendApi;
import com.djulb.messages.redis.RedisNotificationService;
import lombok.Getter;

import java.time.Duration;

@Getter
public class ContractFactory {
    private final EngineManager engineManager;
    private final OsrmBackendApi osrmBackendApi;
    private final RedisNotificationService redisNotificationService;
    private final ElasticSearchRepositoryCustomImpl foodPOIRepositoryCustom;
    private final ElasticSearchRepository elasticSearchRepository;

    public ContractFactory(EngineManager engineManager, OsrmBackendApi osrmBackendApi, RedisNotificationService redisNotificationService, ElasticSearchRepositoryCustomImpl foodPOIRepositoryCustom, ElasticSearchRepository elasticSearchRepository) {
        this.engineManager = engineManager;
        this.osrmBackendApi = osrmBackendApi;
        this.redisNotificationService = redisNotificationService;
        this.foodPOIRepositoryCustom = foodPOIRepositoryCustom;
        this.elasticSearchRepository = elasticSearchRepository;
    }

    public void holdPassangerAndOrder(Passanger passanger) {
        _0HoldStep holdStep = new _0HoldStep(this, passanger, Duration.ofSeconds(10));
        _1OrderTaxiStep step = new _1OrderTaxiStep(this, passanger);
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
