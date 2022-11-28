package com.djulb.engine.contract.steps;

import com.djulb.engine.EngineManagerStatistics;
import com.djulb.engine.contract.BehaviorHelper;
import com.djulb.common.objects.ObjectActivity;
import com.djulb.common.objects.ObjectStatus;
import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.publishers.contracts.model.KMContract;

import static com.djulb.common.objects.GpsConvertor.toGps;
import static com.djulb.db.elastic.ElasticConvertor.objToElastic;
import static com.djulb.db.kafka.KafkaCommon.*;

public class _3TaxiRelease extends Behavior {
    private final Passanger passanger;
    private final Taxi taxi;
    private final String contractId;

    public _3TaxiRelease(BehaviorHelper behaviorHelper,
                         String contractId, Passanger passanger,
                         Taxi taxi) {
        super(behaviorHelper);
        this.passanger = passanger;
        this.taxi = taxi;
        this.contractId = contractId;
    }

    @Override
    public void process() {
        taxi.setStatus(ObjectStatus.IDLE);
        passanger.setActivity(ObjectActivity.DEACTIVATED);

        EngineManagerStatistics.taxiInProcessToIdle();

        behaviorHelper.getKafkaTaxiTemplate().send(TOPIC_GPS_TAXI, taxi.getId(), toGps(taxi));
        behaviorHelper.getKafkaPassangerTemplate().send(TOPIC_GPS_PASSENGER, taxi.getId(), toGps(passanger));

        KMContract contractM =  KMContract.builder()
                ._id(contractId)
                .activity(ObjectActivity.DEACTIVATED)
                .build();
        behaviorHelper.getKafkaContractTemplate().send(TOPIC_CONTRACT, contractId, contractM);

        setToBeRemoved();

        EngineManagerStatistics.decrTotalPassanger();
//        EGps taxiGps = objToElastic(taxi);
//        EGps passangerGps = objToElastic(passanger);

        // contractFactory.getElasticSearchRepository().saveAll(List.of(taxiGps, passangerGps));

    }
}
