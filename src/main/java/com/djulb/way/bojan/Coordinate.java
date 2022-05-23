package com.djulb.way.bojan;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Coordinate {
    private double lat;
    private double lng;
}
