package com.djulb.osrm;

import com.djulb.common.coord.Coordinate;
import com.djulb.common.paths.RoutePath;
import com.djulb.common.paths.RoutePathFactory;
import com.djulb.osrm.model.Waypoint;
import com.djulb.osrm.model.Waypoints;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class OsrmBackendApi {
    WebClient client = WebClient.create();
    public RoutePath getRoute(Coordinate start, Coordinate end) {
        String url = OsrmBackendUrl.getRouteApiUrl(start, end);
        WebClient.ResponseSpec responseSpec = client.get()
                .uri(url)
                .retrieve();
        Mono<Waypoint> mono = responseSpec.bodyToMono(Waypoint.class);
        String json = responseSpec.bodyToMono(String.class).block();
        Waypoint block = mono.block();

        RoutePathFactory builder = new RoutePathFactory();
        RoutePath routePath = builder.buildFromWaypoint(block);
        routePath.setStart(start);
        routePath.setEnd(end);
        return routePath;
    }

    public Optional<Waypoints> getNearest(Coordinate coordinate) {
        WebClient client = WebClient.create();
        String nearestServiceUrl = OsrmBackendUrl.getNearestServiceApiUrl(coordinate.getLat(), coordinate.getLng());

        WebClient.ResponseSpec responseSpec = client.get()
                .uri(nearestServiceUrl)
                .retrieve();
        Waypoint block = responseSpec.bodyToMono(Waypoint.class).block();

        if (block.getWaypoints().size() > 0) {
            return Optional.of(block.getWaypoints().get(0));
        }

        return Optional.empty();
    }
}
