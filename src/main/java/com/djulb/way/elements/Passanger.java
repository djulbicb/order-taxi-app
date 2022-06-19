package com.djulb.way.elements;

import com.djulb.way.bojan.Coordinate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Passanger {
    public enum Status {
        IDLE, WAITING_FOR_CAR, RIDING_IN_CAR, FINISHED
    }
    private String id;
    private Status status;
    private Coordinate currentPosition;
}
