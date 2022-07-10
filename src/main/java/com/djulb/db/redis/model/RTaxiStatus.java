package com.djulb.db.redis.model;

import com.djulb.common.objects.ObjectStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("taxiStatus")
@Data
@Builder
public class RTaxiStatus implements Serializable {
    @Id
    private String id;
    private ObjectStatus status;
}
