package com.djulb.engine.contract;

import com.djulb.engine.contract.steps.Behavior;
import com.djulb.common.objects.Taxi;
import com.djulb.common.objects.Passanger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contract {
    String id;
    Taxi car;
    Passanger person;
    Behavior step;

    public Contract(String id, Taxi car, Passanger person, Behavior step) {
        this.id = id;
        this.car = car;
        this.person = person;
        this.step = step;
    }

    public Behavior getActive() {
        Behavior last = step;
        while (last.hasNext() && last.getStatus() == Behavior.Status.FINISHED) {
            last = last.getNext();
        }
        return last;
    }
}
