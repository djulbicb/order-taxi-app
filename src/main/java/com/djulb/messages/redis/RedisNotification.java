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
@RedisHash("notifications")
@NoArgsConstructor
@PackagePrivate
@AllArgsConstructor
public class RedisNotification implements Serializable {
    private String id;
    private String message;
    private long timestamp;

}
