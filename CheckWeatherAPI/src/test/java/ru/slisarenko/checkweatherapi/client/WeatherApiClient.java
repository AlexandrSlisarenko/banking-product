package ru.slisarenko.checkweatherapi.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.slisarenko.checkweatherapi.config.WireMockConfig;

import static io.restassured.RestAssured.given;

public class WeatherApiClient {
    private static final String PATH = "/v1/current.json";

    private RequestSpecification spec() {
        return new RequestSpecBuilder()
                .setBaseUri(WireMockConfig.baseUrl())
                .addFilter(new AllureRestAssured())
                .build();
    }

    public Response getCurrentWeather(String city) {
        return given()
                .spec(spec())
                .queryParam("key", "test-api-key")
                .queryParam("q", city)
                .when()
                .get(PATH)
                .then()
                .extract()
                .response();
    }

    public Response getWithKey(String apiKey) {
        return given()
                .spec(spec())
                .queryParam("key", apiKey)
                .queryParam("q", "London")
                .when()
                .get(PATH)
                .then()
                .extract()
                .response();
    }
}
