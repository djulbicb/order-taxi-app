package com.djulb.service;

import com.djulb.utils.ZoneService;
import com.djulb.way.PathCalculator;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.bojan.RoutePath;
import com.djulb.way.elements.FakeCar;
import com.djulb.way.osrm.OsrmBackendApi;
import com.djulb.way.osrm.model.Step;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@EnableScheduling
public class FakeCarManager {
    private final ZoneService zoneService;
    private final OsrmBackendApi osrmBackendApi;
    private final HashMap<String, FakeCar> carsMap = new HashMap<>();

    public FakeCarManager(ZoneService zoneService, OsrmBackendApi osrmBackendApi) {
        this.zoneService = zoneService;
        this.osrmBackendApi = osrmBackendApi;
        Coordinate coordinate = Coordinate.builder().lat(52.5200).lng(13.4050).build();

        String id = "id";
        FakeCar car = createFakeCar(id, coordinate);
        carsMap.put(id, car);
    }

    private FakeCar createFakeCar(String id, Coordinate coordinate) {
        FakeCar car = FakeCar.builder()
                .status(FakeCar.Status.IDLE)
                .currentRoutePath(Optional.empty())
                .currentPosition(coordinate).build();
        return car;
    }

    public Optional<FakeCar> getCarById(String id){
        return Optional.of(carsMap.get(id));
    }

    double dist = 50;
    @Scheduled(fixedDelay=500)
    public void test() {

        for (Map.Entry<String, FakeCar> entry : carsMap.entrySet()) {
            String id = entry.getKey();
            FakeCar car = entry.getValue();

            // Assign new path if idle and no path
            if (car.isIdle() && !car.hasPath()) {
                Optional<Coordinate> coordinateInSameZone = zoneService.getCoordinateInSameZone(car.getCurrentPosition());
//                Coordinate build = Coordinate.builder().lat(52.55).lng(13.40).build();
                if (coordinateInSameZone.isPresent()) {
                    RoutePath route = osrmBackendApi.getRoute(car.getCurrentPosition(),coordinateInSameZone.get());
                    car.addPath(route);
                }
            }

            car.move(dist);
            System.out.println("Tick " + car.getCurrentPosition().formatted());
            if (car.isRouteFinished()) {
                car.setCurrentRoutePath(Optional.empty());
            }
        }
    }

}
