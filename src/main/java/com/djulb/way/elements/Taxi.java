package com.djulb.way.elements;

import com.djulb.way.bojan.Coordinate;
import com.djulb.way.bojan.RoutePath;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class Taxi {
    String id;
    Coordinate currentPosition;
    Status status;
    Optional<RoutePath> currentRoutePath;
    public enum Status {
        IDLE, DRIVING_ROUTE, DRIVING_TO_PASSENGER
    }
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
        return status == Status.IDLE;
    }
    public boolean hasPath() {
        return currentRoutePath.isPresent();
    }
}
