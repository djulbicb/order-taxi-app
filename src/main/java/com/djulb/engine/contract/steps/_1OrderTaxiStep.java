package com.djulb.engine.contract.steps;

import com.djulb.db.elastic.ElasticGps;
import com.djulb.engine.contract.ContractFactory;
import com.djulb.way.PathCalculator;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.bojan.RoutePath;
import com.djulb.way.elements.ObjectStatus;
import com.djulb.way.elements.ObjectType;
import com.djulb.way.elements.Passanger;
import com.djulb.way.elements.Taxi;
import com.djulb.way.elements.redis.RedisGps;
import com.djulb.osrm.model.Intersection;
import com.djulb.osrm.model.Step;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.djulb.OrderTaxiAppSettings.MOVE_INCREMENT;
import static com.djulb.way.elements.GpsConvertor.toGps;

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
                ElasticGps gps = ElasticGps.builder()
                        .id(taxi.getId())
                        .status(ObjectStatus.IN_PROCESS)
                        .type(ObjectType.TAXI)
                        .location(new GeoPoint(taxi.getCurrentPosition().getLat(), taxi.getCurrentPosition().getLng()))
                        .build();
                contractFactory.getElasticSearchRepository().save(gps);

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
            // Coordinate coordinateAtDistance = path.getCoordinateAtDistance(dist);
            Coordinate position = PathCalculator.findCoordinateAtPathPosition(x.toArray(new Double[0]), y.toArray(new Double[0]), distance);

            if (position.isZero()) {
                setStatusFinished();
                addNext(new _2TaxiAndDriveToGoal(contractFactory, this.passanger, this.taxi));
            } else {
                taxi.setCurrentPosition(position);
//                Coordinate currentPosition = taxi.getCurrentPosition();
//                Coordinate next = Coordinate.builder().lat(currentPosition.getLat() + 0.01).lng(currentPosition.getLng()).build();
//                taxi.setCurrentPosition(next);
            }

        }
        catch (NullPointerException e) {
            // System.out.println("Null");
        }
    }
}
