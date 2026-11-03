package ru.slisarenko.checkweatherapi_ii.test

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static com.github.tomakehurst.wiremock.client.WireMock.*

class WireMockSetup {
    private static final Logger log = LoggerFactory.getLogger(WireMockSetup)
    static WireMockServer wireMockServer

    static void start() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort())
            wireMockServer.start()
            WireMock.configureFor(wireMockServer.port())
            log.info("WireMock запущен на порту {}", wireMockServer.port())
            stubAll()
        }
    }

    static void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop()
            wireMockServer = null
            log.info("WireMock остановлен")
        }
    }

    private static void stubAll() {
        stubCity("Kursk", "kurskBody.json", "kurskHeaders.json")
        stubCity("Minsk", "minskBody.json", "minskHeaders.json")
        stubCity("Moskow", "moscowBody.json", "moscowHeaders.json") // В запросе указан "Moskow"
        stubCity("Voronezh", "voronezhBody.json", "voronezhHeaders.json")

        stubError("invalid-key", "error_1002.json", 401)
        stubError("disabled-key", "error_1003.json", 403)
        stubError("unknown-city", "error_1006.json", 400)
        stubError("bad-request", "error_1007.json", 400)
        stubError("quota-exceeded", "error_2007.json", 403)
    }

    private static void stubCity(String city, String bodyFile, String headersFile) {
        def body = loadResource(bodyFile)
        def headers = loadHeaders(headersFile)

        /*stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("q", equalTo(city))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeaders(headers)
                        .withBody(body)))*/
        def response = aResponse().withStatus(200).withBody(body)
        headers.each { k, v ->
            if (!(k in ["Transfer-Encoding", "Connection"])) response = response.withHeader(k as String, v as String)
        }
        stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("q", equalTo(city))
                .willReturn(response))
        log.info("Заглушка для города {} зарегистрирована", city)
    }

    private static void stubError(String queryValue, String errorFile, int status) {
        def body = loadResource(errorFile)
        stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("q", equalTo(queryValue))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)))
        log.info("Заглушка ошибки {} зарегистрирована для q={}", errorFile, queryValue)
    }

    private static String loadResource(String fileName) {
        def stream = WireMockSetup.class.getClassLoader().getResourceAsStream("stubs/${fileName}")
        if (stream == null) throw new IllegalArgumentException("Файл не найден: ${fileName}")
        return stream.text
    }

    private static Map<String, String> loadHeaders(String fileName) {
        def stream = WireMockSetup.class.getClassLoader().getResourceAsStream("stubs/${fileName}")
        if (stream == null) throw new IllegalArgumentException("Файл заголовков не найден: ${fileName}")
        def slurper = new JsonSlurper()
        def json = slurper.parse(stream)
        return json.collectEntries { k, v -> [(k): v] }
    }
}
