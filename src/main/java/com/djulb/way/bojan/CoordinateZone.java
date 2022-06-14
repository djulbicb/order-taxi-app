package com.djulb.way.bojan;

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
