package com.djulb.utils;

import com.djulb.way.bojan.Coordinate;
import lombok.Builder;
import lombok.Data;

// http://bboxfinder.com/#0.000000,0.000000,0.000000,0.000000
// bottom left 13.110809,52.377695,
// top right 13.759003,52.661392

// top left 13.110809,52.661392
// bottom right 13.759003,52.377695
@Data
@Builder
public class BBox {
    private Coordinate topLeft;
    private Coordinate topRight;
    private Coordinate bottomLeft;
    private Coordinate bottomRight;

    public static BBox getBerlinBbox() {
        return BBox.builder()
                .topLeft(Coordinate.builder().lat(52.661392).lng(13.110809).build())        //
                .topRight(Coordinate.builder().lat(52.661392).lng(13.759003).build())       //
                .bottomLeft(Coordinate.builder().lat(52.377695).lng(13.110809).build())     //
                .bottomRight(Coordinate.builder().lat(52.377695).lng(13.759003).build())    //
                .build();
    }
}
