package com.djulb.way;

import com.djulb.osrm.model.Intersection;
import com.djulb.osrm.model.Step;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.bojan.RoutePath;

import java.util.ArrayList;
import java.util.List;

// Taken from
//http://www.java2s.com/example/java/java.lang/return-the-xy-position-at-distance-length-into-the-given-polyline.html
public class PathCalculator{

    public void haveNoIdea(RoutePath routePath) {
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();

        for (Step step : routePath.getWaypoint().getRoutes().get(0).getLegs().get(0).getSteps()) {
            List<Intersection> intersections = step.getIntersections();
            for (Intersection intersection : intersections) {
                double lat = intersection.getLocation().get(1);
                double lng = intersection.getLocation().get(0);
                x.add(lat);
                y.add(lng);
            }
        }
    }
    /* from   ww  w.  j  a  v a2s  .c o m
     * Return the x,y position at distance "length" into the given polyline.
            *
            * @param x X coordinates of polyline
     * @param y Y coordinates of polyline
     * @param length Requested position
     * @param position Preallocated to int[2]
            * @return True if point is within polyline, false otherwise
     */
    public static Coordinate findCoordinateAtPathPosition(Double[] x, Double[] y,
                                                          double length) {
        if (length < 0) {
            return Coordinate.builder().lat(0).lng(0).build();
        }
        double accumulatedLength = 0.0;
        for (int i = 1; i < x.length; i++) {
            double legLength = length(x[i - 1], y[i - 1],
                    x[i], y[i]);
            if (legLength + accumulatedLength >= length) {
                double part = length - accumulatedLength;
                double fraction = part / legLength;
                double position0 = (x[i - 1] + fraction
                        * (x[i] - x[i - 1]));
                double position1 = (y[i - 1] + fraction
                        * (y[i] - y[i - 1]));

                return Coordinate.builder()
                        .lat(position0)
                        .lng(position1)
                        .build();
            }

            accumulatedLength += legLength;
        }

        // Length is longer than polyline
        return Coordinate.builder().lat(0).lng(0).build();
    }
    /**
     * Return the length of a vector.
     *
     * @param v Vector to compute length of [x,y,z].
     * @return Length of vector.
     */
    public static double length(double[] v) {
        return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }
    /**
     * Compute distance between two points.
     *
     * @param p0, p1 Points to compute distance between [x,y,z].
     * @return Distance between points.
     */
    public static double length(double[] p0, double[] p1) {
        double[] v = createVector(p0, p1);
        return length(v);
    }
    /**
     * Compute the length of the line from (x0,y0) to (x1,y1)
     *
     * @param x0, y0 First line end point.
     * @param x1, y1 Second line end point.
     * @return Length of line from (x0,y0) to (x1,y1).
     */
//    public static double length(double x0, int y0, int x1, int y1) {
//        return GeometryUtils.length((double) x0, (double) y0, (double) x1,
//                (double) y1);
//    }
    /**
     * Compute the length of the line from (x0,y0) to (x1,y1)
     *
     * @param x0, y0 First line end point.
     * @param x1, y1 Second line end point.
     * @return Length of line from (x0,y0) to (x1,y1).
     */
    public static double length(double x0, double y0, double x1, double y1) {
        return distance(x0, x1, y0, y1);

//        double dx = x1 - x0;
//        double dy = y1 - y0;
//
//        return Math.sqrt(dx * dx + dy * dy);
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
     * Compute the length of a polyline.
     *
     * @param x, y Arrays of x,y coordinates
     * @param nPoints Number of elements in the above.
     * @param isClosed True if this is a closed polygon, false otherwise
     * @return Length of polyline defined by x, y and nPoints.
     */
    public static double length(double[] x, double[] y, boolean isClosed) {
        double length = 0.0;

        int nPoints = x.length;
        for (int i = 0; i < nPoints - 1; i++) {
            length += length(x[i], y[i], x[i + 1], y[i + 1]);
        }

        // Add last leg if this is a polygon
        if (isClosed && nPoints > 1) {
            length += length(x[nPoints - 1], y[nPoints - 1],
                    x[0], y[0]);
        }

        return length;
    }
    /**
     * Construct the vector specified by two points.
     *
     * @param p0, p1 Points the construct vector between [x,y,z].
     * @return v Vector from p0 to p1 [x,y,z].
     */
    public static double[] createVector(double[] p0, double[] p1) {
        double v[] = { p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2] };
        return v;
    }
}
