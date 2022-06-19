package com.djulb.db.redis;

import com.djulb.way.elements.TaxiGps;
import com.djulb.way.elements.redis.TaxiRedisGps;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("redisTaxiRepository")
public interface RedisTaxiRepository extends CrudRepository<TaxiRedisGps, String> {}
