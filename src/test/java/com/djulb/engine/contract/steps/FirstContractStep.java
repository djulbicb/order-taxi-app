package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.ContractHelper;

public class FirstContractStep extends AbstractContractStep{
    public FirstContractStep(ContractHelper contractHelper) {
        super(contractHelper);
    }

    @Override
    public void process() {
        setStatusFinished();
    }
}
