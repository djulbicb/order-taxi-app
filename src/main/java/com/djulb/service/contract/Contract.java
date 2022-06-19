package com.djulb.service.contract;

import com.djulb.service.contract.steps.AbstractContractStep;
import com.djulb.way.elements.Taxi;
import com.djulb.way.elements.Passanger;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class Contract {
    Taxi car;
    Passanger person;
    List<AbstractContractStep> contractSteps = new LinkedList<>();
}
