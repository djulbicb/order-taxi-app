package com.djulb;

import com.djulb.way.Step;
import com.djulb.way.Waypoint;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.bojan.Path;
import com.djulb.way.bojan.PathBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@EnableScheduling
public class PathBank {
    private final Waypoint block;
    private final Path path;

    private AtomicReference<List<Double>> point = new AtomicReference();

    public AtomicReference<List<Double>> getPoint() {
        return point;
    }

    public PathBank() {
        WebClient client = WebClient.create();
        String url = "http://localhost:5000/route/v1";
        url = "http://router.project-osrm.org/route/v1/driving/13.4050,52.5200;13.29547681726967,52.50546582848033?steps=true&overview=full";
        WebClient.ResponseSpec responseSpec = client.get()
                .uri(url)
                .retrieve();
        Mono<Waypoint> mono = responseSpec.bodyToMono(Waypoint.class);
        Waypoint block = mono.block();

        this.block = block;
        // Fake car
        PathBuilder pathBuilder = new PathBuilder();
        for (Step step : block.getRoutes().get(0).getLegs().get(0).getSteps()) {
            double lat = step.getManeuver().getLat();
            double lng = step.getManeuver().getLng();

            pathBuilder.addCoordinate(lat, lng);
        }
        pathBuilder.addCoordinate(52.50546582848033, 13.29547681726967);
        Path path = pathBuilder.build();
        this.path = path;
    }

    double dist = 100;
    @Scheduled(fixedDelay=1000)
    public void test() {
        try {
            Coordinate coordinateAtDistance = path.getCoordinateAtDistance(dist);
            point.set(List.of(coordinateAtDistance.getLat(), coordinateAtDistance.getLng()));
            dist+=100;
            System.out.println(point);
            System.out.println(dist);
        }
        catch (NullPointerException e) {
            System.out.println("Null");
        }
    }

    public List<Object[]> getPathArray() {
        List<Object[]> sss = new ArrayList<>();

        for (Step step : block.getRoutes().get(0).getLegs().get(0).getSteps()) {
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
