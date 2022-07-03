package com.djulb.osrm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Intersection {
    @JsonProperty("out")
    Integer out;
    @JsonProperty("in")
    Integer in;
    @JsonProperty("entry")
    List<Boolean> entry;
    @JsonProperty("bearings")
    List<Integer> bearings;
    @JsonProperty("location")
    List<Double> location;
}
