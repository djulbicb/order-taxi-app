package com.djulb.way.elements;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class GpsConvertor {
    public static TaxiGps toGps(Taxi taxi) {
        return TaxiGps.builder()
                .id(taxi.getId())
                .status(taxi.getStatus())
                .coordinate(taxi.getCurrentPosition())
                .timestamp(Date.from(Instant.now()))
                .build();
    }
    public static PassangerGps toGps(Passanger passanger) {
        return PassangerGps.builder()
                .id(passanger.getId())
                .status(passanger.getStatus())
                .coordinate(passanger.getCurrentPosition())
                .timestamp(Date.from(Instant.now()))
                .build();
    }
}
