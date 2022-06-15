package com.djulb.way.elements;

import com.djulb.way.bojan.Coordinate;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.util.pattern.PathPattern;

import java.util.UUID;

@Data
@Builder
public class FakePerson {
    public enum Status {
        IDLE, WAITING_FOR_CAR, RIDING_IN_CAR, FINISHED
    }
    private UUID uuid;
    private Status status;
    private String zone;
    private Coordinate currentPosition;
}
