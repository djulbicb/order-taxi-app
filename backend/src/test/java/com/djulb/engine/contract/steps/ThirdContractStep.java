package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.ContractHelper;

public class ThirdContractStep extends AbstractContractStep{
    public ThirdContractStep(ContractHelper contractHelper) {
        super(contractHelper);
    }

    @Override
    public void process() {
        setStatusFinished();
    }
}
