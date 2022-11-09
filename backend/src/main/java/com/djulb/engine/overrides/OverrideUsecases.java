package com.djulb.engine.overrides;

import com.djulb.OrderTaxiAppSettings;
import com.djulb.common.coord.BBox;
import com.djulb.common.coord.Coordinate;
import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.db.elastic.ElasticSearchRepository;
import com.djulb.engine.EngineManager;
import com.djulb.engine.ZoneService;
import com.djulb.publishers.contracts.ContractServiceMRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class OverrideUsecases {

    private final ElasticSearchRepository elasticSearchRepository;
    private final ContractServiceMRepository contractServiceMRepository;
    private final EngineManager engineManager;
    private final ZoneService zoneService;

    @GetMapping("/api/overrides/delete-all")
    public void deleteAll() {
        engineManager.deleteAll();
        elasticSearchRepository.deleteAll();
        contractServiceMRepository.deleteAll();
        System.out.println("Delete all");
    }

    @GetMapping("/api/overrides/update-speed/{speed}")
    public void updateSpeed(@PathVariable int speed) {
        OrderTaxiAppSettings.UPDATE_SPEED = speed;
    }

    @GetMapping("/api/overrides/move-increment/{moveIncrement}")
    public void changeMoveIncrement(@PathVariable int moveIncrement) {
        OrderTaxiAppSettings.MOVE_INCREMENT = moveIncrement;
    }

    @GetMapping("/api/central/taxi/{numberOfTaxis}")
    public void addCentralTaxi(@PathVariable int numberOfTaxis) {
        Coordinate middlePoint = BBox.getBerlinBbox().getMiddlePoint();
        String centralZone = ZoneService.getZone(middlePoint);

        for (int i = 0; i < numberOfTaxis; i++) {
            Optional<Coordinate> coordinateInZone = zoneService.getRandomCoordinateInZone(centralZone);
            if (coordinateInZone.isPresent()) {
                Taxi car = engineManager.createCar(coordinateInZone.get());
                engineManager.addToRegisterCar(car);
            }
        }
    }

    @GetMapping("/api/central/passangers/{numberOfPassangers}")
    public void addCentralPassangers(@PathVariable int numberOfPassangers) {
        Coordinate middlePoint = BBox.getBerlinBbox().getMiddlePoint();
        String centralZone = ZoneService.getZone(middlePoint);

        for (int i = 0; i < numberOfPassangers; i++) {
            Optional<Coordinate> coordinateInZone = zoneService.getRandomCoordinateInZone(centralZone);
            if (coordinateInZone.isPresent()) {
                Passanger passanger = engineManager.createPassanger(coordinateInZone.get());
                engineManager.addToRegisterPassanger(passanger);
            }
        }
    }

    @GetMapping("/api/every-zone/taxi/{numberOfTaxis}")
    public void addEveryZoneTaxi(@PathVariable int numberOfTaxis) {
        for (Map.Entry<String, List<Coordinate>> entry : zoneService.getZoneCoordinatesMap().entrySet()) {
            for (int i = 0; i < numberOfTaxis; i++) {
                Optional<Coordinate> randomCoordinateInZone = zoneService.getRandomCoordinateInZone(entry.getKey());
                if (randomCoordinateInZone.isPresent()) {
                    Taxi car = engineManager.createCar(randomCoordinateInZone.get());
                    engineManager.addToRegisterCar(car);
                }
            }
        }
    }

    @GetMapping("/api/every-zone/passangers/{numberOfPassangers}")
    public void addEveryZonePassangers(@PathVariable int numberOfPassangers) {
        for (Map.Entry<String, List<Coordinate>> entry : zoneService.getZoneCoordinatesMap().entrySet()) {
            for (int i = 0; i < numberOfPassangers; i++) {
                Optional<Coordinate> randomCoordinateInZone = zoneService.getRandomCoordinateInZone(entry.getKey());
                if (randomCoordinateInZone.isPresent()) {
                    Passanger passanger = engineManager.createPassanger(randomCoordinateInZone.get());
                    engineManager.addToRegisterPassanger(passanger);
                }
            }
        }
    }

}
