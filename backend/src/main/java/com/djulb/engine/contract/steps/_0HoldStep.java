package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.BehaviorHelper;
import com.djulb.common.objects.Passanger;

import java.time.Duration;
import java.time.Instant;

import static com.djulb.db.kafka.KafkaCommon.TOPIC_NOTIFICATIONS;
import static com.djulb.db.kafka.notifications.NotificationKService.*;


public class _0HoldStep extends Behavior {

    private final Duration threshold;
    private final Passanger passanger;
    private final String contractId;
    private Instant startTime;

    public _0HoldStep(BehaviorHelper behaviorHelper, String contractId, Passanger passanger, Duration threshold) {
        super(behaviorHelper);
        startTime = Instant.now();
        this.threshold = threshold;
        this.passanger = passanger;
        this.contractId = contractId;

        behaviorHelper.getKafkaNotificationTemplate().send(TOPIC_NOTIFICATIONS, passanger.getId(), passangerWaits(passanger.getId(), passanger));
    }

    public static boolean timeHasElapsedSince(Instant then, Duration threshold) {
        return Duration.between(then, Instant.now()).toSeconds() > threshold.toSeconds();
    }

    @Override
    public void process() {
        if (timeHasElapsedSince(startTime, threshold)) {
            setStatusFinished();
            _1OrderTaxiStep step = new _1OrderTaxiStep(behaviorHelper, contractId, passanger);

            behaviorHelper.getKafkaNotificationTemplate().send(TOPIC_NOTIFICATIONS, passanger.getId(), passangerIdleTimeStopped(passanger.getId(), passanger));
            addNext(step);
        }
    }

}
