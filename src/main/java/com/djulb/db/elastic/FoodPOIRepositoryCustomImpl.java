package com.djulb.db.elastic;

import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.Taxi;
import com.djulb.way.elements.redis.RedisGps;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.GeoDistanceOrder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.stream.Collectors;

public class FoodPOIRepositoryCustomImpl implements FoodPOIRepositoryCustom{

    private final ElasticsearchOperations operations;

    public FoodPOIRepositoryCustomImpl(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    public List<RedisGps> toDto(List<SearchHit<ElasticGps>> searchHits) {
        return searchHits.stream()
                .map(searchHit -> {
                    ElasticGps content = searchHit.getContent();
                    return RedisGps.builder()
                            .id(content.getId())
                            .status(content.getType() == ElasticGps.Type.TAXI ? RedisGps.Status.TAXI : RedisGps.Status.PASSANGER)
                            .coordinate(Coordinate.builder().lng(content.getLocation().getLon()).lat(content.getLocation().getLat()).build())
                            .build();
                }).collect(Collectors.toList());
    }
    public List<RedisGps> getAvailableTaxisInArea(Coordinate coordinate, Double distance, String unit) {
        GeoPoint gps = new GeoPoint(coordinate.getLat(), coordinate.getLng());


        Criteria location = new Criteria("location").within(gps, distance.toString() + unit);
        Criteria status = new Criteria("status");
        status.is(Taxi.Status.IDLE);

        Criteria type = new Criteria("type");
        type.is(ElasticGps.Type.TAXI);


        Criteria criteria = Criteria.and().subCriteria(location).subCriteria(status).subCriteria(type);

        Query query = new CriteriaQuery(criteria, Pageable.ofSize(5));

        // add a sort to get the actual distance back in the sort value
        Sort sort = Sort.by(new GeoDistanceOrder("location", gps).withUnit(unit));
        query.addSort(sort);
        return toDto(operations.search(query, ElasticGps.class).getSearchHits());
    }
    public List<SearchHit<ElasticGps>> getObjectsInArea(GeoPoint geoPoint, Double distance, String unit) {
        Query query = new CriteriaQuery(
                new Criteria("location").within(geoPoint, distance.toString() + unit)
        );
        // add a sort to get the actual distance back in the sort value
        Sort sort = Sort.by(new GeoDistanceOrder("location", geoPoint).withUnit(unit));
        query.addSort(sort);
        return operations.search(query, ElasticGps.class).getSearchHits();
    }
}
