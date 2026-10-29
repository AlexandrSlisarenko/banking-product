package ru.slisarenko.checkweatherapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrentWeatherResponse(Location location, Current current) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Location(
            String name,
            String region,
            String country,
            double lat,
            double lon
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Current(
            @JsonProperty("last_updated") String lastUpdated,
            @JsonProperty("temp_c")       double tempC,
            @JsonProperty("temp_f")       double tempF,
            int humidity,
            @JsonProperty("wind_kph")     double windKph,
            @JsonProperty("pressure_mb")  double pressureMb
    ) {}
}
