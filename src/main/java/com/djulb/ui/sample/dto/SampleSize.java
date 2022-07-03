package com.djulb.ui.sample.dto;

import com.djulb.utils.ZoneService;
import com.djulb.way.bojan.Coordinate;

import java.util.ArrayList;
import java.util.List;

public enum SampleSize {
    SIZE_1(1), SIZE_3(3), SIZE_5(5);

    int size;
    SampleSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public static List<String> getZones(Coordinate coordinate, SampleSize size) {
        List<Coordinate> coord = getCoord(coordinate, size);
        List<String> zones = new ArrayList<>();
        for (Coordinate coordinate1 : coord) {
            zones.add(ZoneService.getZone(coordinate1));
        }
        return zones;
    }
    public static List<Coordinate> getCoord(Coordinate coordinate, SampleSize size) {
        double latIncrement = 0.05;
        double lngIncrement = 0.1;

        if (size == SIZE_1) {
            return List.of(coordinate);
        }

        int offset = ((int)Math.floor(size.getSize() / 2));
        int totalIterations = offset * 2 + 1;

        List<Coordinate> coordinates = new ArrayList<>();
        double startLat = coordinate.getLat() + latIncrement * (offset);
        double startLng = coordinate.getLng() - lngIncrement * (offset);
        for (int i = 0; i < totalIterations; i++) {
            for (int j = 0; j < totalIterations; j++) {
                coordinates.add(Coordinate.builder().lat(startLat - latIncrement * i)
                                                    .lng(startLng + lngIncrement * j)
                                                    .build());
            }
        }
        return coordinates;

    }

}