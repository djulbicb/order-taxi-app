package com.djulb.usecase.sample.dto;

import com.djulb.way.bojan.Coordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SampleSizeViewportObjectsGetByIds {

    @Test
    void getCoordWithSample1() {
        Coordinate build = Coordinate.builder().lat(1).lng(1).build();
        List<Coordinate> coord = SampleSize.getCoord(build, SampleSize.SIZE_1);
        Assertions.assertEquals(1, coord.size());
        Assertions.assertEquals(List.of(coord.get(0)), coord);
    }

    @Test
    void getCoordWithSample3() {
        Coordinate build = Coordinate.builder().lat(1).lng(1).build();
        List<Coordinate> coord = SampleSize.getCoord(build, SampleSize.SIZE_3);
        Assertions.assertEquals(9, coord.size());
        Assertions.assertEquals(build, coord.get(4));
    }

    @Test
    void getCoordAndZonesWithSample3() {
        Coordinate build = Coordinate.builder().lat(52.54).lng(13.41).build();
        List<String> zones = SampleSize.getZones(build, SampleSize.SIZE_3);
        List<String> expectedZones = List.of(
                "52.60,13.30",
                "52.60,13.40",
                "52.60,13.50",
                "52.55,13.30",
                "52.55,13.40",
                "52.55,13.50",
                "52.50,13.30",
                "52.50,13.40",
                "52.50,13.50"
        );
        Assertions.assertEquals(9, zones.size());
        Assertions.assertEquals("52.55,13.40", zones.get(4));
        Assertions.assertEquals(expectedZones, zones);
    }

    @Test
    void getCoordAndZonesWithSample5() {
        Coordinate build = Coordinate.builder().lat(52.54).lng(13.41).build();
        List<String> zones = SampleSize.getZones(build, SampleSize.SIZE_5);
        List<String> expectedZones = List.of(
                "52.65,13.20",
                "52.65,13.30",
                "52.65,13.40",
                "52.65,13.50",
                "52.65,13.60",

                "52.60,13.20",
                "52.60,13.30",
                "52.60,13.40",
                "52.60,13.50",
                "52.60,13.60",

                "52.55,13.20",
                "52.55,13.30",
                "52.55,13.40",
                "52.55,13.50",
                "52.55,13.60",

                "52.50,13.20",
                "52.50,13.30",
                "52.50,13.40",
                "52.50,13.50",
                "52.50,13.60",

                "52.45,13.20",
                "52.45,13.30",
                "52.45,13.40",
                "52.45,13.50",
                "52.45,13.60"
        );
        Assertions.assertEquals(25, zones.size());
        Assertions.assertEquals("52.55,13.40", zones.get(12));
        Assertions.assertEquals(expectedZones, zones);
    }
}