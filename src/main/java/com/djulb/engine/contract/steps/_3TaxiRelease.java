package com.djulb.engine.contract.steps;

import com.djulb.db.elastic.dto.EGps;
import com.djulb.engine.contract.ContractFactory;
import com.djulb.way.elements.ObjectActivity;
import com.djulb.way.elements.ObjectStatus;
import com.djulb.way.elements.Passanger;
import com.djulb.way.elements.Taxi;

import java.util.List;

import static com.djulb.db.elastic.ElasticConvertor.objToElastic;

public class _3TaxiRelease extends AbstractContractStep {
    private final Passanger passanger;
    private final Taxi taxi;

    public _3TaxiRelease(ContractFactory contractFactory,
                         Passanger passanger,
                         Taxi taxi) {
        super(contractFactory);
        this.passanger = passanger;
        this.taxi = taxi;
    }

    @Override
    public void process() {
        taxi.setStatus(ObjectStatus.IDLE);
        passanger.setActivity(ObjectActivity.DEACTIVATED);

        EGps taxiGps = objToElastic(taxi);
        EGps passangerGps = objToElastic(passanger);

        contractFactory.getElasticSearchRepository().saveAll(List.of(taxiGps, passangerGps));
    }
}
