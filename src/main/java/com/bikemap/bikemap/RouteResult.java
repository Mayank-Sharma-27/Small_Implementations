package com.bikemap.bikemap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteResult {
    @JsonProperty("trip_id")
    private String tripId;

    private String status;
    @JsonProperty("distance_meters")
    private Integer distanceMeters;
    @JsonProperty("duration_seconds")
    private Integer durationSeconds;
    private String error;
}
