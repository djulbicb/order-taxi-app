package com.djulb.ui.placeholders;

import com.djulb.engine.EngineManager;
import com.djulb.common.coord.Coordinate;
import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class CreateObjectFromPlaceholder {

    private final EngineManager engineManager;

    @PostMapping("/api/placeholders/taxi")
    public void createTaxiFromPlaceholder (@RequestBody Coordinate coordinate) throws IOException {
        System.out.println("Create taxi " + coordinate);


        Taxi fakeCar = engineManager.createCar(coordinate);
        engineManager.addToRegisterCar(fakeCar);
    }

    @PostMapping("/api/placeholders/passanger")
    public void createPassangerFromPlaceholder (@RequestBody Coordinate coordinate) throws IOException {
        System.out.println("Create passanger " + coordinate);

        Passanger fakePassanger = engineManager.createPassanger(coordinate);
        engineManager.addToRegisterPassanger(fakePassanger);
    }
}
