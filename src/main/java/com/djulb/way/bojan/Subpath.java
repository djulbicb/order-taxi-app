package com.djulb.way.bojan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Subpath {
    private Coordinate start;
    private Coordinate end;
    private double distance;
}
