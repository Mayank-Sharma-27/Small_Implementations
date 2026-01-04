package com.bikemap.bikemap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class BikeMap {

    private List<Station> stations;
    private List<Trip> trips;

    @Data
    public static class Station {
        private String id;
        private String name;
        private double lat;
        private double lon;
        @JsonProperty("bikes_available")
        private int bikesAvailable;
    }

    @Data
    public static class Trip {
        @JsonProperty("trip_id")
        private String tripId;
        @JsonProperty("start_station_id")
        private String startStationId;
        @JsonProperty("end_station_id")
        private String endStationId;
    }
}
