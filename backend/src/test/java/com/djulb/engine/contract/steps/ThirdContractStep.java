package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.BehaviorHelper;

public class ThirdContractStep extends Behavior {
    public ThirdContractStep(BehaviorHelper behaviorHelper) {
        super(behaviorHelper);
    }

    @Override
    public void process() {
        setStatusFinished();
    }
}
