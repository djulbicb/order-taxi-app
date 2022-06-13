package com.djulb.way.osrm;

import com.djulb.way.bojan.Coordinate;
import com.djulb.way.bojan.DrivingPath;
import com.djulb.way.bojan.PathBuilder;
import com.djulb.way.osrm.model.Step;
import com.djulb.way.osrm.model.Waypoint;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class OsrmBackendApi {
    public void getRoute(Coordinate start, Coordinate end) {
//        WebClient client = WebClient.create();
//        String url = "http://localhost:5000/route/v1";
//        url = "http://router.project-osrm.org/route/v1/driving/13.4050,52.5200;13.29547681726967,52.50546582848033?steps=true&overview=full";
//        WebClient.ResponseSpec responseSpec = client.get()
//                .uri(url)
//                .retrieve();
//        Mono<Waypoint> mono = responseSpec.bodyToMono(Waypoint.class);
//        Waypoint block = mono.block();
//
//        this.block = block;
//        // Fake car
//        PathBuilder pathBuilder = new PathBuilder();
//        for (Step step : block.getRoutes().get(0).getLegs().get(0).getSteps()) {
//            double lat = step.getManeuver().getLat();
//            double lng = step.getManeuver().getLng();
//
//            pathBuilder.addCoordinate(lat, lng);
//        }
//        pathBuilder.addCoordinate(52.50546582848033, 13.29547681726967);
//        DrivingPath drivingPath = pathBuilder.build();
    }
}
