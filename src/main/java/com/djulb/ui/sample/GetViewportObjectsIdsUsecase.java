package com.djulb.ui.sample;

import com.djulb.db.elastic.FoodPOIRepository;
import com.djulb.db.elastic.ElasticGps;
import com.djulb.utils.ZoneService;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.redis.RedisGps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GetViewportObjectsIdsUsecase {

    private final ZoneService zoneService;
    @Qualifier("mongoTaxiDb")
    private final MongoTemplate mongoTaxiDb;
    @Qualifier("mongoPassangerDb")
    private final MongoTemplate mongoPassangerDb;
    private final FoodPOIRepository elasticGpsRepository;
    ;
    @GetMapping("/viewport/objects-in-area")
    public List<RedisGps> getViewportObjects(SampleRequest request) {
        GeoPoint location = new GeoPoint(request.getLat(), request.getLng());
        return elasticGpsRepository.getObjectsInArea(location, 50.0, "km").stream()
                .map(searchHit -> {
                    Double distance = (Double) searchHit.getSortValues().get(0);
                    ElasticGps content = searchHit.getContent();
                    return RedisGps.builder()
                            .id(content.getId())
                            .status(content.getType() == ElasticGps.Type.TAXI ? RedisGps.Status.TAXI : RedisGps.Status.PASSANGER)
                            .coordinate(Coordinate.builder().lng(content.getLocation().getLon()).lat(content.getLocation().getLat()).build())
                            .build();
                }).collect(Collectors.toList());
    }

    //        String placeholderCollectionName = "placeholder";
//
//        List<String> zones = SampleSize.getZones(request.getCoordinate(), request.getSize());
//
//        List<UnionWithOperation> zonesUnion = new ArrayList<>();
//        for (String s : zones) {
//            zonesUnion.add(UnionWithOperation.unionWith(s));
//        }
//        Aggregation aggregation = TypedAggregation.newAggregation(
////               UnionWithOperation.unionWith("52.40,13.10"),
//                zonesUnion
//        );
////
//        List<PassangerGps> passangers = mongoPassangerDb.aggregate(aggregation, placeholderCollectionName, PassangerGps.class).getMappedResults();
//        List<TaxiGps> taxis = mongoTaxiDb.aggregate(aggregation, placeholderCollectionName, TaxiGps.class).getMappedResults();
//
//        HashMap<SampleObject, Object> allGps = new HashMap<>();
//        allGps.put(SampleObject.PASSANGER, passangers);
//        allGps.put(SampleObject.TAXI, taxis);
//
//
//        List<String> ids = new ArrayList<>();
//        passangers.stream().forEach(passangerGps -> ids.add(passangerGps.getId()));
//        taxis.stream().forEach(passangerGps -> ids.add(passangerGps.getId()));
//        Iterable<RedisGps> allById = redisGpsRepository.findAllById(ids);

//        return testss.stream().map(resultData -> RedisGps.builder()
//                .id(resultData.getName())
//                .status(RedisGps.Status.TAXI)
//                .coordinate(Coordinate.builder().lng(resultData.getLocation().getLon()).lat(resultData.getLocation().getLat()).build())
//                .build()).collect(Collectors.toList());
//        return StreamSupport.stream(allById.spliterator(), false)
//                .collect(Collectors.toList());
    // return redisGpsRepository.findByCoordinateNear(new Point(request.getLng(), request.getLat()), new Distance(10000));

//        List<String> ids = new ArrayList<>();
//        passangers.stream().forEach(passangerGps -> ids.add(passangerGps.getId()));
//        taxis.stream().forEach(passangerGps -> ids.add(passangerGps.getId()));
//        System.out.println(ids.size());
//        return ids;


//    private List<ResultData> testss(SampleRequest requestData) {
////        GeoPoint location = new GeoPoint(requestData.getLat(), requestData.getLng());
////        Sort sort = Sort.by(new GeoDistanceOrder("location", location).withUnit("km"));
////        List<SearchHit<ElasticGps>> searchHits = elasticGpsRepository.searchTop50ByTypeAndStatus(
////                ElasticGps.Type.TAXI, ElasticGps.Status.IDLE, sort
////        );
//////        if (StringUtils.hasText(requestData.getName())) {
//////            searchHits = repository.searchTop3ByName(requestData.getName(), sort);
//////        } else {
//////            searchHits = repository.searchTop3By(sort);
//////        }
////        return searchHits.stream()
////                .map(searchHit -> {
////                    Double distance = (Double) searchHit.getSortValues().get(0);
////                    ElasticGps content = searchHit.getContent();
////                    return new ResultData(content.getId(), content.getLocation(), distance);
////                }).collect(Collectors.toList());
//    }

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