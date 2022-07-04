package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.ContractFactory;

public class FirstContractStep extends AbstractContractStep{
    public FirstContractStep(ContractFactory contractFactory) {
        super(contractFactory);
    }

    @Override
    public void process() {
        setStatusFinished();
    }
}
