package com.djulb.ui.sample;

import com.djulb.db.redis.RedisGpsRepository;
import com.djulb.engine.ZoneService;
import com.djulb.messages.redis.RedisGps;
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
    private final RedisGpsRepository redisGpsRepository;

    @PostMapping("/viewport/objects-by-id")
    public Iterable<RedisGps> getViewportObjects(@RequestBody ViewportObjectsGetByIds ids) {
        return new ArrayList<>();
//        System.out.println(ids);
//        if (ids.getIds().size() == 0) {
//            return new ArrayList<>();
//        }
//
//        return redisGpsRepository.findAllById(ids.getIds());
    }
}

