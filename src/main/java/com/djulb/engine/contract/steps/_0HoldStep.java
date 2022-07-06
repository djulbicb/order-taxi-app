package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.ContractFactory;
import com.djulb.common.objects.Passanger;

import java.time.Duration;
import java.time.Instant;


public class _0HoldStep extends AbstractContractStep{

    private final Duration threshold;
    private final Passanger passanger;
    private final String contractId;
    private Instant startTime;

    public _0HoldStep(ContractFactory contractFactory, String contractId, Passanger passanger, Duration threshold) {
        super(contractFactory);
        startTime = Instant.now();
        this.threshold = threshold;
        this.passanger = passanger;
        this.contractId = contractId;

        contractFactory.getNotificationService().passangerWaits(passanger);
    }

    public static boolean timeHasElapsedSince(Instant then, Duration threshold) {
        return Duration.between(then, Instant.now()).toSeconds() > threshold.toSeconds();
    }

    @Override
    public void process() {
        if (timeHasElapsedSince(startTime, threshold)) {
            setStatusFinished();
            _1OrderTaxiStep step = new _1OrderTaxiStep(contractFactory, contractId, passanger);

            contractFactory.getNotificationService().passangerIdleTimeStopped(passanger);
            addNext(step);
        }
    }

}
