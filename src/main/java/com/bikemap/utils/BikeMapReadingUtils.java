package com.bikemap.utils;

import com.bikemap.bikemap.BikeMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.File;

public class BikeMapReadingUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static BikeMap getBikeMap() {
        BikeMap bikeMap = objectMapper
                .readValue(new File("/Users/mayank/workspace/Small_Implementations/src/main/java/com/bikemap/resources/bikemap.json"),
                        BikeMap.class);
        return bikeMap;
    }



}
