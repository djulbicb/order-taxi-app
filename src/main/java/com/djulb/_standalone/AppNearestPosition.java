package com.djulb._standalone;

import com.djulb.way.bojan.BBox;
import com.djulb.osrm.OsrmBackendApi;
import com.djulb.osrm.model.Waypoints;
import com.djulb.way.bojan.Coordinate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.djulb.way.bojan.BBox.getBerlinBbox;

public class AppNearestPosition {
    public static void main(String[] args) throws IOException {
        List<Coordinate> coordinateMatrix = getCoordinateMatrix(getBerlinBbox(), 15);
        List<Coordinate> nearest = getNearest(coordinateMatrix);

        print(coordinateMatrix, "matrix_15x15.csv");
        print(nearest, "matrix_nearest_15x15.csv");

    }

    private static void print(List<Coordinate> coordinateMatrix, String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Coordinate coordinate : coordinateMatrix) {
            sb.append(coordinate.formatted());
            sb.append("\n");
        }

        FileWriter fw = new FileWriter(filename);
        fw.write(sb.toString());
        fw.close();
    }

    private static List<Coordinate> getNearest(List<Coordinate> coordinateMat) {
        OsrmBackendApi api = new OsrmBackendApi();
        List<Coordinate> nearestCoordinates = new ArrayList<>();

        for (Coordinate coordinate : coordinateMat) {
            Optional<Waypoints> nearest = api.getNearest(coordinate);
            if (nearest.isPresent()) {
                Waypoints waypoints = nearest.get();
                nearestCoordinates.add(Coordinate.builder().lat(waypoints.getLatitude()).lng(waypoints.getLongitude()).build());
            }
        }
        return nearestCoordinates;
    }

    private static List<Coordinate> getCoordinateMatrix(BBox box, int iterations) {
        List<Double> allLat = new ArrayList<>();
        List<Double> allLng = new ArrayList<>();

        double lngDif = box.getBottomRight().getLng() - box.getBottomLeft().getLng();
        double latDif = box.getTopLeft().getLat() - box.getBottomLeft().getLat();

        double lngIncrement = lngDif / iterations;
        double latIncrement = latDif / iterations;

        double startLat = box.getTopLeft().getLat();
        double startLng = box.getTopLeft().getLng();
        for (int i = 0; i <= iterations; i++) {
            allLat.add(startLat);
            allLng.add(startLng);

            startLat -= latIncrement;
            startLng += lngIncrement;
        }

        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < allLat.size() ; i++) {
            for (int j = 0; j < allLng.size() ; j++) {
                coordinates.add(Coordinate.builder()
                        .lat(allLat.get(i))
                        .lng(allLng.get(j))
                        .build());
            }
        }
        return coordinates;
    }
}
