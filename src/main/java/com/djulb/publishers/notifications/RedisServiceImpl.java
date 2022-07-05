package com.djulb.publishers.notifications;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService<String, RedisNotification> {

    // Get redisTemplate instance in constructor, key(not hashKey) uses String type by default
    private RedisTemplate<String, RedisNotification> redisTemplate;
    // Instantiate the operation object in the constructor through the redisTemplate factory method
    private HashOperations<String, String, RedisNotification> hashOperations;
    private ListOperations<String, RedisNotification> listOperations;
    private ZSetOperations<String, RedisNotification> zSetOperations;
    private SetOperations<String, RedisNotification> setOperations;
    private ValueOperations<String, RedisNotification> valueOperations;

    // IDEA can be injected successfully even though it has errors. After instantiating the operation object, the method can be called directly to operate the Redis database
    @Autowired
    public RedisServiceImpl(RedisTemplate<String, RedisNotification> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.listOperations = redisTemplate.opsForList();
        this.zSetOperations = redisTemplate.opsForZSet();
        this.setOperations = redisTemplate.opsForSet();
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public void hashPut(String key, String hashKey, RedisNotification domain) {
        hashOperations.put(key, hashKey, domain);
    }

    @Override
    public Map<String, RedisNotification> hashFindAll(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public RedisNotification hashGet(String key, String hashKey) {
        return hashOperations.get(key, hashKey);
    }

    @Override
    public void hashRemove(String key, String hashKey) {
        hashOperations.delete(key, hashKey);
    }

    @Override
    public Long listPush(String key, RedisNotification domain) {
        return listOperations.rightPush(key, domain);
    }

    @Override
    public Long listUnshift(String key, RedisNotification domain) {
        return listOperations.leftPush(key, domain);
    }

    @Override
    public List<RedisNotification> listFindAll(String key) {
        if (!redisTemplate.hasKey(key)) {
            return null;
        }
        return listOperations.range(key, 0, listOperations.size(key));
    }

    @Override
    public RedisNotification listLPop(String key) {
        return listOperations.leftPop(key);
    }

    @Override
    public void valuePut(String key, RedisNotification domain) {
        valueOperations.set(key, domain);
    }

    @Override
    public RedisNotification getValue(String key) {
        return valueOperations.get(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean expirse(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }



}
