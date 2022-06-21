package com.djulb.db.elastic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
@Builder
@AllArgsConstructor
public class ResultData {
    private String name;
    private GeoPoint location;
    private Double distance;
}