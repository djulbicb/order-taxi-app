package com.djulb.db.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${service.redis.url}")
    private String serviceRedisUrl;
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
//        int redisPort = Integer.parseInt(serviceRedisUrl.substring(serviceRedisUrl.length() - 4));
//        String redisUrl = serviceRedisUrl.substring(0, serviceRedisUrl.length() - 4);

        int redisPort = Integer.parseInt(serviceRedisUrl.split(":")[2]);
        String redisUrl = serviceRedisUrl.split(":")[1].replaceAll("//", "");

        System.out.println(redisPort);
        System.out.println(redisUrl);

        return new LettuceConnectionFactory(redisUrl, redisPort);
    }

    @Bean
    public RedisTemplate redisTemplate(){
        RedisTemplate empTemplate = new RedisTemplate<>();
        empTemplate.setConnectionFactory(redisConnectionFactory());
        return empTemplate;
    }
}
