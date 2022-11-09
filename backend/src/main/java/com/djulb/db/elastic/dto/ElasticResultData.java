package com.djulb.db.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
@Builder
@AllArgsConstructor
public class ElasticResultData {
    private String name;
    private GeoPoint location;
    private Double distance;
}