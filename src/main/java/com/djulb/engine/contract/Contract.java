package com.djulb.engine.contract;

import com.djulb.engine.contract.steps.AbstractContractStep;
import com.djulb.common.objects.Taxi;
import com.djulb.common.objects.Passanger;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class Contract {
    Taxi car;
    Passanger person;
    AbstractContractStep step;

    public Contract(Taxi car, Passanger person, AbstractContractStep step) {
        this.car = car;
        this.person = person;
        this.step = step;
    }

    public AbstractContractStep getActive() {
        AbstractContractStep last = step;
        while (last.hasNext() && last.getStatus() == AbstractContractStep.Status.FINISHED) {
            last = last.getNext();
        }
        return last;
    }
}
