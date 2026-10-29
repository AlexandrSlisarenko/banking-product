package ru.slisarenko.checkweatherapi.steps;

import ru.slisarenko.checkweatherapi.client.WeatherApiClient;
import ru.slisarenko.checkweatherapi.config.WireMockConfig;
import ru.slisarenko.checkweatherapi.model.CurrentWeatherResponse;
import ru.slisarenko.checkweatherapi.util.WeatherComparator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.io.InputStream;
import java.util.List;

public class PositiveWeatherSteps {
    private final WeatherApiClient client = new WeatherApiClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private Response response;

    @Before
    public void setUp() {
        WireMockConfig.reset();
    }

    @Given("сервис погоды запущен на WireMock")
    public void wireMockIsRunning() {
        Assertions.assertTrue(WireMockConfig.getServer().isRunning(),
                "WireMock должен быть запущен");
    }

    @When("я запрашиваю текущую погоду для города {string}")
    public void requestWeather(String city) {
        response = client.getCurrentWeather(city);
        Allure.addAttachment("Response body (%s)".formatted(city),
                "application/json", response.asString(), ".json");
    }

    @Then("ответ содержит код {int}")
    public void responseHasStatus(int expectedStatus) {
        Allure.addAttachment("HTTP status",
                "text/plain", String.valueOf(response.statusCode()), ".txt");
        Assertions.assertEquals(expectedStatus, response.statusCode(),
                "Неверный HTTP-статус. Тело: %s".formatted(response.asString()));
    }

    @Then("данные погоды соответствуют эталону для {string}")
    public void compareWithExpected(String city) throws Exception {
        var actual   = response.as(CurrentWeatherResponse.class);
        var expected = readExpected(city);

        List<String> diffs = WeatherComparator.compare(actual, expected);

        Allure.addAttachment("Расхождения по %s".formatted(city),
                "text/plain",
                diffs.isEmpty() ? "Расхождений нет" : String.join("\n", diffs),
                ".txt");

        Assertions.assertTrue(diffs.isEmpty(),
                "Найдены расхождения:\n%s".formatted(String.join("\n", diffs)));
    }

    private CurrentWeatherResponse readExpected(String city) throws Exception {
        String file = switch (city) {
            case "London" -> "/wiremock/__files/london.json";
            case "Moscow" -> "/wiremock/__files/moscow.json";
            case "Berlin" -> "/wiremock/__files/berlin.json";
            case "Paris"  -> "/wiremock/__files/paris.json";
            default -> throw new IllegalArgumentException("Нет эталона для " + city);
        };
        try (InputStream is = getClass().getResourceAsStream(file)) {
            if (is == null) {
                throw new IllegalStateException("Файл эталона не найден в classpath: " + file);
            }
            return mapper.readValue(is, CurrentWeatherResponse.class);
        }
    }
}
