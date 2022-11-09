package com.djulb;

import com.djulb.common.coord.BBox;
import com.djulb.common.coord.Coordinate;

public class TestData {
    public static BBox testBBox() {
        return BBox.builder()
        .bottomLeft(Coordinate.builder().lng(0).lat(0).build())
        .topRight(Coordinate.builder().lng(10).lat(10).build())
        .topLeft(Coordinate.builder().lng(0).lat(10).build())
        .bottomRight(Coordinate.builder().lng(10).lat(0).build())
        .build();
    }
}
