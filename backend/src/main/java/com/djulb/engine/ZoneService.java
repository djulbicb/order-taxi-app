package com.djulb.engine;

import com.djulb.OrderTaxiAppSettings;
import com.djulb.ui.sample.dto.SampleSize;
import com.djulb.utils.FileUtils;
import com.djulb.common.coord.BBox;
import com.djulb.common.coord.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ZoneService {
    private final HashMap<String, List<Coordinate>> zoneCoordinatesMap = new HashMap<>();
    private final List<String> zones = new ArrayList<>();
    private final Coordinate[] allCoordinatesArray;
    //String MATRIX_FOLDER = "src/main/resources/matrix/";
    String MATRIX_FOLDER = "matrix/";
    String MATRIX_50_FILEPATH = Paths.get(MATRIX_FOLDER, "matrix_nearest_50x50.csv").toString();
    static NumberFormat formatter = new DecimalFormat("#0.00");
    Random rnd = new Random();

    public ZoneService() throws IOException {
        List<Coordinate> coordinates = loadCoordinates(MATRIX_50_FILEPATH);
        for (Coordinate coordinate : coordinates) {
            String zone = getZone(coordinate);
            if (!zoneCoordinatesMap.containsKey(zone)) {
                zoneCoordinatesMap.put(zone, new ArrayList<>());
            }
            zoneCoordinatesMap.get(zone).add(coordinate);
        }
        zones.addAll(zoneCoordinatesMap.keySet());

        List<Coordinate> allCoordinates = new ArrayList<>();
        for (List<Coordinate> value : zoneCoordinatesMap.values()) {
            allCoordinates.addAll(value);
        }
        allCoordinatesArray = allCoordinates.toArray(Coordinate[]::new);
        log.info("Loaded {} coordinates in {} zones", coordinates.size(), zoneCoordinatesMap.entrySet().size());
    }

    public HashMap<String, List<Coordinate>> getZoneCoordinatesMap() {
        return zoneCoordinatesMap;
    }

    public List<Coordinate> getCoordinatesInZone(Coordinate coordinate) {
        String zone = getZone(coordinate);
        if (zoneCoordinatesMap.containsKey(zone)) {
            return zoneCoordinatesMap.get(zone);
        }
        return new ArrayList<>();
    }
    public Optional<Coordinate> getCoordinateInSameZone(Coordinate coordinate) {
        String zone = getZone(coordinate);
        return getRandomCoordinateInZone(zone);
    }
    public Optional<Coordinate> getRandomCoordinateInZone(String zone) {
        if (zoneCoordinatesMap.containsKey(zone)) {
            List<Coordinate> zoneMap = zoneCoordinatesMap.get(zone);
            return Optional.of(zoneMap.get(rnd.nextInt(zoneMap.size())));
        }
        return Optional.empty();
    }
    public static String getZone(Coordinate coordinate) {
       return getZone(coordinate.getLat(), coordinate.getLng());
    }
    public static String getZone(double lat, double lng) {
        // Round to nearest next half or whole
        double zoneLat = 5*((int)(Math.floor((lat + 0.05)*100)/5))/100d;
        double zoneLng = Math.floor(lng * 10) / 10d;
        return String.format("%s,%s", formatter.format(zoneLat), formatter.format(zoneLng));
    }

    private List<Coordinate> loadCoordinates(String filePath) throws IOException {
        List<Coordinate> coordinates = new ArrayList<>();

        List<String> rows = getRows(filePath);
        for (String row : rows) {
            String[] segments = row.split(",");
            double lat = Double.parseDouble(segments[0]);
            double lng = Double.parseDouble(segments[1]);

            Coordinate coordinate = Coordinate.builder().lat(lat).lng(lng).build();
            coordinates.add(coordinate);
        }
        return coordinates;
    }

    private List<String> getRows(String filePath) throws IOException {
//        return FileUtils.readAll(filePath);

        ArrayList<String> rows = new ArrayList<>();
        InputStream is = ZoneService.class.getClassLoader().getResourceAsStream(filePath);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null)
        {
            rows.add(line);
        }
        br.close();
        isr.close();
        is.close();
        return rows;
    }

    @Deprecated
    // Unused: can be deleted
    public String getZone(BBox boundingBox, int zoneLatDivision, int zoneLngDivision, Coordinate coordinate) {
        Map<String, BBox> zones = getZones(boundingBox, zoneLatDivision, zoneLngDivision);
        for (Map.Entry<String, BBox> entry : zones.entrySet()) {
            BBox value = entry.getValue();
            if (isInBoundingBox(value, coordinate)) {
                return entry.getKey();
            }
        }
        return "UNDEFINED";
    }

    @Deprecated
    // Unused: can be deleted
    public Map<String, BBox> getZones(BBox boundingBox, int zoneLatDivision, int zoneLngDivision) {
        Map<String, BBox> zones = new LinkedHashMap<>();
        Coordinate topLeft = boundingBox.getTopLeft();
        Coordinate topRight = boundingBox.getTopRight();
        Coordinate bottomLeft = boundingBox.getBottomLeft();

        double lngDiff = topRight.getLng() - topLeft.getLng();
        double latDiff = topLeft.getLat() - bottomLeft.getLat();
        double lngIncrement = lngDiff / zoneLngDivision;
        double latIncrement = latDiff / zoneLatDivision;

        for (int latIdx = 0; latIdx < zoneLngDivision; latIdx++) {
            for (int lngIdx = 0; lngIdx < zoneLngDivision; lngIdx++) {
                BBox bBox = BBox.builder()
                        .topLeft(Coordinate.builder()
                                .lng(topLeft.getLng() + lngIncrement*lngIdx)
                                .lat(topLeft.getLat() - latIncrement*latIdx).build())
                        .topRight(Coordinate.builder()
                                .lng(topLeft.getLng() + lngIncrement*(lngIdx+1))
                                .lat(topLeft.getLat() - latIncrement*(latIdx)).build())
                        .bottomLeft(Coordinate.builder()
                                .lng(topLeft.getLng() + lngIncrement*lngIdx)
                                .lat(topLeft.getLat() - latIncrement*(latIdx+1)).build())
                        .bottomRight(Coordinate.builder()
                                .lng(topLeft.getLng() + lngIncrement*(lngIdx+1))
                                .lat(topLeft.getLat() - latIncrement*(latIdx+1)).build())
                        .build();
                zones.put(String.format("%s_%s", latIdx, lngIdx), bBox);
            }
        }
        return zones;
    }

    @Deprecated
    public boolean isInBoundingBox(BBox bBox, Coordinate coordinate) {
        double lng = coordinate.getLng();
        double lat = coordinate.getLat();
        if (bBox.getTopLeft().getLat() >= lat && bBox.getBottomLeft().getLat() <= lat
        && bBox.getTopLeft().getLng() <= lng && bBox.getTopRight().getLng() >= lng) {
            return true;
        }
        return false;
    }

    public Coordinate getRandomCoordinate() {
        if (OrderTaxiAppSettings.PRIORITIZE_COORDINATES_IN_CENTER) {
            return getRandomCoordinateInZone(getZone(BBox.getBerlinBbox().getMiddlePoint())).get();
        }

//        System.out.println(LocalDateTime.now());
        return allCoordinatesArray[rnd.nextInt(allCoordinatesArray.length)];
    }

    public Optional<Coordinate> getCoordinateInAdjecentZone(Coordinate startCoordinate) {
        if (OrderTaxiAppSettings.PRIORITIZE_COORDINATES_IN_CENTER) {
            return getRandomCoordinateInZone(getZone(startCoordinate));
        }

        List<String> adjecentZones = getAdjecentZones(startCoordinate);
        String s = adjecentZones.get(rnd.nextInt(adjecentZones.size()));
        return getRandomCoordinateInZone(s);
    }

    public List<String> getAdjecentZones(Coordinate coordinate) {
        String zone1 = getZone(coordinate);
        List<Coordinate> coord = getCoord(coordinate, SampleSize.SIZE_3);
        List<String> zones = new ArrayList<>();
        for (Coordinate coordinate1 : coord) {
            String zone = ZoneService.getZone(coordinate1);
            if (zoneCoordinatesMap.containsKey(zone)) {
                zones.add(zone);
            }
        }
        if (zones.size() > 1) {
            zones.remove(zone1);
        }
        return zones;
    }
    public List<Coordinate> getCoord(Coordinate coordinate, SampleSize size) {
       return SampleSize.getCoord(coordinate, size);
    }
}
