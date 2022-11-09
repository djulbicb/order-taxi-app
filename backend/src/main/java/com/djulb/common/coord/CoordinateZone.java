package com.djulb.common.coord;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CoordinateZone{

    private String zone;
    private Coordinate coordinate;
    public String formatted() {
        return coordinate.formatted();
    }

}
