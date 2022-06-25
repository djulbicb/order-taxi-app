package com.djulb.way.elements;

import com.djulb.way.bojan.Coordinate;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class Passanger {
    private String id;
    private Taxi.Status status;
    private Coordinate currentPosition;
    private Coordinate destination;
}
