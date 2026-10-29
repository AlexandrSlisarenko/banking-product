package ru.slisarenko.checkweatherapi.config;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class StubRegistry {
    private StubRegistry() {}

    public static void registerAll(WireMockServer server) {
        // ---------- ПОЗИТИВНЫЕ СТАБЫ ----------
        stubCity(server, "London", "london.json");
        stubCity(server, "Moscow", "moscow.json");
        stubCity(server, "Berlin", "berlin.json");
        stubCity(server, "Paris", "paris.json");

        // ---------- НЕГАТИВНЫЕ СТАБЫ ----------
        stubError(server, "bad-key-1002", 401, "error_1002.json");
        stubError(server, "invalid-key-1003", 403, "error_1003.json");
        stubError(server, "unknown-city-1006", 400, "error_1006.json");
        stubError(server, "quota-exceeded-2007", 429, "error_2007.json");
    }

    private static void stubCity(WireMockServer s, String city, String bodyFile) {
        s.stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("q", equalTo(city))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile(bodyFile)));
    }

    private static void stubError(WireMockServer s, String apiKey, int status, String bodyFile) {
        s.stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("key", equalTo(apiKey))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile(bodyFile)));
    }
}
