package com.djulb.utils;

import com.djulb.engine.ZoneService;
import com.djulb.way.bojan.Coordinate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ZoneServiceViewportObjectsGetByIds {
    @Autowired
    ZoneService zoneService;

    @Test
    void load() {

    }

    @Test
    void getZone() {
        Coordinate coordinate1 = Coordinate.builder().lat(52.472191).lng(13.32694).build();
        String zone1 = zoneService.getZone(coordinate1);
        assertEquals("52.50,13.30", zone1);

        Coordinate coordinate2 = Coordinate.builder().lat(52.434676).lng(13.32619).build();
        String zone2 = zoneService.getZone(coordinate2);
        assertEquals("52.45,13.30", zone2);

        Coordinate coordinate3 = Coordinate.builder().lat(52.434362).lng(13.715192).build();
        String zone3 = zoneService.getZone(coordinate3);
        assertEquals("52.45,13.70", zone3);

        Coordinate coordinate4 = Coordinate.builder().lat(52.629057).lng(13.528003).build();
        String zone4 = zoneService.getZone(coordinate4);
        assertEquals("52.65,13.50", zone4);

        Coordinate coordinate5 = Coordinate.builder().lat(52.661312).lng(13.283742).build();
        String zone5 = zoneService.getZone(coordinate5);
        assertEquals("52.70,13.20", zone5);
    }


//    void getZones() {
//        // box from 0,0 to 10,10
//        BBox box = BBox.builder()
//                .bottomLeft(Coordinate.builder().lng(0).lat(0).build())
//                .topRight(Coordinate.builder().lng(10).lat(10).build())
//                .topLeft(Coordinate.builder().lng(0).lat(10).build())
//                .bottomRight(Coordinate.builder().lng(10).lat(0).build())
//                .build();
//
//        Coordinate coordinate = Coordinate.builder().lat(2).lng(5).build();
//
//        Map<String, BBox> zones = zoneBank.getZones(box, 10, 10);
//        String zone = zoneBank.getZone(box, 10, 10, coordinate);
//        System.out.println(zone);
//    }
}