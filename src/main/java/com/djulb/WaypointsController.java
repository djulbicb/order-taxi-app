package com.djulb;

import com.djulb.way.Step;
import com.djulb.way.Waypoint;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.bojan.Path;
import com.djulb.way.bojan.PathBuilder;
import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@RestController
public class WaypointsController {
    @GetMapping("/test")
    public String test () throws IOException {

        WebClient client = WebClient.create();
        String url = "http://localhost:5000/route/v1";
        url = "http://router.project-osrm.org/route/v1/driving/13.4050,52.5200;13.29547681726967,52.50546582848033?steps=true&overview=full";
        WebClient.ResponseSpec responseSpec = client.get()
                .uri(url)
                .retrieve();
        Mono<Waypoint> mono = responseSpec.bodyToMono(Waypoint.class);
        Waypoint block = mono.block();

        // http://router.project-osrm.org/route/v1/driving/13.4050,52.5200;13.29547681726967,52.50546582848033?steps=true&overview=full


        PathBuilder pathBuilder = new PathBuilder();
        for (Step step : block.getRoutes().get(0).getLegs().get(0).getSteps()) {
            double lat = step.getManeuver().getLat();
            double lng = step.getManeuver().getLng();

            pathBuilder.addCoordinate(lat, lng);
        }
        pathBuilder.addCoordinate(52.50546582848033, 13.29547681726967);
        Path path = pathBuilder.build();

        Coordinate distance = path.getCoordinateAtDistance(7000);

        return "sss";
    }

    public void convertJsonToJavaClass(URL inputJsonUrl, File outputJavaClassDirectory, String packageName, String javaClassName)
            throws IOException {
        // convertJsonToJavaClass(new URL(url), new File("/home/sss/Documents/dev/workspace/order-taxi-app (copy)/src/main/java/com/djulb/way"), "", "com.djulb.way.Waypoint");
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
