package com.djulb.db.redis;

import com.djulb.common.objects.ObjectStatus;
import com.djulb.db.redis.model.RTaxiStatus;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedissonConfig {

    @Value("${service.redis.url}")
    private String serviceRedisUrl;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress(serviceRedisUrl);
        return Redisson.create(config);
    }
}
