package com.djulb.way.osrm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Step {
    @JsonProperty("geometry")
    String name;
    @JsonProperty("duration")
    Double duration;
    @JsonProperty("distance")
    Double distance;
    @JsonProperty("maneuver")
    Maneuver maneuver;
    @JsonProperty("destination")
    String destination;
    @JsonProperty("mode")
    String mode;
    @JsonProperty("intersections")
    List<Intersection> intersections;


}
