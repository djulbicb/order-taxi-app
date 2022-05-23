package com.djulb.way;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Intersection {
    Integer out;
    Integer in;
    List<Boolean> entry;
    List<Integer> bearings;
    List<Double> location;
}
