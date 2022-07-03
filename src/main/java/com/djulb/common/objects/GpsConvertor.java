package com.djulb.common.objects;

import com.djulb.common.coord.Coordinate;
import com.djulb.messages.redis.RedisGps;

import java.time.Instant;
import java.util.Date;

public class GpsConvertor {
    public static RedisGps toRedisGps(PassangerKGps taxi) {
        return RedisGps.builder()
                .id(taxi.getId())
                .status(ObjectType.PASSANGER)
                .coordinate(new Coordinate(taxi.getCoordinate().getLat(), taxi.getCoordinate().getLng()))
                .timestamp(Date.from(Instant.now()))
                .activity(taxi.getActivity())
                .build();
    }
    public static RedisGps toRedisGps(TaxiKGps taxi) {
        return RedisGps.builder()
                .id(taxi.getId())
                .status(ObjectType.TAXI)
                .coordinate(new Coordinate(taxi.getCoordinate().getLat(), taxi.getCoordinate().getLng()))
                .timestamp(Date.from(Instant.now()))
                .activity(taxi.getActivity())
                .build();
    }
    public static TaxiKGps toGps(Taxi taxi) {
        return TaxiKGps.builder()
                .id(taxi.getId())
                .status(taxi.getStatus())
                .coordinate(taxi.getCurrentPosition())
                .timestamp(Date.from(Instant.now()))
                .activity(taxi.getActivity())
                .build();
    }

    public static Taxi toGps(TaxiKGps taxi) {
        return Taxi.builder()
                .id(taxi.getId())
                .status(taxi.getStatus())
                .currentPosition(taxi.getCoordinate())
                .activity(taxi.getActivity())
                .build();
    }

    public static PassangerKGps toGps(Passanger passanger) {
        return PassangerKGps.builder()
                .id(passanger.getId())
                .status(passanger.getStatus())
                .coordinate(passanger.getCurrentPosition())
                .timestamp(Date.from(Instant.now()))
                .activity(passanger.getActivity())
                .build();
    }
}
