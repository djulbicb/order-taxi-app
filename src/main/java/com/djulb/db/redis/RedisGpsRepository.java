package com.djulb.db.redis;

import com.djulb.way.elements.redis.RedisGps;
import com.djulb.way.elements.redis.RedisTrue;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("redisGpsRepository")
public interface RedisGpsRepository extends CrudRepository<RedisTrue, String> {
}
