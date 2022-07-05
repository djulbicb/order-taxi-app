package com.djulb.messages.redis;

import com.djulb.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.PackagePrivate;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
//https://programmer.group/example-of-using-redis-in-spring-boot.html
@Data
@Builder
@RedisHash(value = "notifications",timeToLive = 10)
@NoArgsConstructor
@PackagePrivate
@AllArgsConstructor
public class RedisNotification implements Serializable {
    private String id;
    private String message;
    private long timestamp;

    public static RedisNotification build(String id, String started) {
        return RedisNotification.builder().id(id).message(started).timestamp(TimeUtils.getNowEpoch()).build();
    }
}
