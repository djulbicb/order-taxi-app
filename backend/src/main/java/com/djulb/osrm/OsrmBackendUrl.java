package com.djulb.osrm;

import com.djulb.common.coord.Coordinate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OsrmBackendUrl {

    @Value("${service.osrm-backend-berlin.url}")
    private String serviceOsrmBackendUrl;
    private static final String PROFILE = "driving";
    private static final String NEAREST_SERVICE_API = "http://${server}/nearest/v1/${profile}/${coordinates}.json?number=${number}";
    /**
     * Get route coordinates that are near the specified coordinate
     * @param longitude
     * @param latitude
     * @param numberOfPoints number of coordinates to return
     * @return
     */
    public String getNearestServiceApiUrl(double latitude, double longitude) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("server", serviceOsrmBackendUrl);
        data.put("profile", PROFILE);
        data.put("coordinates", StringUtils.join(List.of(longitude, latitude), ","));
        data.put("number", String.valueOf(1));

        return StrSubstitutor.replace(NEAREST_SERVICE_API, data);
    }
    private static final String ROUTE_SERVICE_API = "http://${server}/route/v1/${profile}/${longitudeStart},${latitudeStart};${longitudeEnd},${latitudeEnd}?steps=true&overview=full&geometries=polyline6";
    public String getRouteApiUrl(Coordinate start, Coordinate end) {
        return getRouteApiUrl(start.getLng(), start.getLat(), end.getLng(), end.getLat());
    }
    public String getRouteApiUrl(double longitudeStart, double latitudeStart, double longitudeEnd, double latitudeEnd) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("server", serviceOsrmBackendUrl);
        data.put("profile", PROFILE);
        data.put("longitudeStart", String.valueOf(longitudeStart));
        data.put("latitudeStart", String.valueOf(latitudeStart));
        data.put("longitudeEnd", String.valueOf(longitudeEnd));
        data.put("latitudeEnd", String.valueOf(latitudeEnd));

        return StrSubstitutor.replace(ROUTE_SERVICE_API, data);
    }



}
