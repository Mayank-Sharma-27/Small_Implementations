package com.bikemap;

import com.bikemap.bikemap.BikeMap;
import com.bikemap.bikemap.RouteResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bikemap.utils.BikeMapReadingUtils.getBikeMap;

public class BikeMapApplication {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        BikeMap bikeMap = getBikeMap();
        System.out.println(bikeMap.getStations().size());
        Map<String, Integer> stationBike = new HashMap<>();

        for (BikeMap.Station station : bikeMap.getStations()) {
            stationBike.put(station.getId(), station.getBikesAvailable());
        }

        int maxBikes = -1;
        String stationWithMaxBike = null;
        for (String key : stationBike.keySet()) {
            if (maxBikes < stationBike.get(key)) {
                stationWithMaxBike = key;
            }
            maxBikes = Math.max(maxBikes, stationBike.get(key));
        }
        System.out.println(stationWithMaxBike);
        int invalidIdStations = 0;

        for (BikeMap.Trip trip : bikeMap.getTrips()) {
            if (!stationBike.keySet().contains(trip.getEndStationId()) || !stationBike.keySet().contains(trip.getStartStationId())) {
                invalidIdStations++;
            }
        }
        System.out.println(invalidIdStations);
        List<RouteResult> results = new ArrayList<>();
        for (BikeMap.Trip trip : bikeMap.getTrips()) {
            try {
                String responseJson = callRouteApiStub(trip.getTripId());
                JsonNode response = objectMapper.readTree(responseJson);

                int distance = response.get("distance_meters").asInt();
                int duration = response.get("duration_seconds").asInt();

                results.add(new RouteResult(
                        trip.getTripId(),
                        "ok",
                        distance,
                        duration,
                        null
                ));

            } catch (Exception e) {
                results.add(new RouteResult(
                        trip.getTripId(),
                        "error",
                        null,
                        null,
                        e.getMessage()
                ));
            }
        }
        writeResultsToFile(results);
    }

    private static  void writeResultsToFile(List<RouteResult> results) {
        try {
            objectMapper.writeValue(new File("/Users/mayank/workspace/Small_Implementations/src/main/java/com/bikemap/resources/bike_routes_out.json"),
                    results);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write output to file", e);
        }
    }
    private static String callRouteApiStub(String tripId) {
        // Simulate a successful API response
        return """
                {
                  "distance_meters": 4200,
                  "duration_seconds": 780
                }
                """;
    }

}
