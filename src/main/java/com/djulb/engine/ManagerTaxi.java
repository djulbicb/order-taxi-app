package com.djulb.engine;

import com.djulb.OrderTaxiAppSettings;
import com.djulb.db.elastic.ElasticGps;
import com.djulb.db.elastic.FoodPOIRepository;
import com.djulb.engine.generator.TaxiIdGenerator;
import com.djulb.utils.ZoneService;
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


//        Coordinate coordinate = Coordinate.builder().lat(52.5200).lng(13.4050).build();
//        String id = "test";
//        addFakeCar(createFakeCar(id, zoneService.getRandomCoordinateInZone(ZoneService.getZone(coordinate)).get()));

        populateList();
    }

    private Taxi createFakeCar() {
        Coordinate randomCoordinate = zoneService.getRandomCoordinate();
        return createFakeCar(generator.getNext(), randomCoordinate);
    }

    private Taxi createFakeCar(String id, Coordinate coordinate) {
        Taxi car = Taxi.builder()
                .id(id)
                .status(Taxi.Status.IDLE)
                .currentRoutePath(Optional.empty())
                .currentPosition(coordinate).build();

        foodPOIRepository.save(toElasticGps(car));
        return car;
    }

    private ElasticGps toElasticGps(Taxi car) {
        return ElasticGps.builder()
                .id(car.getId())
                .status(car.getStatus())
                .type(ElasticGps.Type.PASSANGER)
                .location(new GeoPoint(car.getCurrentPosition().getLat(), car.getCurrentPosition().getLng()))
                .build();
    }

    private void addFakeCar(Taxi car) {
        carsByIdMap.put(car.getId().toString(), car);
    }

    public Optional<Taxi> getCarById(String id){
        return Optional.of(carsByIdMap.get(id));
    }
    public ConcurrentHashMap<String, Taxi> getCars() {
        return carsByIdMap;
    }

//    @Scheduled(fixedDelay=5000)
    private void populateList() {
        int currentTaxiCount = carsByIdMap.values().size();
        boolean shouldAddMoreCars = OrderTaxiAppSettings.MINIMUM_CARS > currentTaxiCount;
        if (!shouldAddMoreCars) { return; }

        int countOfTaxiToAdd = OrderTaxiAppSettings.MINIMUM_CARS - currentTaxiCount;

        for (int i = 0; i < countOfTaxiToAdd; i++) {
            addFakeCar(createFakeCar());
        }
    }
    @Scheduled(fixedDelay=1000)
    private void updateGps() {
        for (Map.Entry<String, Taxi> entry : carsByIdMap.entrySet()) {
            String zone = entry.getKey();
            Taxi car = entry.getValue();

            // ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_GPS_TAXI, car.getUuid().toString(), car.getCurrentPosition().formatted());
            //mongoTemplate.save(fakePerson, fakePerson.getZone());
            kafkaTemplate.send(TOPIC_GPS_TAXI, car.getId().toString(), toGps(car));
        }
        kafkaTemplate.flush();
        // force send - sync. Cause if it shuts down, it may not be sent
//        kafkaProducer.flush(); // close also does flush
//        kafkaProducer.close();
    }

    public Optional<Taxi> get(List<String> taxiIds) {
        List<Taxi> taxis = new ArrayList<>();
        for (String id : taxiIds) {
            if (carsByIdMap.containsKey(id)) {
                taxis.add(carsByIdMap.get(id));
            }
        }
        if (taxis.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(taxis.get(0));
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
