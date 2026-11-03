package ru.slisarenko.checkweatherapi_ii.test


import io.restassured.RestAssured
import io.restassured.response.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static io.restassured.RestAssured.given

class WeatherApiSpec extends Specification{

    private static final Logger log = LoggerFactory.getLogger(WeatherApiSpec)

    @Shared
    String baseUri

    def setupSpec() {
        WireMockSetup.start()
        baseUri = "http://localhost:${WireMockSetup.wireMockServer.port()}"
        RestAssured.baseURI = baseUri
    }

    def cleanupSpec() {
        WireMockSetup.stop()
    }

    // ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================

    @Unroll
    def "Позитивный тест: текущая погода для города #city"() {
        given: "Заглушка для города #city уже настроена"
        log.info("Отправка запроса для города: {}", city)

        when: "Выполняется GET-запрос к /v1/current.json"
        Response response = given()
                .queryParam("q", city)
                .when()
                .get("/v1/current.json")
                .then()
                .extract().response()

        then: "Статус-код 200"
        response.statusCode() == 200

        and: "Ответ содержит ожидаемые значения"
        def json = response.jsonPath()
        log.info("Получен ответ для {}: temp_c={}, condition={}", city, json.getFloat("current.temp_c"), json.getString("current.condition.text"))

        // Пример сравнения с эталонными значениями (можно вынести в data table)
        def expectedTemp = getExpectedTemp(city)
        def actualTemp = json.getFloat("current.temp_c")
        if (actualTemp != expectedTemp) {
            log.warn("Расхождение для {}: expected temp_c={}, actual={}", city, expectedTemp, actualTemp)
        } else {
            log.info("Температура для {} совпадает: {} °C", city, actualTemp)
        }

        // Проверка других полей (пример)
        assert json.getString("location.name") == getExpectedLocationName(city)
        assert json.getString("current.condition.text") != null

        where:
        city        | _
        "Kursk"     | _
        "Minsk"     | _
        "Moskow"    | _
        "Voronezh"  | _
    }

    // ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================

    @Unroll
    def "Негативный тест: ошибка #errorCode для q=#queryValue"() {
        given: "Заглушка ошибки настроена"
        log.info("Проверка ошибки {} для q={}", errorCode, queryValue)

        when: "Выполняется GET-запрос"
        Response response = given()
                .queryParam("q", queryValue)
                .when()
                .get("/v1/current.json")
                .then()
                .extract().response()

        then: "Статус-код соответствует ожидаемому"
        response.statusCode() == expectedStatus

        and: "Тело содержит корректный код и сообщение об ошибке"
        def json = response.jsonPath()
        def actualCode = json.getInt("error.code")
        def actualMessage = json.getString("error.message")

        log.info("Получена ошибка: code={}, message={}", actualCode, actualMessage)

        actualCode == errorCode
        actualMessage == expectedMessage

        where:
        queryValue       | errorCode | expectedStatus | expectedMessage
        "invalid-key"    | 1002      | 401            | "API key is invalid or not provided."
        "disabled-key"   | 1003      | 403            | "API key has been disabled."
        "unknown-city"   | 1006      | 400            | "No matching location found."
        "bad-request"    | 1007      | 400            | "API key has exceeded the monthly quota."
        "quota-exceeded" | 2007      | 403            | "API key has exceeded the monthly quota."
    }

    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================

    private static float getExpectedTemp(String city) {
        switch (city) {
            case "Kursk":    return 9.0f
            case "Minsk":    return 5.6f
            case "Moskow":   return 3.7f
            case "Voronezh": return 9.7f
            default:         return 0f
        }
    }

    private static String getExpectedLocationName(String city) {
        switch (city) {
            case "Kursk":    return "Kursk"
            case "Minsk":    return "Minsk"
            case "Moskow":   return "Moscow"   // В ответе location.name = "Moscow"
            case "Voronezh": return "Voronezh"
            default:         return ""
        }
    }
}
