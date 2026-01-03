package com.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestResult {
    public final String id;
    public final int expectedStatus;
    public final Integer actualStatus;
    public final boolean success;
    public final String error;
}
