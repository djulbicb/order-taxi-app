package com.djulb.ui.placeholders;

import com.djulb.engine.EngineManager;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.Passanger;
import com.djulb.way.elements.Taxi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class CreateObjectFromPlaceholder {

    private final EngineManager engineManager;

    @PostMapping("/api/placeholders/taxi")
    public void createTaxiFromPlaceholder (@RequestBody Coordinate coordinate) throws IOException {
        System.out.println("Create taxi " + coordinate);


        Taxi fakeCar = engineManager.createFakeCar(coordinate);
        engineManager.addFakeCar(fakeCar);

    }

    @PostMapping("/api/placeholders/passanger")
    public void createPassangerFromPlaceholder (@RequestBody Coordinate coordinate) throws IOException {
        System.out.println("Create passanger " + coordinate);

        Passanger fakePassanger = engineManager.createFakePassanger(coordinate);
        engineManager.addFakePassanger(fakePassanger);
    }
}
