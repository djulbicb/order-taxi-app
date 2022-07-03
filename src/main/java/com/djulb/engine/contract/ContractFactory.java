package com.djulb.engine.contract;

import com.djulb.db.elastic.FoodPOIRepository;
import com.djulb.db.elastic.FoodPOIRepositoryCustomImpl;
import com.djulb.engine.ManagerPassanger;
import com.djulb.engine.ManagerTaxi;
import com.djulb.way.elements.redis.RedisNotificationService;
import com.djulb.osrm.OsrmBackendApi;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
