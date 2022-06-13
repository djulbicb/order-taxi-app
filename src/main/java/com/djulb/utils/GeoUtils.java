package com.djulb.utils;

import com.djulb.way.bojan.Coordinate;

public class GeoUtils {
    public static Coordinate getMiddle(Coordinate pOne, Coordinate pTwo) {
        double midLat = mid(pOne.getLat(), pTwo.getLat());
        double midLng = mid(pOne.getLng(), pTwo.getLng());
        return Coordinate.builder().lat(midLat).lng(midLng).build();
    }

    public static double mid(double x, double y) {
        return ((y - x) / 2) + x;
    }
}
