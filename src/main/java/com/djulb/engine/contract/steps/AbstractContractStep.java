package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.ContractFactory;

public abstract class AbstractContractStep {
    protected final ContractFactory contractFactory;
    protected AbstractContractStep next;

    public AbstractContractStep(
            ContractFactory contractFactory
    ) {
        this.contractFactory = contractFactory;
    }

    protected void addNext(AbstractContractStep step) {
        this.next = step;
    }
    private Status status = Status.IN_PROGRESS;

    public boolean hasNext() {
        return this.next != null;
    }

    public AbstractContractStep getNext() {
        return next;
    }

    enum Status {
         IN_PROGRESS, FINISHED
    }
    public void start() {

    };
    public void process() {

    };
    public void end() {

    };



    void setStatusInProgress() {
        this.status = Status.IN_PROGRESS;
    }
    void setStatusFinished() {
        this.status = Status.FINISHED;
    }

    boolean isFinished() {
        return this.status == Status.FINISHED;
    };
}
