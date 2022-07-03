package com.djulb.ui.placeholders;

import com.djulb.way.bojan.Coordinate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CreateObjectFromPlaceholder {

    @PostMapping("/api/placeholders/taxi")
    public void createTaxiFromPlaceholder (@RequestBody Coordinate coordinate) throws IOException {
        System.out.println("Create taxi " + coordinate);
    }

    @PostMapping("/api/placeholders/passanger")
    public void createPassangerFromPlaceholder (@RequestBody Coordinate coordinate) throws IOException {
        System.out.println("Create passanger " + coordinate);
    }
}
