package com.djulb.ui.placeholders;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GetPlaceholdersUsecase {

    @GetMapping("/api/placeholders/matrix")
    public Collection<String[]> getPlaceholdersMatrix () throws IOException {
        String filename = "/home/sss/Documents/dev/workspace/order-taxi-app/src/main/resources/matrix/matrix_15x15.csv";
        return removeDuplicates(Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8));
    }

    @GetMapping("/api/placeholders/nearest")
    public Collection<String[]> getPlaceholdersNearest () throws IOException {
        String filename = "/home/sss/Documents/dev/workspace/order-taxi-app/src/main/resources/matrix/matrix_nearest_15x15.csv";
        return removeDuplicates(Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8));
    }

    private Collection<String[]> removeDuplicates(List<String> readAllLines) {
        Map<String, String[]> map = new HashMap<>();
        for (String line : readAllLines) {
            map.put(line, line.split(","));
        }
        return map.values();
    }
}

