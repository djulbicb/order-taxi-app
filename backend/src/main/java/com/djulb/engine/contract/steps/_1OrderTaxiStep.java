package com.djulb.engine.contract.steps;

import com.djulb.common.objects.ObjectActivity;
import com.djulb.engine.EngineManagerStatistics;
import com.djulb.engine.contract.ContractHelper;
import com.djulb.publishers.contracts.model.KMContract;
import com.djulb.utils.PathCalculator;
import com.djulb.common.coord.Coordinate;
import com.djulb.common.paths.RoutePath;
import com.djulb.common.objects.ObjectStatus;
import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.ui.model.GpsUi;
import com.djulb.osrm.model.Intersection;
import com.djulb.osrm.model.Step;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.djulb.OrderTaxiAppSettings.MOVE_INCREMENT;
import static com.djulb.db.elastic.ElasticConvertor.objToElastic;
import static com.djulb.common.objects.GpsConvertor.toGps;
import static com.djulb.db.kafka.KafkaCommon.*;
import static com.djulb.db.kafka.notifications.NotificationKService.*;

public class _1OrderTaxiStep extends AbstractContractStep{
    private final Passanger passanger;
    private final String contractId;
    private Taxi taxi;
    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();
    Instant taxiStartInstant;
    public _1OrderTaxiStep(ContractHelper contractHelper, String contractId, Passanger passanger) {
        super(contractHelper);
        this.passanger = passanger;
        this.contractId = contractId;

        List<GpsUi> taxisInArea = contractHelper.getElasticSearchRepositoryCustomImpl().getAvailableTaxisInArea(passanger.getCurrentPosition(), 100.0, "km");
        List<String> taxiIds = taxisInArea.stream().map(redisGps -> redisGps.getId()).collect(Collectors.toList());
        Optional<Taxi> taxi1 = contractHelper.getEngineManager().getTaxiByIds(taxiIds);
        if (taxi1.isPresent()) {
            if (taxi1.isPresent()) {
                taxiStartInstant = Instant.now();
                taxi = taxi1.get();
                taxi.setStatus(ObjectStatus.IN_PROCESS);
                passanger.setStatus(ObjectStatus.IN_PROCESS);

                EngineManagerStatistics.passangerIdleToWaiting();
                EngineManagerStatistics.taxiIdleToInProcess();

                contractHelper.getKafkaTaxiTemplate().send(TOPIC_GPS_TAXI, taxi.getId(), toGps(taxi));
                contractHelper.getKafkaPassangerTemplate().send(TOPIC_GPS_PASSENGER, passanger.getId(), toGps(passanger));
                contractHelper.getKafkaNotificationTemplate().send(TOPIC_NOTIFICATIONS, taxi.getId(), orderedTaxi(passanger.getId(), passanger, taxi));
                contractHelper.getKafkaNotificationTemplate().send(TOPIC_NOTIFICATIONS, passanger.getId(), orderedTaxi(taxi.getId(), passanger, taxi));

                Coordinate start = Coordinate.builder().lng(taxi.getCurrentPosition().getLng()).lat(taxi.getCurrentPosition().getLat()).build();
                Coordinate end = Coordinate.builder().lng(passanger.getCurrentPosition().getLng()).lat(passanger.getCurrentPosition().getLat()).build();

                try {
                    RoutePath routePath = contractHelper.getOsrmBackendApi().getRoute(start, end);

                    EngineManagerStatistics.taxiRouteCalculate(routePath.getTotalDistance());

                    for (Step step : routePath.getWaypoint().getRoutes().get(0).getLegs().get(0).getSteps()) {
                        List<Intersection> intersections = step.getIntersections();
                        for (Intersection intersection : intersections) {
                            double lat = intersection.getLocation().get(1);
                            double lng = intersection.getLocation().get(0);
                            x.add(lat);
                            y.add(lng);
                        }
                    }

                    KMContract contractM = KMContract.builder()
                            ._id(contractId)
                            .passangerId(passanger.getId())
                            .taxiId(taxi.getId())
                            .passangerStartPosition(passanger.getCurrentPosition())
                            .pathTaxiToPassanger(routePath.getPathArray())
                            .activity(ObjectActivity.ACTIVE)
                            .build();
                    contractHelper.getKafkaContractTemplate().send(TOPIC_CONTRACT, contractId, contractM);

                } catch (WebClientResponseException e) {
                    System.out.println("error");
                }

            }
        }else {
            contractHelper.getKafkaNotificationTemplate().send(TOPIC_NOTIFICATIONS, passanger.getId(), passangerDidntGetTaxi(passanger.getId(), passanger));
            EngineManagerStatistics.passangerRetryIncr();
            setStatusFinished();
            _0HoldStep step = new _0HoldStep(contractHelper, contractId, passanger, Duration.ofSeconds(10));
            addNext(step);
        }

    }


    private int distance = MOVE_INCREMENT;
    @Override
    public void process() {
        try {
            distance += MOVE_INCREMENT;
            Coordinate position = PathCalculator.findCoordinateAtPathPosition(x.toArray(new Double[0]), y.toArray(new Double[0]), distance);

            if (position.isZero()) {
                setStatusFinished();

                EngineManagerStatistics.passangerWaitingToInProcess();
                EngineManagerStatistics.passangerWaitTimeCalculate(Duration.between(taxiStartInstant, Instant.now()).toSeconds());

                addNext(new _2TaxiAndDriveToGoal(contractHelper, contractId, this.passanger, this.taxi));
            } else {
                taxi.setCurrentPosition(position);
            }

        }
        catch (NullPointerException e) {
            // System.out.println("Null");
        }
    }
}