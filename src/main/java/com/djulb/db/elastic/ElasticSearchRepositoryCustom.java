package com.djulb.db.elastic;

import com.djulb.db.elastic.dto.EGps;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

public interface ElasticSearchRepositoryCustom {
        public List<SearchHit<EGps>> getObjectsInArea(GeoPoint geoPoint, Double distance, String unit);
}
