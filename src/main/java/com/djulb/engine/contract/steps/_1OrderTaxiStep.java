package com.djulb.engine.contract.steps;

import com.djulb.common.objects.ObjectActivity;
import com.djulb.engine.contract.ContractFactory;
import com.djulb.publishers.contracts.model.ContractM;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.djulb.OrderTaxiAppSettings.MOVE_INCREMENT;
import static com.djulb.db.elastic.ElasticConvertor.objToElastic;
import static com.djulb.common.objects.GpsConvertor.toGps;
import static com.djulb.db.kafka.KafkaCommon.*;

public class _1OrderTaxiStep extends AbstractContractStep{
    private final Passanger passanger;
    private final String contractId;
    private Taxi taxi;
    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();

    public _1OrderTaxiStep(ContractFactory contractFactory, String contractId, Passanger passanger) {
        super(contractFactory);
        this.passanger = passanger;
        this.contractId = contractId;

        List<GpsUi> taxisInArea = contractFactory.getElasticSearchRepositoryCustomImpl().getAvailableTaxisInArea(passanger.getCurrentPosition(), 100.0, "km");
        List<String> taxiIds = taxisInArea.stream().map(redisGps -> redisGps.getId()).collect(Collectors.toList());
        Optional<Taxi> taxi1 = contractFactory.getEngineManager().getTaxiByIds(taxiIds);
        if (taxi1.isPresent()) {
            if (taxi1.isPresent()) {

                taxi = taxi1.get();
                taxi.setStatus(ObjectStatus.IN_PROCESS);
                passanger.setStatus(ObjectStatus.IN_PROCESS);

                contractFactory.getKafkaTaxiTemplate().send(TOPIC_GPS_TAXI, taxi.getId(), toGps(taxi));
                contractFactory.getKafkaPassangerTemplate().send(TOPIC_GPS_PASSENGER, taxi.getId(), toGps(passanger));
//                contractFactory.getElasticSearchRepository().save(objToElastic(taxi));

                contractFactory.getNotificationService().orderedTaxi(passanger, taxi);

                Coordinate start = Coordinate.builder().lng(taxi.getCurrentPosition().getLng()).lat(taxi.getCurrentPosition().getLat()).build();
                Coordinate end = Coordinate.builder().lng(passanger.getCurrentPosition().getLng()).lat(passanger.getCurrentPosition().getLat()).build();

                try {
                    RoutePath routePath = contractFactory.getOsrmBackendApi().getRoute(start, end);

                    for (Step step : routePath.getWaypoint().getRoutes().get(0).getLegs().get(0).getSteps()) {
                        List<Intersection> intersections = step.getIntersections();
                        for (Intersection intersection : intersections) {
                            double lat = intersection.getLocation().get(1);
                            double lng = intersection.getLocation().get(0);
                            x.add(lat);
                            y.add(lng);
                        }
                    }

                    ContractM contractM = ContractM.builder()
                            ._id(contractId)
                            .passangerId(passanger.getId())
                            .taxiId(taxi.getId())
                            .passangerStartPosition(passanger.getCurrentPosition())
                            .pathTaxiToPassanger(routePath.getPathArray())
                            .activity(ObjectActivity.ACTIVE)
                            .build();
                    contractFactory.getKafkaContractTemplate().send(TOPIC_CONTRACT, contractId, contractM);

                } catch (WebClientResponseException e) {
                    System.out.println("error");
                }

            }
        }else {

            contractFactory.getNotificationService().passangerDidntGetTaxi(passanger);

            setStatusFinished();
            _0HoldStep step = new _0HoldStep(contractFactory, contractId, passanger, Duration.ofSeconds(10));
            addNext(step);
        }

    }

//    private Taxi toGps(GpsUi gpsUi) {
//        return Taxi.builder()
//                .currentPosition(gpsUi.getCoordinate())
//                .id(gpsUi.getId())
//                .build();
//    }

    private int distance = MOVE_INCREMENT;
    @Override
    public void process() {
        try {
            distance += MOVE_INCREMENT;
            Coordinate position = PathCalculator.findCoordinateAtPathPosition(x.toArray(new Double[0]), y.toArray(new Double[0]), distance);

            if (position.isZero()) {
                setStatusFinished();
                addNext(new _2TaxiAndDriveToGoal(contractFactory, contractId, this.passanger, this.taxi));
            } else {
                taxi.setCurrentPosition(position);
            }

        }
        catch (NullPointerException e) {
            // System.out.println("Null");
        }
    }
}
