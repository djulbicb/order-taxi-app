package com.djulb.db.redis.usecase;

import com.djulb.db.redis.RedissonMapCacheRepository;
import com.djulb.messages.redis.RedisNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class GetNotificationsUsecase {

    private final RedissonMapCacheRepository mapCacheRepository;

    @GetMapping("api/notifications/{id}")
    public Collection<RedisNotification> getNotifications(@PathVariable String id) {
        return mapCacheRepository.get(id);
    }
}
