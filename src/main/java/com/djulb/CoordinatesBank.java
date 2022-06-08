package com.djulb;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CoordinatesBank {

    @GetMapping("/coordinates")
    public HashSet<String> coordinates () throws IOException {
        String filename = "fileName.csv";
        List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
        return new HashSet<String>(lines);
    }
}
