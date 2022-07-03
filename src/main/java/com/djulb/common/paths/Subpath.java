package com.djulb.common.paths;

import com.djulb.common.coord.Coordinate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Subpath {
    private Coordinate start;
    private Coordinate end;
    private double distance;
}
