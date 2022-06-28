package com.djulb.service.contract.steps;

import com.djulb.db.elastic.FoodPOIRepository;
import com.djulb.db.elastic.FoodPOIRepositoryCustomImpl;
import com.djulb.service.ManagerTaxi;
import com.djulb.service.contract.ContractFactory;
import com.djulb.way.PathCalculator;
import com.djulb.way.bojan.Coordinate;
import com.djulb.way.bojan.RoutePath;
import com.djulb.way.elements.Passanger;
import com.djulb.way.elements.Taxi;
import com.djulb.way.elements.redis.RedisNotificationService;
import com.djulb.way.osrm.OsrmBackendApi;
import com.djulb.way.osrm.model.Intersection;
import com.djulb.way.osrm.model.Step;

import java.util.ArrayList;
import java.util.List;

import static com.djulb.AppSettings.INCREMENT;

public class _2TaxiAndDriveToGoal extends AbstractContractStep {

    private final Taxi taxi;
    private final Passanger passanger;

    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();

    public _2TaxiAndDriveToGoal(ContractFactory contractFactory,
                                Passanger passanger,
                                Taxi taxi) {
        super(contractFactory);

        this.taxi = taxi;
        this.passanger = passanger;

        Coordinate start = taxi.getCurrentPosition();
        Coordinate end = passanger.getDestination();

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
    private int distance = INCREMENT;
    @Override
    public void process() {
        try {
            distance +=INCREMENT;
            // Coordinate coordinateAtDistance = path.getCoordinateAtDistance(dist);
            Coordinate position = PathCalculator.findCoordinateAtPathPosition(x.toArray(new Double[0]), y.toArray(new Double[0]), distance);

            if (position.isZero()) {
                setStatusFinished();
                addNext(new _3TaxiRelease(contractFactory, passanger, taxi));
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
