Feature: Проверка текущей погоды (позитивные сценарии)

  Background:
    Given сервис погоды запущен на WireMock

  Scenario Outline: Запрос текущей погоды по городу и сравнение с эталоном
    When я запрашиваю текущую погоду для города "<city>"
    Then ответ содержит код 200
    And данные погоды соответствуют эталону для "<city>"

    Examples:
      | city   |
      | London |
      | Moscow |
      | Berlin |
      | Paris  |