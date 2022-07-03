package com.djulb.engine.messanger;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoNotificationRepository {
    @Qualifier("mongoMessageDb")
    private final MongoTemplate mongoMessageDb;
}
