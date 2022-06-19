package com.djulb.service.contract.steps;

import java.time.Duration;
import java.time.Instant;

public class _0HoldStep extends AbstractContractStep{

    private Instant startTime;

    public static boolean timeHasElapsedSince(Instant then, Duration threshold) {
        return Duration.between(then, Instant.now()).toSeconds() > threshold.toSeconds();
    }

    @Override
    void start() {
        startTime = Instant.now();
    }

    @Override
    void process() {
        Duration threshold = Duration.ofSeconds(3);
        if (timeHasElapsedSince(startTime, threshold)) {
            setStatusFinished();
        }
    }

    @Override
    void end() {

    }
}
