package com.djulb.osrm.model;

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
    "code",
    "routes",
    "waypoints"
})
@Generated("jsonschema2pojo")
public class Waypoint {

    @JsonProperty("code")
    private String code;
    @JsonProperty("routes")
    private List<Route> routes = new ArrayList<Route>();
    @JsonProperty("waypoints")
    private List<Waypoints> waypoints = new ArrayList<Waypoints>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    public Waypoint withCode(String code) {
        this.code = code;
        return this;
    }

    @JsonProperty("routes")
    public List<Route> getRoutes() {
        return routes;
    }

    @JsonProperty("routes")
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public Waypoint withRoutes(List<Route> routes) {
        this.routes = routes;
        return this;
    }

    @JsonProperty("waypoints")
    public List<Waypoints> getWaypoints() {
        return waypoints;
    }

    @JsonProperty("waypoints")
    public void setWaypoints(List<Waypoints> waypoints) {
        this.waypoints = waypoints;
    }

    public Waypoint withWaypoints(List<Waypoints> waypoints) {
        this.waypoints = waypoints;
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

    public Waypoint withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Waypoint.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("code");
        sb.append('=');
        sb.append(((this.code == null)?"<null>":this.code));
        sb.append(',');
        sb.append("routes");
        sb.append('=');
        sb.append(((this.routes == null)?"<null>":this.routes));
        sb.append(',');
        sb.append("waypoints");
        sb.append('=');
        sb.append(((this.waypoints == null)?"<null>":this.waypoints));
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
        result = ((result* 31)+((this.routes == null)? 0 :this.routes.hashCode()));
        result = ((result* 31)+((this.code == null)? 0 :this.code.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.waypoints == null)? 0 :this.waypoints.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Waypoint) == false) {
            return false;
        }
        Waypoint rhs = ((Waypoint) other);
        return (((((this.routes == rhs.routes)||((this.routes!= null)&&this.routes.equals(rhs.routes)))&&((this.code == rhs.code)||((this.code!= null)&&this.code.equals(rhs.code))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.waypoints == rhs.waypoints)||((this.waypoints!= null)&&this.waypoints.equals(rhs.waypoints))));
    }

}
