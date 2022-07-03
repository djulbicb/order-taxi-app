package com.djulb.engine;

import com.djulb.OrderTaxiAppSettings;
import com.djulb.db.elastic.ElasticGps;
import com.djulb.db.elastic.FoodPOIRepository;
import com.djulb.engine.generator.TaxiIdGenerator;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.Taxi;
import com.djulb.way.elements.TaxiGps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.djulb.db.kafka.KafkaCommon.TOPIC_GPS_TAXI;
import static com.djulb.way.elements.GpsConvertor.toGps;

@Component
@EnableScheduling
@Slf4j
public class ManagerTaxi {
    private final ZoneService zoneService;
    private final KafkaTemplate<String, TaxiGps> kafkaTemplate;
    private final ConcurrentHashMap<String, Taxi> carsByIdMap = new ConcurrentHashMap<>();
    private final TaxiIdGenerator generator;

    private final FoodPOIRepository foodPOIRepository;

    public ManagerTaxi(ZoneService zoneService, KafkaTemplate<String, TaxiGps> kafkaTemplate, TaxiIdGenerator generator, FoodPOIRepository foodPOIRepository) {
        this.zoneService = zoneService;
        this.kafkaTemplate = kafkaTemplate;
        this.generator = generator;
        this.foodPOIRepository = foodPOIRepository;


////        Coordinate coordinate = Coordinate.builder().lat(52.5200).lng(13.4050).build();
////        String id = "test";
////        addFakeCar(createFakeCar(id, zoneService.getRandomCoordinateInZone(ZoneService.getZone(coordinate)).get()));
//
//        populateCarList();
    }




//    double dist = 50;
//    @Scheduled(fixedDelay=500)
//    public void test() {
//
//        for (Map.Entry<String, FakeCar> entry : carsMap.entrySet()) {
//            String id = entry.getKey();
//            FakeCar car = entry.getValue();
//
//            // Assign new path if idle and no path
//            if (car.isIdle() && !car.hasPath()) {
//                Optional<Coordinate> coordinateInSameZone = zoneService.getCoordinateInSameZone(car.getCurrentPosition());
////                Coordinate build = Coordinate.builder().lat(52.55).lng(13.40).build();
//                if (coordinateInSameZone.isPresent()) {
//                    RoutePath route = osrmBackendApi.getRoute(car.getCurrentPosition(),coordinateInSameZone.get());
//                    car.addPath(route);
//                }
//            }
//
//            car.move(dist);
//            // System.out.println("Tick " + car.getCurrentPosition().formatted());
//            if (car.isRouteFinished()) {
//                car.setCurrentRoutePath(Optional.empty());
//            }
//        }
//    }

}
