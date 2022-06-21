package com.djulb.db.elastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.GeoDistanceOrder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Sort;
import java.util.List;
public class FoodPOIRepositoryCustomImpl implements FoodPOIRepositoryCustom{

    private final ElasticsearchOperations operations;
    public FoodPOIRepositoryCustomImpl(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    public List<SearchHit<ElasticGps>> sss(GeoPoint geoPoint, Double distance, String unit) {
        Query query = new CriteriaQuery(
                new Criteria("location").within(geoPoint, distance.toString() + unit)
        );
        // add a sort to get the actual distance back in the sort value
        Sort sort = Sort.by(new GeoDistanceOrder("location", geoPoint).withUnit(unit));
        query.addSort(sort);
        return operations.search(query, ElasticGps.class).getSearchHits();
    }
}
