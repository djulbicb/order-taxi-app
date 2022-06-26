package com.djulb.way.bojan;

import com.djulb.way.osrm.model.Intersection;
import com.djulb.way.osrm.model.Step;
import com.djulb.way.osrm.model.Waypoint;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoutePathFactory {
    List<Coordinate> coordinates = new ArrayList<>();

    public void addCoordinate(double lat, double lng) {
        Coordinate coordinate = Coordinate.builder().lat(lat).lng(lng).build();
        coordinates.add(coordinate);
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

    /**
     * Calculates GPS distance between two coordinates
     * Good for short distances like 100m
     * @param lat1
     * @param lat2
     * @param lng1
     * @param lng2
     * @return
     */
    public static double distanceFlatEarth(double lat1, double lat2,
                                           double lng1, double lng2){
        double a = (lat1-lat2)*distPerLat(lat1);
        double b = (lng1-lng2)*distPerLng(lat1);
        return Math.sqrt(a*a+b*b);
    }

    private static double distPerLng(double lat){
        return 0.0003121092*Math.pow(lat, 4)
                +0.0101182384*Math.pow(lat, 3)
                -17.2385140059*lat*lat
                +5.5485277537*lat+111301.967182595;
    }

    private static double distPerLat(double lat){
        return -0.000000487305676*Math.pow(lat, 4)
                -0.0033668574*Math.pow(lat, 3)
                +0.4601181791*lat*lat
                -1.4558127346*lat+110579.25662316;
    }

    public RoutePath buildFromWaypoint(Waypoint block) {
        List<Coordinate> coordinates1 = new ArrayList<>();
        for (Step step : block.getRoutes().get(0).getLegs().get(0).getSteps()) {
            List<Intersection> intersections = step.getIntersections();
            for (Intersection intersection : intersections) {
                double lat = intersection.getLocation().get(1);
                double lng = intersection.getLocation().get(0);
                coordinates1.add(Coordinate.builder().lng(lng).lat(lat).build());
            }
        }
        coordinates1.add(Coordinate.builder().lng(13.29547681726967).lat(52.50546582848033).build());


        double totalDistance = block.getRoutes().get(0).getDistance();

        List<Subpath> subpaths = new ArrayList<>();
        for (int i = 0; i < coordinates1.size()-1; i++) {
            Coordinate i0 = coordinates1.get(i);
            Coordinate i1 = coordinates1.get(i+1);

            double distance1 = distance(i0.getLat(), i1.getLat(), i0.getLng(), i1.getLng());
            double distance = distanceFlatEarth(i0.getLat(), i1.getLat(), i0.getLng(), i1.getLng());
//            System.out.println(String.format("A: %s; B: %s; Distance: %sm", i0.toString(), i1.toString(), distance));
            subpaths.add(Subpath.builder().start(i0).end(i1).distance(distance).build());
        }
//        System.out.println("Total distance: " + totalDistance);


        return RoutePath.builder().subpaths(subpaths).totalDistance(totalDistance).waypoint(block).build();
    }
}
