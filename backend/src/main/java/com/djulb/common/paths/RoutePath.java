package com.djulb.common.paths;

import com.djulb.common.coord.Coordinate;
import com.djulb.osrm.model.Waypoint;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.PackagePrivate;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@PackagePrivate
public class RoutePath {
    private List<Subpath> subpaths;
    private Waypoint waypoint;
    private double distanceSoFar;
    private double totalDistance;
    private Coordinate start;
    private Coordinate end;

    public void increaseTraveledDistance(double distane) {
        distanceSoFar += distane;
    }

    public Coordinate getCoordinateAtDistance(double moveDistance) {
        ComparisonSubpath lastSubpath = getLastSubpath(moveDistance);

        // TODO: Start and end are switched here. Find out why
        double remaining = Math.abs(moveDistance - lastSubpath.getDistanceSoFar());
        var end = lastSubpath.getSubpath().getStart();
        var start = lastSubpath.getSubpath().getEnd();
        var distance = lastSubpath.getSubpath().getDistance();

        double Px = Math.abs(start.getLat() * end.getLat());
        double Py = Math.abs(start.getLng() * end.getLng());

        double x = start.getLat() + (remaining / distance) * (end.getLat() - start.getLat());
        double y = start.getLng() + (remaining / distance) * (end.getLng() - start.getLng());

        return Coordinate.builder().lat(x).lng(y).build();
    }

    private ComparisonSubpath getLastSubpath(double pushDistanceMeters) {
        double distanceSoFar = 0;

        List<Object> obj = new ArrayList<>();
        for (Subpath subpath : subpaths) {
            distanceSoFar += subpath.getDistance();
            if (distanceSoFar > pushDistanceMeters) {
                return ComparisonSubpath.builder().subpath(subpath).distanceSoFar(distanceSoFar).build();
            }
        }

        return null;
    }

    public List<Double[]> getPathArray() {
        List<Double[]> sss = new ArrayList<>();

        for (Subpath subpath : subpaths) {
            double lat = subpath.getStart().getLat();
            double lng = subpath.getStart().getLng();
            Double[] q = new Double[2];
            q[0] = lat;
            q[1] = lng;
            sss.add(q);
        }
        return sss;
    }
}
@Data
@Builder
class ComparisonSubpath {
    private Subpath subpath;
    private double distanceSoFar;
}
