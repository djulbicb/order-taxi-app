package com.djulb.way.elements;

import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.redis.RedisGps;
import org.springframework.data.geo.Point;

import java.time.Instant;
import java.util.Date;

public class GpsConvertor {
    public static RedisGps toRedisGps(PassangerGps taxi) {
        return RedisGps.builder()
                .id(taxi.getId())
                .status(RedisGps.Status.PASSANGER)
                .coordinate(new Coordinate(taxi.getCoordinate().getLat(), taxi.getCoordinate().getLng()))
                .timestamp(Date.from(Instant.now()))
                .build();
    }
    public static RedisGps toRedisGps(TaxiGps taxi) {
        return RedisGps.builder()
                .id(taxi.getId())
                .status(RedisGps.Status.TAXI)
                .coordinate(new Coordinate(taxi.getCoordinate().getLat(), taxi.getCoordinate().getLng()))
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
