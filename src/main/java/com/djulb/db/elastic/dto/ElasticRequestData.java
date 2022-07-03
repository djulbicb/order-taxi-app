package com.djulb.db.elastic.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ElasticRequestData {
    private String name;
    private double lat;
    private double lon;
}