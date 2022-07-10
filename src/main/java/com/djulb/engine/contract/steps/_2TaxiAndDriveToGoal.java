package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.ContractHelper;
import com.djulb.publishers.contracts.model.KMContract;
import com.djulb.utils.PathCalculator;
import com.djulb.common.coord.Coordinate;
import com.djulb.common.paths.RoutePath;
import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.osrm.model.Intersection;
import com.djulb.osrm.model.Step;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

import static com.djulb.OrderTaxiAppSettings.MOVE_INCREMENT;
import static com.djulb.db.kafka.KafkaCommon.TOPIC_CONTRACT;
import static com.djulb.db.kafka.KafkaCommon.TOPIC_NOTIFICATIONS;
import static com.djulb.db.kafka.notifications.NotificationKService.*;

public class _2TaxiAndDriveToGoal extends AbstractContractStep {

    private final Taxi taxi;
    private final Passanger passanger;
    private final String contractId;

    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();

    public _2TaxiAndDriveToGoal(ContractHelper contractHelper,
                                String contractId,
                                Passanger passanger,
                                Taxi taxi) {
        super(contractHelper);

        this.taxi = taxi;
        this.passanger = passanger;
        this.contractId = contractId;


        Coordinate start = taxi.getCurrentPosition();
        Coordinate end = passanger.getDestination();



        try {
            RoutePath routePath = contractHelper.getOsrmBackendApi().getRoute(start, end);

            for (Step step : routePath.getWaypoint().getRoutes().get(0).getLegs().get(0).getSteps()) {
                List<Intersection> intersections = step.getIntersections();
                for (Intersection intersection : intersections) {
                    double lat = intersection.getLocation().get(1);
                    double lng = intersection.getLocation().get(0);
                    x.add(lat);
                    y.add(lng);
                }
            }

            contractHelper.getKafkaNotificationTemplate().send(TOPIC_NOTIFICATIONS, passanger.getId(), passangerAndTaxiStarted(passanger.getId(), passanger, taxi));
            contractHelper.getKafkaNotificationTemplate().send(TOPIC_NOTIFICATIONS, taxi.getId(), passangerAndTaxiStarted(taxi.getId(), passanger, taxi));

            KMContract contractM =  KMContract.builder()
                    ._id(contractId)
                    .passangerStartPosition(passanger.getCurrentPosition())
                    .pathTaxiToDestination(routePath.getPathArray())
                    .build();
            contractHelper.getKafkaContractTemplate().send(TOPIC_CONTRACT, contractId, contractM);

        } catch (WebClientResponseException e) {
            System.out.println("error1");
        }


    }
    private int distance = MOVE_INCREMENT;
    @Override
    public void process() {
        try {
            distance += MOVE_INCREMENT;
            // Coordinate coordinateAtDistance = path.getCoordinateAtDistance(dist);
            Coordinate position = PathCalculator.findCoordinateAtPathPosition(x.toArray(new Double[0]), y.toArray(new Double[0]), distance);

            if (position.isZero()) {
                contractHelper.getKafkaNotificationTemplate().send(TOPIC_NOTIFICATIONS, passanger.getId(), taxiAndPassangerArrived(passanger.getId(), passanger, taxi));
                contractHelper.getKafkaNotificationTemplate().send(TOPIC_NOTIFICATIONS, taxi.getId(), taxiAndPassangerArrived(taxi.getId(), passanger, taxi));
                setStatusFinished();
                addNext(new _3TaxiRelease(contractHelper, contractId, passanger, taxi));
            } else {
                taxi.setCurrentPosition(position);
                passanger.setCurrentPosition(position);
            }

        }
        catch (NullPointerException e) {
            // System.out.println("Null");
        }
    }
}
