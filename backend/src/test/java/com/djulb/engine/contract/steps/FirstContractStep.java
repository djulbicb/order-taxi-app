package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.BehaviorHelper;

public class FirstContractStep extends Behavior {
    public FirstContractStep(BehaviorHelper behaviorHelper) {
        super(behaviorHelper);
    }

    @Override
    public void process() {
        setStatusFinished();
    }
}
