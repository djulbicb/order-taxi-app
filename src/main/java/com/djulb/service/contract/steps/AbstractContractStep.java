package com.djulb.service.contract.steps;

public abstract class AbstractContractStep {
    private Status status = Status.IN_PROGRESS;
    enum Status {
         IN_PROGRESS, FINISHED
    }
    void start() {

    };
    void process() {

    };
    void end() {

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
