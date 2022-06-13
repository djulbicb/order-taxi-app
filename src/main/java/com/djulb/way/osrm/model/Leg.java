package com.djulb.way.osrm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "steps",
    "summary",
    "weight",
    "duration",
    "distance"
})
@Generated("jsonschema2pojo")
public class Leg {

    @JsonProperty("steps")
//    private List<Object> steps = new ArrayList<Object>();
    private List<Step> steps = new ArrayList<Step>();
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("weight")
    private Integer weight;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("distance")
    private Integer distance;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("steps")
    public List<Step> getSteps() {
        return steps;
    }

    @JsonProperty("steps")
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Leg withSteps(List<Step> steps) {
        this.steps = steps;
        return this;
    }

    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Leg withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    @JsonProperty("weight")
    public Integer getWeight() {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Leg withWeight(Integer weight) {
        this.weight = weight;
        return this;
    }

    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Leg withDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    @JsonProperty("distance")
    public Integer getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Leg withDistance(Integer distance) {
        this.distance = distance;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Leg withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Leg.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("steps");
        sb.append('=');
        sb.append(((this.steps == null)?"<null>":this.steps));
        sb.append(',');
        sb.append("summary");
        sb.append('=');
        sb.append(((this.summary == null)?"<null>":this.summary));
        sb.append(',');
        sb.append("weight");
        sb.append('=');
        sb.append(((this.weight == null)?"<null>":this.weight));
        sb.append(',');
        sb.append("duration");
        sb.append('=');
        sb.append(((this.duration == null)?"<null>":this.duration));
        sb.append(',');
        sb.append("distance");
        sb.append('=');
        sb.append(((this.distance == null)?"<null>":this.distance));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.summary == null)? 0 :this.summary.hashCode()));
        result = ((result* 31)+((this.duration == null)? 0 :this.duration.hashCode()));
        result = ((result* 31)+((this.distance == null)? 0 :this.distance.hashCode()));
        result = ((result* 31)+((this.weight == null)? 0 :this.weight.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.steps == null)? 0 :this.steps.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Leg) == false) {
            return false;
        }
        Leg rhs = ((Leg) other);
        return (((((((this.summary == rhs.summary)||((this.summary!= null)&&this.summary.equals(rhs.summary)))&&((this.duration == rhs.duration)||((this.duration!= null)&&this.duration.equals(rhs.duration))))&&((this.distance == rhs.distance)||((this.distance!= null)&&this.distance.equals(rhs.distance))))&&((this.weight == rhs.weight)||((this.weight!= null)&&this.weight.equals(rhs.weight))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.steps == rhs.steps)||((this.steps!= null)&&this.steps.equals(rhs.steps))));
    }

}
