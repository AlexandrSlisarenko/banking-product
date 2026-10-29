package ru.slisarenko.checkweatherapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiErrorResponse(Error error) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Error(int code, String message) {}
}
