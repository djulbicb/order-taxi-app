package com.djulb.way.elements;

import com.djulb.way.elements.redis.PassangerRedisGps;
import com.djulb.way.elements.redis.TaxiRedisGps;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class GpsConvertor {
    public static PassangerRedisGps toRedisGps(PassangerGps taxi) {
        return PassangerRedisGps.builder()
                .id(taxi.getId())
                .status(taxi.getStatus())
                .coordinate(taxi.getCoordinate())
                .timestamp(Date.from(Instant.now()))
                .build();
    }
    public static TaxiRedisGps toRedisGps(TaxiGps taxi) {
        return TaxiRedisGps.builder()
                .id(taxi.getId())
                .status(taxi.getStatus())
                .coordinate(taxi.getCoordinate())
                .timestamp(Date.from(Instant.now()))
                .build();
    }
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
