package com.djulb.db.kafka.notifications;

import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.db.kafka.model.NotificationK;


public class NotificationKService {
    public static NotificationK passangerIdleTimeStopped(String id, Passanger passanger) {
        String message = String.format("Passanger %s idle time stopped.", passanger.getId());
        return NotificationK.build(id, message);
    }
    public static NotificationK passangerIsOrderingTaxiNow(String id, Passanger passanger) {
        String message = String.format("Passanger %s is ordering taxi now.", passanger.getId());
        return NotificationK.build(id, message);
    }
    public static NotificationK taxiAndPassangerArrived(String id, Passanger passanger, Taxi taxi) {
        String message = String.format("Passanger %s and taxi %s arrived at destination.", passanger.getId(), taxi.getId());
        return NotificationK.build(id, message);
    }
    public static NotificationK passangerDidntGetTaxi(String id, Passanger passanger) {
        String message = String.format("Passanger %s didnt get taxi.", passanger.getId());
        return NotificationK.build(id, message);
    }
    public static NotificationK passangerAndTaxiStarted(String id, Passanger passanger, Taxi taxi) {
        String message = String.format("Passanger %s and taxi %s started.", passanger.getId(), taxi.getId());
        return NotificationK.build(id, message);
    }
    public static NotificationK orderedTaxi(String id, Passanger passanger, Taxi taxi) {
        String message = String.format("Passanger %s ordered taxi %s.", passanger.getId(), taxi.getId());
        return NotificationK.build(id, message);
    }
    public static NotificationK passangerWaits(String id, Passanger passanger) {
        String message = String.format("Passanger %s is waiting.", passanger.getId());
        return NotificationK.build(id, message);
    }
}
