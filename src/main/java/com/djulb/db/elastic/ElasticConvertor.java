package com.djulb.db.elastic;

import com.djulb.common.objects.*;
import com.djulb.db.elastic.dto.EGps;
import com.djulb.db.kafka.model.PassangerKGps;
import com.djulb.db.kafka.model.TaxiKGps;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

public class ElasticConvertor {

    public static EGps objToElastic(Taxi taxi) {
        return EGps.builder()
                .id(taxi.getId())
                .status(taxi.getStatus())
                .type(ObjectType.TAXI)
                .location(new GeoPoint(taxi.getCurrentPosition().getLat(), taxi.getCurrentPosition().getLng()))
                .activity(taxi.getActivity())
                .build();
    }

    public static EGps objToElastic(Passanger passanger) {
        return EGps.builder()
                .id(passanger.getId())
                .status(passanger.getStatus())
                .type(ObjectType.PASSANGER)
                .location(new GeoPoint(passanger.getCurrentPosition().getLat(), passanger.getCurrentPosition().getLng()))
                .activity(passanger.getActivity())
                .build();
    }

    public static EGps objToElastic(PassangerKGps passanger) {
        return EGps.builder()
                .id(passanger.getId())
                .status(passanger.getStatus())
                .type(ObjectType.PASSANGER)
                .location(new GeoPoint(passanger.getCoordinate().getLat(), passanger.getCoordinate().getLng()))
                .activity(passanger.getActivity())
                .build();
    }

    public static EGps objToElastic(TaxiKGps taxiKGps) {
        return EGps.builder()
                .id(taxiKGps.getId())
                .status(taxiKGps.getStatus())
                .type(ObjectType.TAXI)
                .location(new GeoPoint(taxiKGps.getCoordinate().getLat(), taxiKGps.getCoordinate().getLng()))
                .activity(taxiKGps.getActivity())
                .build();
    }
}
