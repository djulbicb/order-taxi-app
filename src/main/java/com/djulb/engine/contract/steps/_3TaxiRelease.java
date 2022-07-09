package com.djulb.engine.contract.steps;

import com.djulb.db.elastic.dto.EGps;
import com.djulb.engine.contract.ContractFactory;
import com.djulb.common.objects.ObjectActivity;
import com.djulb.common.objects.ObjectStatus;
import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;

import java.util.List;

import static com.djulb.common.objects.GpsConvertor.toGps;
import static com.djulb.db.elastic.ElasticConvertor.objToElastic;
import static com.djulb.db.kafka.KafkaCommon.TOPIC_GPS_PASSENGER;
import static com.djulb.db.kafka.KafkaCommon.TOPIC_GPS_TAXI;

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

        contractFactory.getKafkaTaxiTemplate().send(TOPIC_GPS_TAXI, taxi.getId(), toGps(taxi));
        contractFactory.getKafkaPassangerTemplate().send(TOPIC_GPS_PASSENGER, taxi.getId(), toGps(passanger));

//        EGps taxiGps = objToElastic(taxi);
//        EGps passangerGps = objToElastic(passanger);

        // contractFactory.getElasticSearchRepository().saveAll(List.of(taxiGps, passangerGps));

    }
}
