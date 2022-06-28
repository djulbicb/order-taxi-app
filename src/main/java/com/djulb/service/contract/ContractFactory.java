package com.djulb.service.contract;

import com.djulb.db.elastic.FoodPOIRepository;
import com.djulb.db.elastic.FoodPOIRepositoryCustomImpl;
import com.djulb.service.ManagerPassanger;
import com.djulb.service.ManagerTaxi;
import com.djulb.service.contract.steps._0HoldStep;
import com.djulb.service.contract.steps._1OrderTaxiStep;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.bojan.RoutePath;
import com.djulb.way.elements.Passanger;
import com.djulb.way.elements.Taxi;
import com.djulb.way.elements.redis.RedisNotificationService;
import com.djulb.way.osrm.OsrmBackendApi;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Getter
@RequiredArgsConstructor
public class ContractFactory {
    private final ManagerPassanger managerPassanger;
    private final ManagerTaxi managerTaxi;
    private final OsrmBackendApi osrmBackendApi;
    private final RedisNotificationService redisNotificationService;
    private final FoodPOIRepositoryCustomImpl foodPOIRepositoryCustom;
    private final FoodPOIRepository foodPOIRepository;




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
