package com.djulb.db.redis;

import com.djulb.messages.redis.RedisNotification;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedissonMapCacheRepository {
    private final RedissonClient redissonClient;

    private final static int EXPIRE_TIME = 1;
    private final static TimeUnit EXPIRE_UNIT = TimeUnit.MINUTES;

    public void put(String mapCacheName, RedisNotification notification) {
        redissonClient.getMapCache(mapCacheName).put(notification.getTimestamp(), notification, EXPIRE_TIME, EXPIRE_UNIT);
    }


    public Collection<RedisNotification> get(String mapCacheName) {
        RMapCache<String, RedisNotification> mapCache = redissonClient.getMapCache(mapCacheName);
        return mapCache.values().stream().sorted((Comparator.comparing(RedisNotification::getTimestamp))).collect(Collectors.toList());
    }
}
