package com.djulb.engine.contract.steps;

import com.djulb.engine.contract.ContractFactory;
import com.djulb.messages.redis.RedisNotification;
import com.djulb.utils.PathCalculator;
import com.djulb.common.coord.Coordinate;
import com.djulb.common.paths.RoutePath;
import com.djulb.common.objects.Passanger;
import com.djulb.common.objects.Taxi;
import com.djulb.osrm.model.Intersection;
import com.djulb.osrm.model.Step;

import java.util.ArrayList;
import java.util.List;

import static com.djulb.OrderTaxiAppSettings.MOVE_INCREMENT;

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
        contractFactory.getNotificationService().passangerAndTaxiStarted(passanger, taxi);
    }
    private int distance = MOVE_INCREMENT;
    @Override
    public void process() {
        try {
            distance += MOVE_INCREMENT;
            // Coordinate coordinateAtDistance = path.getCoordinateAtDistance(dist);
            Coordinate position = PathCalculator.findCoordinateAtPathPosition(x.toArray(new Double[0]), y.toArray(new Double[0]), distance);

            if (position.isZero()) {
                contractFactory.getNotificationService().taxiAndPassangerArrived(passanger, taxi);
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
