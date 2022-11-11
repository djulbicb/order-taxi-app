package com.djulb.ui.placeholders;

import com.djulb.common.coord.BBox;
import com.djulb.common.coord.Coordinate;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

    private final Coordinate bottomLeftBound;
    private final Coordinate topLeftBound;
    int direction = -1;
    double moveIncrement = 0.01;

    Coordinate current = new Coordinate();

    public SampleService() {
        this.topLeftBound = BBox.getBerlinBbox().getTopLeft();
        this.bottomLeftBound = BBox.getBerlinBbox().getBottomLeft();

        current.setLat(topLeftBound.getLat());
        current.setLng(topLeftBound.getLng());
    }

    public void move() {
        current.setLat(current.getLat() + moveIncrement * direction);

        if (current.getLat() > topLeftBound.getLat() || bottomLeftBound.getLat() > current.getLat() ) {
            direction *= -1;
            System.out.println("Change");
        }
    }

    public Coordinate getCurrent() {
        return current;
    }
}
