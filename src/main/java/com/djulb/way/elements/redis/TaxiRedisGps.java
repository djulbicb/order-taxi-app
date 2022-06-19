package com.djulb.way.elements.redis;

import com.djulb.way.bojan.Coordinate;
import com.djulb.way.elements.Taxi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Data
@Builder
@RedisHash(value = "Taxi", timeToLive = 60)
@NoArgsConstructor
@AllArgsConstructor
public class TaxiRedisGps {
    @Id
    private String id;
    private Taxi.Status status;
    private Coordinate coordinate;
    Date timestamp;
}
