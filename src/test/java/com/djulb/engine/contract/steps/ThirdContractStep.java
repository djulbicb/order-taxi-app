package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.ContractFactory;

public class ThirdContractStep extends AbstractContractStep{
    public ThirdContractStep(ContractFactory contractFactory) {
        super(contractFactory);
    }

    @Override
    public void process() {
        setStatusFinished();
    }
}
