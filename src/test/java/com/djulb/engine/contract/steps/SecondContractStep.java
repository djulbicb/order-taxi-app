package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.ContractFactory;

public class SecondContractStep extends AbstractContractStep{
    public SecondContractStep(ContractFactory contractFactory) {
        super(contractFactory);
    }

    @Override
    public void process() {
        setStatusFinished();
    }
}
