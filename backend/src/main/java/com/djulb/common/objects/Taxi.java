package com.djulb.common.objects;

import com.djulb.common.coord.Coordinate;
import com.djulb.common.paths.RoutePath;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class Taxi {
    String id;
    Coordinate currentPosition;
    ObjectStatus status;
    ObjectActivity activity;
    Optional<RoutePath> currentRoutePath;

    public void addPath(RoutePath route) {
        this.currentRoutePath = Optional.of(route);
    }

    public void move(double dist) {
        if (currentRoutePath.isPresent()) {
            RoutePath routePath = currentRoutePath.get();
            Coordinate coordinateAtDistance = routePath.getCoordinateAtDistance(routePath.getDistanceSoFar() + dist);
            setCurrentPosition(coordinateAtDistance);
            routePath.increaseTraveledDistance(dist);
        }
    }

    public boolean isRouteFinished() {
        if (currentRoutePath.isPresent()) {
            return currentRoutePath.get().getDistanceSoFar() >= currentRoutePath.get().getTotalDistance();
        }
        System.out.println("no path assigned");
        return false;
    }



    public boolean isIdle() {
        return status == ObjectStatus.IDLE;
    }
    public boolean hasPath() {
        return currentRoutePath.isPresent();
    }
}
