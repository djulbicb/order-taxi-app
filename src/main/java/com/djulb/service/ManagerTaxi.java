package com.djulb.service;

import com.djulb.AppSettings;
import com.djulb.utils.ZoneService;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.Taxi;
import com.djulb.way.elements.TaxiGps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
@RequiredArgsConstructor
@Slf4j
public class ManagerTaxi implements ApplicationRunner {
    private final ZoneService zoneService;
    private final KafkaTemplate<String, TaxiGps> kafkaTemplate;
    private final ConcurrentHashMap<String, Taxi> carsByIdMap = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Coordinate coordinate = Coordinate.builder().lat(52.5200).lng(13.4050).build();
        String id = "test";
        addFakeCar(createFakeCar(id, coordinate));

        populateList();
    }

    private Taxi createFakeCar() {
        Coordinate randomCoordinate = zoneService.getRandomCoordinate();
        return createFakeCar(UUID.randomUUID().toString(), randomCoordinate);
    }

    private Taxi createFakeCar(String id, Coordinate coordinate) {
        Taxi car = Taxi.builder()
                .id(UUID.randomUUID().toString())
                .status(Taxi.Status.IDLE)
                .currentRoutePath(Optional.empty())
                .currentPosition(coordinate).build();
        return car;
    }

    private void addFakeCar(Taxi car) {
        carsByIdMap.put(car.getId().toString(), car);
    }

    public Optional<Taxi> getCarById(String id){
        return Optional.of(carsByIdMap.get(id));
    }

    @Scheduled(fixedDelay=5000)
    private void populateList() {
        int currentTaxiCount = carsByIdMap.values().size();
        boolean shouldAddMoreCars = AppSettings.MINIMUM_CARS > currentTaxiCount;
        if (!shouldAddMoreCars) { return; }

        int countOfTaxiToAdd = AppSettings.MINIMUM_CARS - currentTaxiCount;

        for (int i = 0; i < countOfTaxiToAdd; i++) {
            addFakeCar(createFakeCar());
        }
    }
    @Scheduled(fixedDelay=500)
    private void updateGps() {
        for (Map.Entry<String, Taxi> entry : carsByIdMap.entrySet()) {
            String zone = entry.getKey();
            Taxi car = entry.getValue();

            // ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_GPS_TAXI, car.getUuid().toString(), car.getCurrentPosition().formatted());
            //mongoTemplate.save(fakePerson, fakePerson.getZone());
            System.out.println("sending");
            kafkaTemplate.send(TOPIC_GPS_TAXI, car.getId().toString(), toGps(car));
        }
        kafkaTemplate.flush();
        // force send - sync. Cause if it shuts down, it may not be sent
//        kafkaProducer.flush(); // close also does flush
//        kafkaProducer.close();
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
