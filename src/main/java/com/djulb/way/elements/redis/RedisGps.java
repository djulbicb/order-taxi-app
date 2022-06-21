package com.djulb.way.elements.redis;

import com.djulb.way.bojan.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.GeoIndexed;

import java.util.Date;

@Data
@Builder
@RedisHash(value = "GPS", timeToLive = 60)
@NoArgsConstructor
@AllArgsConstructor
public class RedisGps {
    public enum Status {
        TAXI, PASSANGER
    }
    @Id
    private String id;
    private Status status;
    @GeoIndexed
    private Point coordinate;

    Date timestamp;
}
