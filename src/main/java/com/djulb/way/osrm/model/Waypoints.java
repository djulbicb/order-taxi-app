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
    "hint",
    "distance",
    "name",
    "location"
})
@Generated("jsonschema2pojo")
public class Waypoints {

    @JsonProperty("hint")
    private String hint;
    @JsonProperty("distance")
    private Double distance;
    @JsonProperty("name")
    private String name;
    @JsonProperty("location")
    private List<Double> location = new ArrayList<Double>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonIgnore
    private Double latitude;
    @JsonIgnore
    private Double longitude;
    @JsonProperty("hint")
    public String getHint() {
        return hint;
    }

    @JsonProperty("hint")
    public void setHint(String hint) {
        this.hint = hint;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public String formattedCoordinates() {
        return String.format("%s,%s", getLatitude(), getLongitude());
    }

    public Waypoints withHint(String hint) {
        this.hint = hint;
        return this;
    }

    @JsonProperty("distance")
    public Double getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Waypoints withDistance(Double distance) {
        this.distance = distance;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Waypoints withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("location")
    public List<Double> getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(List<Double> location) {
        this.location = location;
        this.latitude = location.get(1);
        this.longitude = location.get(0);
    }

    public Waypoints withLocation(List<Double> location) {
        this.location = location;
        this.latitude = location.get(1);
        this.longitude = location.get(0);
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

    public Waypoints withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Waypoints.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("hint");
        sb.append('=');
        sb.append(((this.hint == null)?"<null>":this.hint));
        sb.append(',');
        sb.append("distance");
        sb.append('=');
        sb.append(((this.distance == null)?"<null>":this.distance));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("location");
        sb.append('=');
        sb.append(((this.location == null)?"<null>":this.location));
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
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.location == null)? 0 :this.location.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.distance == null)? 0 :this.distance.hashCode()));
        result = ((result* 31)+((this.hint == null)? 0 :this.hint.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Waypoints) == false) {
            return false;
        }
        Waypoints rhs = ((Waypoints) other);
        return ((((((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.location == rhs.location)||((this.location!= null)&&this.location.equals(rhs.location))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.distance == rhs.distance)||((this.distance!= null)&&this.distance.equals(rhs.distance))))&&((this.hint == rhs.hint)||((this.hint!= null)&&this.hint.equals(rhs.hint))));
    }

}
