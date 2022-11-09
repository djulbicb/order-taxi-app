package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.ContractHelper;

public class SecondContractStep extends AbstractContractStep{
    public SecondContractStep(ContractHelper contractHelper) {
        super(contractHelper);
    }

    @Override
    public void process() {
        setStatusFinished();
    }
}
