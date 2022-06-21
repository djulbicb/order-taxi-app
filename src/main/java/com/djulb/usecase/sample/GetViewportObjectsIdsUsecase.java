package com.djulb.usecase.sample;

import com.djulb.db.redis.RedisGpsRepository;
import com.djulb.usecase.sample.dto.SampleObject;
import com.djulb.usecase.sample.dto.SampleSize;
import com.djulb.utils.ZoneService;
import com.djulb.way.elements.PassangerGps;
import com.djulb.way.elements.TaxiGps;
import com.djulb.way.elements.redis.RedisGps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.aggregation.UnionWithOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GetViewportObjectsIdsUsecase {

    private final ZoneService zoneService;
    @Qualifier("mongoTaxiDb")
    private final MongoTemplate mongoTaxiDb;
    @Qualifier("mongoPassangerDb")
    private final MongoTemplate mongoPassangerDb;
    private final RedisGpsRepository redisGpsRepository;
    @GetMapping("/viewport/objects-in-area")
    public List<RedisGps> getViewportObjects(SampleRequest request) {
        String placeholderCollectionName = "placeholder";

        List<String> zones = SampleSize.getZones(request.getCoordinate(), request.getSize());

        List<UnionWithOperation> zonesUnion = new ArrayList<>();
        for (String s : zones) {
            zonesUnion.add(UnionWithOperation.unionWith(s));
        }
        Aggregation aggregation = TypedAggregation.newAggregation(
//               UnionWithOperation.unionWith("52.40,13.10"),
                zonesUnion
        );
//
        List<PassangerGps> passangers = mongoPassangerDb.aggregate(aggregation, placeholderCollectionName, PassangerGps.class).getMappedResults();
        List<TaxiGps> taxis = mongoTaxiDb.aggregate(aggregation, placeholderCollectionName, TaxiGps.class).getMappedResults();

        HashMap<SampleObject, Object> allGps = new HashMap<>();
        allGps.put(SampleObject.PASSANGER, passangers);
        allGps.put(SampleObject.TAXI, taxis);


        List<String> ids = new ArrayList<>();
        passangers.stream().forEach(passangerGps -> ids.add(passangerGps.getId()));
        taxis.stream().forEach(passangerGps -> ids.add(passangerGps.getId()));
        Iterable<RedisGps> allById = redisGpsRepository.findAllById(ids);
        return StreamSupport.stream(allById.spliterator(), false)
                .collect(Collectors.toList());
        // return redisGpsRepository.findByCoordinateNear(new Point(request.getLng(), request.getLat()), new Distance(10000));

//        List<String> ids = new ArrayList<>();
//        passangers.stream().forEach(passangerGps -> ids.add(passangerGps.getId()));
//        taxis.stream().forEach(passangerGps -> ids.add(passangerGps.getId()));
//        System.out.println(ids.size());
//        return ids;
    }

}

// https://stackoverflow.com/questions/65735942/convert-the-aggregation-query-of-mongodb-for-spring-boot
//        Aggregation.newAggregation(
//                project().and(ObjectOperators.valueOf(ROOT).toArray()).as("data"),
//                unwind("data"),
//                match(Criteria.where("data.v").regex("Mohit Chandani")
//                )
//        ).withOptions(AggregationOptions.builder().allowDiskUse(Boolean.TRUE).build());


//        List<AggregationOperation> operations = new ArrayList<>();
//        operations.add(
//                Aggregation.project()
//                        .and("52.40,13.10").concatArrays("52.40,13.10").as("item")
//        );
//        operations.add(Aggregation.unwind("item"));
//        Aggregation aggregation = Aggregation.newAggregation(operations);