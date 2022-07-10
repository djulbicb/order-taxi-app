package com.djulb.publishers.notifications;

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
public class NotificationR implements Serializable {
    private String id;
    private String message;
    private long timestamp;

    public static NotificationR build(String id, String started) {
        return NotificationR.builder().id(id).message(started).timestamp(TimeUtils.getNowEpoch()).build();
    }
}
