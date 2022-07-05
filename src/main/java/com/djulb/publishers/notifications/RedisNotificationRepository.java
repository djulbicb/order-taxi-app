package com.djulb.publishers.notifications;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisNotificationRepository extends CrudRepository<RedisNotification, String> {}
