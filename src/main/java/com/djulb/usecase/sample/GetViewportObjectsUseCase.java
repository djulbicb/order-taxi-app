package com.djulb.usecase.sample;

import com.djulb.db.redis.RedisGpsRepository;
import com.djulb.utils.ZoneService;
import com.djulb.way.elements.redis.RedisGps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

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
    private final RedisGpsRepository redisGpsRepository;

    @PostMapping("/viewport/objects-by-id")
    public Iterable<RedisGps> getViewportObjects(@RequestBody ViewportObjectsGetByIds ids) {
//        System.out.println(ids);
        if (ids.getIds().size() == 0) {
            return new ArrayList<>();
        }

        return redisGpsRepository.findAllById(ids.getIds());
    }
}

