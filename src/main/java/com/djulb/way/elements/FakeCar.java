package com.djulb.way.elements;

import com.djulb.way.bojan.Coordinate;
import com.djulb.way.bojan.DrivingPath;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FakeCar {
    enum Status {
        IDLE, DRIVING_ROUTE, DRIVING_TO_PASSENGER
    }

    Coordinate currentPosition;
    DrivingPath currentDrivingPath;
}
