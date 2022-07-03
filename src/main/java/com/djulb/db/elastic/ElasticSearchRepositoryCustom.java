package com.djulb.db.elastic;

import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

public interface ElasticSearchRepositoryCustom {
        public List<SearchHit<ElasticGps>> getObjectsInArea(GeoPoint geoPoint, Double distance, String unit);
}
