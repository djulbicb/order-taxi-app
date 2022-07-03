package com.djulb.db.elastic;

import com.djulb.db.elastic.dto.EGps;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.ObjectActivity;
import com.djulb.way.elements.ObjectStatus;
import com.djulb.way.elements.ObjectType;
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

public class ElasticSearchRepositoryCustomImpl implements ElasticSearchRepositoryCustom {

    private final ElasticsearchOperations operations;

    public ElasticSearchRepositoryCustomImpl(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    public List<RedisGps> toDto(List<SearchHit<EGps>> searchHits) {
        return searchHits.stream()
                .map(searchHit -> {
                    EGps content = searchHit.getContent();
                    return RedisGps.builder()
                            .id(content.getId())
                            .status(content.getType())
                            .coordinate(Coordinate.builder().lng(content.getLocation().getLon()).lat(content.getLocation().getLat()).build())
                            .build();
                }).collect(Collectors.toList());
    }
    public List<RedisGps> getAvailableTaxisInArea(Coordinate coordinate, Double distance, String unit) {
        GeoPoint gps = new GeoPoint(coordinate.getLat(), coordinate.getLng());

        Criteria location = new Criteria("location").within(gps, distance.toString() + unit);
        Criteria status = new Criteria("status").is(ObjectStatus.IDLE);
        Criteria type = new Criteria("type").is(ObjectType.TAXI);
        //Criteria activity = new Criteria("type").is(ObjectActivity.ACTIVE);

        Criteria criteria = Criteria.and().subCriteria(location).subCriteria(status).subCriteria(type);

        Query query = new CriteriaQuery(criteria, Pageable.ofSize(5));

        // add a sort to get the actual distance back in the sort value
        Sort sort = Sort.by(new GeoDistanceOrder("location", gps).withUnit(unit));
        query.addSort(sort);
        return toDto(operations.search(query, EGps.class).getSearchHits());
    }
    public List<SearchHit<EGps>> getObjectsInArea(GeoPoint geoPoint, Double distance, String unit) {
        Query query = new CriteriaQuery(
                new Criteria("location").within(geoPoint, distance.toString() + unit)
                        .and().subCriteria(new Criteria("activity").is(ObjectActivity.ACTIVE))
        );

        Query q = new CriteriaQuery(
                new Criteria("location").within(geoPoint, distance.toString() + unit)
        );
        // add a sort to get the actual distance back in the sort value
        Sort sort = Sort.by(new GeoDistanceOrder("location", geoPoint).withUnit(unit));
        query.addSort(sort);
        return operations.search(query, EGps.class).getSearchHits();
    }
}
