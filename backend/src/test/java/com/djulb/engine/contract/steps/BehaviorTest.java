package com.djulb.engine.contract.steps;

import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.engine.contract.Contract;
import com.djulb.engine.contract.BehaviorHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BehaviorTest {

    @Test
    public void test () {
        BehaviorHelper behaviorHelper = null;
        FirstContractStep firstContractStep = new FirstContractStep(behaviorHelper);
        SecondContractStep secondContractStep = new SecondContractStep(behaviorHelper);
        ThirdContractStep thirdContractStep = new ThirdContractStep(behaviorHelper);

        firstContractStep.addNext(secondContractStep, thirdContractStep);

        Contract contract = new Contract("anything", Taxi.builder().build(), Passanger.builder().build(), firstContractStep);

        assertEquals(contract.getActive(), firstContractStep);

        firstContractStep.process();
        assertEquals(contract.getActive(), secondContractStep);

        secondContractStep.process();
        assertEquals(contract.getActive(), thirdContractStep);
    }
}