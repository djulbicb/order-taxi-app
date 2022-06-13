package com.djulb;

import com.djulb.way.osrm.OsrmBackendUrl;
import com.djulb.way.bojan.DrivingPath;
import com.djulb.way.osrm.model.Step;
import com.djulb.way.osrm.model.Waypoint;
import com.djulb.way.bojan.PathBuilder;
import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WaypointsController {
    @GetMapping("/test")
    public String test () throws IOException {

        double longitudeStart = 13.4050;
        double latitudeStart = 52.5200;
        double longitudeEnd = 13.29547681726967;
        double latitudeEnd = 52.50546582848033;

        WebClient client = WebClient.create();
        String url = OsrmBackendUrl.getRouteApiUrl(longitudeStart, latitudeStart, longitudeEnd, latitudeEnd);
        WebClient.ResponseSpec responseSpec = client.get()
                .uri(url)
                .retrieve();
        Mono<Waypoint> mono = responseSpec.bodyToMono(Waypoint.class);
        Waypoint block = mono.block();

        PathBuilder pathBuilder = new PathBuilder();
        for (Step step : block.getRoutes().get(0).getLegs().get(0).getSteps()) {
            double lat = step.getManeuver().getLat();
            double lng = step.getManeuver().getLng();

            pathBuilder.addCoordinate(lat, lng);
        }
        pathBuilder.addCoordinate(52.50546582848033, 13.29547681726967);
        DrivingPath drivingPath = pathBuilder.build();

        return "sss";
    }

    @Autowired
    PathBank pathBank;
    @GetMapping("/waypoints")
    public List<Object[]> getWaypoints () throws IOException {
        return pathBank.getPathArray();
    }

    @GetMapping("/point")
    public List<Double> point () throws IOException {
        return pathBank.getPoint().get();
    }


    public void convertJsonToJavaClass(URL inputJsonUrl, File outputJavaClassDirectory, String packageName, String javaClassName)
            throws IOException {
        // convertJsonToJavaClass(new URL(url), new File("/home/sss/Documents/dev/workspace/order-taxi-app (copy)/src/main/java/com/djulb/way"), "", "com.djulb.way.osrm.model.Waypoint");
        JCodeModel jcodeModel = new JCodeModel();

        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() {
                return true;
            }

            @Override
            public SourceType getSourceType() {
                return SourceType.JSON;
            }
        };

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(jcodeModel, javaClassName, packageName, inputJsonUrl);

        jcodeModel.build(outputJavaClassDirectory);
    }
}
