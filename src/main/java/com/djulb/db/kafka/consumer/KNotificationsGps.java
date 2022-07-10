package com.djulb.db.kafka.consumer;

import com.djulb.db.kafka.KafkaCommon;
import com.djulb.db.kafka.model.NotificationK;
import com.djulb.db.redis.RedissonMapCacheRepository;
import com.djulb.publishers.notifications.NotificationR;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.djulb.db.elastic.ElasticConvertor.objToElastic;

@Component
@RequiredArgsConstructor
@Slf4j
public class KNotificationsGps {
    private final RedissonMapCacheRepository mapCacheRepository;

    @KafkaListener(topics = KafkaCommon.TOPIC_NOTIFICATIONS, groupId = "notificationsListener", containerFactory = "kafkaListenerContainerFactoryNotifications")
    public void listenGroupFoo(List<NotificationK> messages) {
        List<NotificationR> notificationRS = new ArrayList<>();
        for (NotificationK value : messages) {
            mapCacheRepository.put(value.getId(), kToR(value));
        }
    }

    private NotificationR kToR(NotificationK value) {
        return NotificationR.builder()
                .timestamp(value.getTimestamp())
                .message(value.getMessage())
                .id(value.getId())
                .build();
    }
}
