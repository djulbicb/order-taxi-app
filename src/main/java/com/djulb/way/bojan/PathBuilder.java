package com.djulb.way.bojan;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PathBuilder {
    List<Coordinate> coordinates = new ArrayList<>();

    public void addCoordinate(double lat, double lng) {
        Coordinate coordinate = Coordinate.builder().lat(lat).lng(lng).build();
        coordinates.add(coordinate);
    }

    public Path build() {
        double totalDistance = 0d;
        List<Subpath> subpaths = new ArrayList<>();
        for (int i = 0; i < coordinates.size()-1; i++) {
            Coordinate i0 = coordinates.get(i);
            Coordinate i1 = coordinates.get(i+1);

//            double x = i1.lat - i0.lat;
//            double y = i1.lng - i0.lng;

//            double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            double distance = distance(i0.getLat(), i1.getLat(), i0.getLng(), i1.getLng());
            System.out.println(String.format("A: %s; B: %s; Distance: %sm", i0.toString(), i1.toString(), distance));
            System.out.println();
            subpaths.add(Subpath.builder().start(i0).end(i1).distance(distance).build());
            totalDistance += distance;
        }
        System.out.println("Total distance: " + totalDistance);
        return Path.builder().subpaths(subpaths).distance(totalDistance).build();
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {
        double el1 = 0d, el2 = 0d;
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
