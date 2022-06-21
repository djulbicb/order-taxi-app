package com.djulb.way.elements.redis;

import com.djulb.way.bojan.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Data
@Builder
@RedisHash("redis")
@NoArgsConstructor
@AllArgsConstructor
public class RedisTrue {
    public enum Status {
        TAXI, PASSANGER
    }
    private String id;
    private Status status;

    private Coordinate coordinate;

    Date timestamp;
}
