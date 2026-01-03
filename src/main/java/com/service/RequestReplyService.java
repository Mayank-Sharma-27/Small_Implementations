package com.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Request;
import com.model.RequestResult;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RequestReplyService {

    ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();
    private final Map<String, String> oldToNewId = new HashMap<>();
    @SneakyThrows
    public List<RequestResult> migrateRequests() {

        List<Request> requests = objectMapper.readValue(
                new File("/Users/mayank/workspace/Small_Implementations/src/main/java/com/resources/requests.json"),
                new TypeReference<List<Request>>() {
                }
        );
        List<RequestResult> results = new ArrayList<>();
        for (Request request : requests) {
            results.add(replyRequest(request));
        }
        return results;
    }

    private RequestResult replyRequest(Request request) {
        try {
            HttpRequest httpRequest = buildHttpRequest((request));
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            boolean success = response.statusCode() == request.getExpectedStatus();
            return new RequestResult(request.getId(), request.getExpectedStatus(), response.statusCode(), success, null);

        } catch (Exception e) {
            return new RequestResult(request.getId(), request.getExpectedStatus(), null, false, e.getMessage());

        }


    }

    @SneakyThrows
    private HttpRequest buildHttpRequest(Request request) {
        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(5));
        if (request.getHeaders() != null) {
            for (String key : request.getHeaders().keySet()) {
                httpRequestBuilder.header(key, request.getHeaders().get((key)));
            }
        }

        String method = request.getMethod();
        switch (method) {
            case "GET":
                httpRequestBuilder.GET();
                break;
            case "POST":
                String body = request.getBody() == null ? "" : objectMapper.writeValueAsString(request.getBody());
                httpRequestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
                httpRequestBuilder.header("Content-Type", "application/json");
                break;

        }
        return httpRequestBuilder.build();
    }

}
