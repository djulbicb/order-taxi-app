package com.djulb.engine.contract;

import com.djulb.engine.contract.steps.AbstractContractStep;
import com.djulb.way.elements.Taxi;
import com.djulb.way.elements.Passanger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contract {
    Taxi car;
    Passanger person;
    AbstractContractStep step;


    public AbstractContractStep getActive() {
        AbstractContractStep last = step;
        while (last.hasNext()) {
            last = last.getNext();
        }
        return last;
    }
}
