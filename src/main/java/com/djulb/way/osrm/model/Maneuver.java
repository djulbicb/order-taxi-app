package com.djulb.way.osrm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Maneuver {
    @JsonProperty("bearing_after")
    private String bearingAfter;
    @JsonProperty("bearing_before")
    private String bearingBefore;
    @JsonProperty("type")
    private String type;
    @JsonProperty("location")
    private List<Double> locations;

    public double getLng() {
        return locations.get(0);
    }
    public double getLat() {
        return locations.get(1);
    }
}
