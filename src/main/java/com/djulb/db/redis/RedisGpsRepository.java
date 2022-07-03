package com.djulb.db.redis;

import com.djulb.messages.redis.RedisNotification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("redisGpsRepository")
public interface RedisGpsRepository extends CrudRepository<RedisNotification, String> {
}
