package com.djulb.db.redis;

import com.djulb.way.elements.redis.PassangerRedisGps;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("redisPassangerRepository")
public interface RedisPassangerRepository extends CrudRepository<PassangerRedisGps, String> {}
