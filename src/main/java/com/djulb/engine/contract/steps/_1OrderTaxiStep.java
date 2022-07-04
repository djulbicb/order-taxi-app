package com.djulb.engine.contract.steps;

import com.djulb.db.elastic.dto.EGps;
import com.djulb.engine.contract.ContractFactory;
import com.djulb.utils.PathCalculator;
import com.djulb.common.coord.Coordinate;
import com.djulb.common.paths.RoutePath;
import com.djulb.common.objects.ObjectStatus;
import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.messages.redis.RedisGps;
import com.djulb.osrm.model.Intersection;
import com.djulb.osrm.model.Step;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.djulb.OrderTaxiAppSettings.MOVE_INCREMENT;
import static com.djulb.db.elastic.ElasticConvertor.objToElastic;
import static com.djulb.common.objects.GpsConvertor.toGps;

public class _1OrderTaxiStep extends AbstractContractStep{
    private final Passanger passanger;
    private Taxi taxi;
    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();

    public _1OrderTaxiStep(ContractFactory contractFactory, Passanger passanger) {
        super(contractFactory);
        this.passanger = passanger;

        List<RedisGps> taxisInArea = contractFactory.getFoodPOIRepositoryCustom().getAvailableTaxisInArea(passanger.getCurrentPosition(), 100.0, "km");
        if (taxisInArea.size() > 0) {
            List<String> taxiIds = taxisInArea.stream().map(redisGps -> redisGps.getId()).collect(Collectors.toList());
            Optional<Taxi> taxi1 = contractFactory.getEngineManager().getTaxiByIds(taxiIds);
            if (taxi1.isPresent()) {
                taxi = taxi1.get();
                taxi.setStatus(ObjectStatus.IN_PROCESS);

                contractFactory.getElasticSearchRepository().save(objToElastic(taxi));

                Coordinate start = Coordinate.builder().lng(taxi.getCurrentPosition().getLng()).lat(taxi.getCurrentPosition().getLat()).build();
                Coordinate end = Coordinate.builder().lng(passanger.getCurrentPosition().getLng()).lat(passanger.getCurrentPosition().getLat()).build();

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
            }
        }else {
            setStatusFinished();
            _0HoldStep step = new _0HoldStep(contractFactory,passanger, Duration.ofSeconds(10));
            addNext(step);
        }

    }

    private Taxi toGps(RedisGps redisGps) {
        return Taxi.builder()
                .currentPosition(redisGps.getCoordinate())
                .id(redisGps.getId())
                .build();
    }

    private int distance = MOVE_INCREMENT;
    @Override
    public void process() {
        try {
            distance += MOVE_INCREMENT;
            Coordinate position = PathCalculator.findCoordinateAtPathPosition(x.toArray(new Double[0]), y.toArray(new Double[0]), distance);

            if (position.isZero()) {
                setStatusFinished();
                addNext(new _2TaxiAndDriveToGoal(contractFactory, this.passanger, this.taxi));
            } else {
                taxi.setCurrentPosition(position);
            }

        }
        catch (NullPointerException e) {
            // System.out.println("Null");
        }
    }
}
