package com.djulb.ui.placeholders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GetSamleUsecase {

    @Autowired
    SampleService sampleService;

    @GetMapping("/api/sample")
    public List<Double> getSample () throws IOException {
        List<Double> coordinate = new ArrayList<>();
        coordinate.add(sampleService.getCurrent().getLat());
        coordinate.add(sampleService.getCurrent().getLng());
        return coordinate;
    }
}
