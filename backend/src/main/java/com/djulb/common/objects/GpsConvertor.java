package com.djulb.common.objects;

import com.djulb.common.coord.Coordinate;
import com.djulb.db.kafka.model.PassangerKGps;
import com.djulb.db.kafka.model.TaxiKGps;
import com.djulb.ui.model.GpsUi;

import java.time.Instant;
import java.util.Date;

public class GpsConvertor {
    public static GpsUi convertKafkaGpsUi(PassangerKGps taxi) {
        return GpsUi.builder()
                .id(taxi.getId())
                .type(ObjectType.PASSANGER)
                .coordinate(new Coordinate(taxi.getCoordinate().getLat(), taxi.getCoordinate().getLng()))
                .timestamp(Date.from(Instant.now()))
                .activity(taxi.getActivity())
                .status(taxi.getStatus())
                .build();
    }
    public static GpsUi convertKafkaGpsUi(TaxiKGps taxi) {
        return GpsUi.builder()
                .id(taxi.getId())
                .type(ObjectType.TAXI)
                .coordinate(new Coordinate(taxi.getCoordinate().getLat(), taxi.getCoordinate().getLng()))
                .timestamp(Date.from(Instant.now()))
                .activity(taxi.getActivity())
                .status(taxi.getStatus())
                .build();
    }
    public static TaxiKGps toGps(Taxi taxi) {
        return TaxiKGps.builder()
                .id(taxi.getId())
                .status(taxi.getStatus())
                .coordinate(taxi.getCurrentPosition())
                .timestamp(Date.from(Instant.now()))
                .activity(taxi.getActivity())
                .status(taxi.getStatus())
                .build();
    }

    public static Taxi toGps(TaxiKGps taxi) {
        return Taxi.builder()
                .id(taxi.getId())
                .status(taxi.getStatus())
                .currentPosition(taxi.getCoordinate())
                .activity(taxi.getActivity())
                .status(taxi.getStatus())
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
