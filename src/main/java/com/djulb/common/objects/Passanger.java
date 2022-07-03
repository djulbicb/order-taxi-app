package com.djulb.common.objects;

import com.djulb.common.coord.Coordinate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Passanger {
    private String id;
    private ObjectStatus status;
    private ObjectActivity activity;
    private Coordinate currentPosition;
    private Coordinate destination;
}
