package com.djulb.engine.contract.steps;

import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.db.redis.RedissonMapCacheRepository;
import com.djulb.messages.redis.RedisNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RNotificationService {

    private final RedissonMapCacheRepository mapCacheRepository;

    public void passangerWaits(Passanger passanger) {
        String message = String.format("Passanger %s is waiting.", passanger.getId());
        RedisNotification notification = RedisNotification.build(passanger.getId(), message);
        mapCacheRepository.put(passanger.getId(), notification);
    }

    public void passangerIdleTimeStopped(Passanger passanger) {
        String message = String.format("Passanger %s idle time stopped.", passanger.getId());
        RedisNotification notification = RedisNotification.build(passanger.getId(), message);
        mapCacheRepository.put(passanger.getId(), notification);
    }


    public void passangerIsOrderingTaxiNow(Passanger passanger) {
        String message = String.format("Passanger %s is ordering taxi now.", passanger.getId());
        RedisNotification notification = RedisNotification.build(passanger.getId(), message);
        mapCacheRepository.put(passanger.getId(), notification);
    }

    public void orderedTaxi(Passanger passanger, Taxi taxi) {
        String message = String.format("Passanger %s ordered taxi %s.", passanger.getId(), taxi.getId());

        RedisNotification notification = RedisNotification.build(passanger.getId(), message);
        mapCacheRepository.put(passanger.getId(), notification);
        mapCacheRepository.put(taxi.getId(), notification);
    }

    public void taxiAndPassangerArrived(Passanger passanger, Taxi taxi) {
        String message = String.format("Passanger %s and taxi %s arrived at destination.", passanger.getId(), taxi.getId());

        RedisNotification notification = RedisNotification.build(passanger.getId(), message);
        mapCacheRepository.put(passanger.getId(), notification);
        mapCacheRepository.put(taxi.getId(), notification);
    }

    public void passangerDidntGetTaxi(Passanger passanger) {
        String message = String.format("Passanger %s didnt get taxi.", passanger.getId());

        RedisNotification notification = RedisNotification.build(passanger.getId(), message);
        mapCacheRepository.put(passanger.getId(), notification);
    }

    public void passangerAndTaxiStarted(Passanger passanger, Taxi taxi) {
        String message = String.format("Passanger %s and taxi %s started.", passanger.getId(), taxi.getId());

        RedisNotification notification = RedisNotification.build(passanger.getId(), message);
        mapCacheRepository.put(passanger.getId(), notification);
        mapCacheRepository.put(taxi.getId(), notification);
    }
}
