package ru.slisarenko.checkweatherapi.util;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.slisarenko.checkweatherapi.model.CurrentWeatherResponse;

public final class WeatherComparator {
    private static final Logger log = LoggerFactory.getLogger(WeatherComparator.class);

    private WeatherComparator() {}

    public static List<String> compare(CurrentWeatherResponse actual,
                                       CurrentWeatherResponse expected) {
        List<String> diffs = new ArrayList<>();

        // --- location ---
        compareString(diffs, "location.name",
                actual.location().name(), expected.location().name());
        compareString(diffs, "location.country",
                actual.location().country(), expected.location().country());
        compareDouble(diffs, "location.lat",
                actual.location().lat(), expected.location().lat());
        compareDouble(diffs, "location.lon",
                actual.location().lon(), expected.location().lon());

        // --- current ---
        compareDouble(diffs, "current.temp_c",
                actual.current().tempC(), expected.current().tempC());
        compareDouble(diffs, "current.temp_f",
                actual.current().tempF(), expected.current().tempF());
        compareInt(diffs, "current.humidity",
                actual.current().humidity(), expected.current().humidity());
        compareDouble(diffs, "current.wind_kph",
                actual.current().windKph(), expected.current().windKph());
        compareDouble(diffs, "current.pressure_mb",
                actual.current().pressureMb(), expected.current().pressureMb());

        if (diffs.isEmpty()) {
            log.info("Сравнение прошло без расхождений");
        } else {
            log.warn("Найдено {} расхождений:", diffs.size());
            diffs.forEach(d -> log.warn("   - {}", d));
        }
        return diffs;
    }

    private static void compareString(List<String> diffs, String field,
                                      String actual, String expected) {
        if (!Objects.equals(actual, expected)) {
            diffs.add("%s: expected='%s', actual='%s'".formatted(field, expected, actual));
        }
    }

    private static void compareInt(List<String> diffs, String field,
                                   int actual, int expected) {
        if (actual != expected) {
            diffs.add("%s: expected=%d, actual=%d".formatted(field, expected, actual));
        }
    }

    private static void compareDouble(List<String> diffs, String field,
                                      double actual, double expected) {
        if (Double.compare(actual, expected) != 0) {
            diffs.add("%s: expected=%.2f, actual=%.2f".formatted(field, expected, actual));
        }
    }
}
