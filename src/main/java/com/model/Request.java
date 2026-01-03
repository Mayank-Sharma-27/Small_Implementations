package com.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Request {
    String id;
    String method;
    String url;
    Map<String, String> headers;
    JsonNode body;
    int expectedStatus;


}
