package ru.slisarenko.checkweatherapi.steps;

import ru.slisarenko.checkweatherapi.client.WeatherApiClient;
import ru.slisarenko.checkweatherapi.config.WireMockConfig;
import ru.slisarenko.checkweatherapi.model.ApiErrorResponse;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

public class NegativeWeatherSteps {
    private final WeatherApiClient client = new WeatherApiClient();

    private Response response;

    @Before
    public void setUp() {
        WireMockConfig.reset();
    }

    @When("я запрашиваю погоду с ключом {string}")
    public void requestWithKey(String apiKey) {
        response = client.getWithKey(apiKey);
        Allure.addAttachment("Error response (%s)".formatted(apiKey),
                "application/json", response.asString(), ".json");
    }

    @Then("ответ содержит HTTP-код {int}")
    public void responseHasHttpStatus(int expectedStatus) {
        Allure.addAttachment("HTTP status",
                "text/plain", String.valueOf(response.statusCode()), ".txt");
        Assertions.assertEquals(expectedStatus, response.statusCode(),
                "Неверный HTTP-статус. Тело: %s".formatted(response.asString()));
    }

    @Then("тело ответа содержит ошибку с кодом {int} и сообщением {string}")
    public void bodyContainsError(int expectedCode, String expectedMessage) {
        var errorResponse = response.as(ApiErrorResponse.class);

        Assertions.assertNotNull(errorResponse.error(),
                "В ответе отсутствует блок error");

        Allure.addAttachment("Error code",
                "text/plain", String.valueOf(errorResponse.error().code()), ".txt");

        Assertions.assertEquals(expectedCode, errorResponse.error().code(),
                "Код ошибки не совпадает. Тело: %s".formatted(response.asString()));

        Assertions.assertEquals(expectedMessage, errorResponse.error().message(),
                "Сообщение ошибки не совпадает. Тело: %s".formatted(response.asString()));
    }
}
