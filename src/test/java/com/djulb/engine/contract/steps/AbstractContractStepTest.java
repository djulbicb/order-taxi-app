package com.djulb.engine.contract.steps;

import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.engine.contract.Contract;
import com.djulb.engine.contract.ContractFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractContractStepTest {

    @Test
    public void test () {
        ContractFactory contractFactory = null;
        FirstContractStep firstContractStep = new FirstContractStep(contractFactory);
        SecondContractStep secondContractStep = new SecondContractStep(contractFactory);
        ThirdContractStep thirdContractStep = new ThirdContractStep(contractFactory);

        firstContractStep.addNext(secondContractStep, thirdContractStep);

        Contract contract = new Contract("anything", Taxi.builder().build(), Passanger.builder().build(), firstContractStep);

        assertEquals(contract.getActive(), firstContractStep);

        firstContractStep.process();
        assertEquals(contract.getActive(), secondContractStep);

        secondContractStep.process();
        assertEquals(contract.getActive(), thirdContractStep);
    }
}