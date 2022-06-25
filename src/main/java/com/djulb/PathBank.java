package com.djulb;

import com.djulb.way.PathCalculator;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.bojan.RoutePath;
import com.djulb.way.osrm.OsrmBackendApi;
import com.djulb.way.osrm.model.Step;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
//@EnableScheduling
public class PathBank {

    OsrmBackendApi osrmBackendApi;

    private final RoutePath routePath;

    private AtomicReference<List<Double>> point = new AtomicReference();

    public AtomicReference<List<Double>> getPoint() {
        return point;
    }

    public PathBank(OsrmBackendApi osrmBackendApi) {
        this.osrmBackendApi = osrmBackendApi;

        Coordinate start = Coordinate.builder().lng(13.4050).lat(52.5200).build();
        Coordinate end = Coordinate.builder().lng(13.29547681726967).lat(52.50546582848033).build();

        RoutePath routePath = osrmBackendApi.getRoute(start, end);
        this.routePath = routePath;
    }

    public List<Object[]> getPathArray() {
        List<Object[]> sss = new ArrayList<>();

        for (Step step : routePath.getWaypoint().getRoutes().get(0).getLegs().get(0).getSteps()) {
            double lat = step.getManeuver().getLat();
            double lng = step.getManeuver().getLng();
            List<Double> q = new ArrayList<>();
            q.add(lat);
            q.add(lng);
            sss.add(q.toArray());
        }
        return sss;
    }

}
