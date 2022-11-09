package com.djulb.ui.statistics;

import com.djulb.engine.EngineManagerStatistics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;

@RestController
public class GetEngineStatisticsUsecase {
    @GetMapping("/api/engine/statistics")
    public String getPlaceholdersMatrix () throws IllegalAccessException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LinkedHashMap<String, Object> statiscticsAsMap = EngineManagerStatistics.getAsMap();
        return gson.toJson(statiscticsAsMap);
    }
}
