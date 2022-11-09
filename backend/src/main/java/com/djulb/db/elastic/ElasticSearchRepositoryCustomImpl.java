package com.djulb.db.elastic;

import com.djulb.db.elastic.dto.EGps;
import com.djulb.common.coord.Coordinate;
import com.djulb.common.objects.ObjectActivity;
import com.djulb.common.objects.ObjectStatus;
import com.djulb.common.objects.ObjectType;
import com.djulb.ui.model.GpsUi;
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

    public List<GpsUi> toDto(List<SearchHit<EGps>> searchHits) {
        return searchHits.stream()
                .map(searchHit -> {
                    EGps content = searchHit.getContent();
                    return GpsUi.builder()
                            .id(content.getId())
                            .type(content.getType())
                            .coordinate(Coordinate.builder().lng(content.getLocation().getLon()).lat(content.getLocation().getLat()).build())
                            .build();
                }).collect(Collectors.toList());
    }
    public List<GpsUi> getAvailableTaxisInArea(Coordinate coordinate, Double distance, String unit) {
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
        Criteria location = new Criteria("location").within(geoPoint, distance.toString() + unit);
        Criteria activity = new Criteria("activity").is(ObjectActivity.ACTIVE);

        Criteria criteria = Criteria.and().subCriteria(location).subCriteria(activity);

        Query query = new CriteriaQuery(criteria, Pageable.ofSize(1000));

        // add a sort to get the actual distance back in the sort value
        Sort sort = Sort.by(new GeoDistanceOrder("location", geoPoint).withUnit(unit));
        query.addSort(sort);
        return operations.search(query, EGps.class).getSearchHits();
    }
}
