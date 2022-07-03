package com.djulb.way.bojan;

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

    public List<Object[]> getPathArray() {
        List<Object[]> sss = new ArrayList<>();

        for (Subpath subpath : subpaths) {
            double lat = subpath.getStart().getLat();
            double lng = subpath.getStart().getLng();
            List<Double> q = new ArrayList<>();
            q.add(lat);
            q.add(lng);
            sss.add(q.toArray());
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
