package com.djulb.ui.sample;

import com.djulb.engine.ZoneService;
import com.djulb.ui.model.GpsUi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GetViewportObjectsUseCase {
    private final ZoneService zoneService;
    @Qualifier("mongoTaxiDb")
    private final MongoTemplate mongoTaxiDb;
    @Qualifier("mongoPassangerDb")
    private final MongoTemplate mongoPassangerDb;
    private final RedisTemplate redisTemplate;

    @PostMapping("/viewport/objects-by-id")
    public Iterable<GpsUi> getViewportObjects(@RequestBody ViewportObjectsGetByIds ids) {
        return new ArrayList<>();
//        System.out.println(ids);
//        if (ids.getIds().size() == 0) {
//            return new ArrayList<>();
//        }
//
//        return redisGpsRepository.findAllById(ids.getIds());
    }
}
