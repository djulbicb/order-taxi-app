package com.djulb.way.bojan;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DrivingPath {
    private List<Subpath> subpaths;
    private double totalDistance;
    private Coordinate start;
    private Coordinate end;

    public Coordinate getCoordinateAtDistance(double moveDistance) {
        ComparisonSubpath lastSubpath = getLastSubpath(moveDistance);

        double remaining = moveDistance - lastSubpath.getDistanceSoFar();
        var start = lastSubpath.getSubpath().getStart();
        var end = lastSubpath.getSubpath().getEnd();
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
}
@Data
@Builder
class ComparisonSubpath {
    private Subpath subpath;
    private double distanceSoFar;
}
