package com.djulb.db.kafka.model;

import com.djulb.publishers.notifications.NotificationR;
import com.djulb.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationK {
    private String id;
    private String message;
    private long timestamp;

    public static NotificationK build(String id, String started, Long timestamp) {
        return NotificationK.builder().id(id).message(started).timestamp(timestamp).build();
    }
    public static NotificationK build(String id, String started) {
        return NotificationK.builder().id(id).message(started).timestamp(TimeUtils.getNowEpoch()).build();
    }
}
