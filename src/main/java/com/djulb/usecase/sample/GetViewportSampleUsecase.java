package com.djulb.usecase.sample;

import com.djulb.usecase.sample.dto.SampleSize;
import com.djulb.utils.ZoneService;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.FakePerson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.util.aggregation.TestAggregationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.graphLookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GetViewportSampleUsecase {

    private final ZoneService zoneService;
    private final MongoTemplate mongoTemplate;

    @GetMapping("/viewport-sample")
    public List<Coordinate> bojan(SampleRequest request) {
        String zone = zoneService.getZone(request.getCoordinate());
        SampleSize size = request.getSize();
        List<String> zones = SampleSize.getZones(request.getCoordinate(), size);


        List<UnionWithOperation> union = new ArrayList<>();
        for (String s : zones) {
            union.add(UnionWithOperation.unionWith(s));
        }

        Aggregation aggregation = TypedAggregation.newAggregation(
//               UnionWithOperation.unionWith("52.40,13.10"),
//               UnionWithOperation.unionWith("52.40,13.30")
                union
        );
        List<FakePerson> bojan = mongoTemplate.aggregate(aggregation, "bojan", FakePerson.class).getMappedResults();
//        List<AggregationOperation> operations = new ArrayList<>();
//        operations.add(
//                Aggregation.project()
//                        .and("52.40,13.10").concatArrays("52.40,13.10").as("item")
//        );
//        operations.add(Aggregation.unwind("item"));
//        Aggregation aggregation = Aggregation.newAggregation(operations);

        List<FakePerson> all = mongoTemplate.findAll(FakePerson.class, zone);
        List<Coordinate> collect = bojan.stream().map(fakePerson -> fakePerson.getCurrentPosition()).collect(Collectors.toList());
        System.out.println(request);
        return collect;
    }

}
