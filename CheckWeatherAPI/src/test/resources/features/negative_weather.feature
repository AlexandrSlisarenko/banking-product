Feature: Обработка ошибок API погоды (негативные сценарии)

  Background:
    Given сервис погоды запущен на WireMock

  Scenario Outline: Получение ошибки API и её проверка
    When я запрашиваю погоду с ключом "<apiKey>"
    Then ответ содержит HTTP-код <httpStatus>
    And тело ответа содержит ошибку с кодом <errorCode> и сообщением "<message>"

    Examples:
      | apiKey         | httpStatus | errorCode | message                              |
      | bad-key-1002   | 401        | 1002      | API key is invalid or not provided.  |
      | invalid-key-1003 | 403      | 1003      | API key has been disabled.           |
      | unknown-city-1006 | 400     | 1006      | No matching location found.          |
      | quota-exceeded-2007 | 429   | 2007      | API key has exceeded the monthly quota. |