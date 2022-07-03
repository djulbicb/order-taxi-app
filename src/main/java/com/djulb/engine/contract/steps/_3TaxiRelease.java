package com.djulb.engine.contract.steps;

import com.djulb.db.elastic.ElasticGps;
import com.djulb.engine.contract.ContractFactory;
import com.djulb.way.elements.Passanger;
import com.djulb.way.elements.Taxi;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

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
        taxi.setStatus(Taxi.Status.IDLE);
        ElasticGps gps = ElasticGps.builder()
                .id(taxi.getId())
                .status(Taxi.Status.IDLE)
                .type(ElasticGps.Type.TAXI)
                .location(new GeoPoint(taxi.getCurrentPosition().getLat(), taxi.getCurrentPosition().getLng()))
                .build();
        contractFactory.getFoodPOIRepository().save(gps);
    }
}
