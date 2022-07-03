package com.djulb.messages.redis;

import com.djulb.common.coord.Coordinate;
import com.djulb.common.objects.ObjectActivity;
import com.djulb.common.objects.ObjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisGps {

    private String id;
    private ObjectType status;
    private ObjectActivity activity;

    private Coordinate coordinate;

    Date timestamp;
}
