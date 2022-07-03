package com.djulb.way.elements;

import com.djulb.way.bojan.Coordinate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Passanger {
    private String id;
    private ObjectStatus status;
    private Coordinate currentPosition;
    private Coordinate destination;
}
