package com.djulb.usecase.sample;

import com.djulb.usecase.sample.dto.SampleLayer;
import com.djulb.usecase.sample.dto.SampleObject;
import com.djulb.usecase.sample.dto.SampleSize;
import com.djulb.way.bojan.Coordinate;
import lombok.Data;

import java.util.List;

@Data
public class SampleRequest {
    double lat;
    double lng;
    List<SampleObject> objects;
    SampleSize size;
    SampleLayer layer;

    public Coordinate getCoordinate() {
        return Coordinate.builder().lat(lat).lng(lng).build();
    }
}
