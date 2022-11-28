package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.BehaviorHelper;

public class SecondContractStep extends Behavior {
    public SecondContractStep(BehaviorHelper behaviorHelper) {
        super(behaviorHelper);
    }

    @Override
    public void process() {
        setStatusFinished();
    }
}
