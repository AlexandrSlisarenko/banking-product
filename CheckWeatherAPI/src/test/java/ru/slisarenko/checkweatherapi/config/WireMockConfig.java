package ru.slisarenko.checkweatherapi.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class WireMockConfig {
    private static WireMockServer server;

    private WireMockConfig() {}

    public static synchronized WireMockServer getServer() {
        if (server == null) {
            server = new WireMockServer(
                    WireMockConfiguration
                            .options()
                            .dynamicPort()
                            .usingFilesUnderClasspath("wiremock")
            );
            server.start();
            loadDefaultStubs(server);

            Runtime.getRuntime().addShutdownHook(new Thread(WireMockConfig::stop));
        }
        return server;
    }

    public static String baseUrl() {
        return getServer().baseUrl();
    }

    private static void loadDefaultStubs(WireMockServer s) {
        // Здесь регистрируются стабы из src/test/resources/wiremock/mappings
        // через программный DSL, чтобы не зависеть от classpath-сканирования.
        // Сами тела ответов лежат в __files и читаются ниже.
        // (см. StubRegistry)
        StubRegistry.registerAll(s);
    }

    public static synchronized void reset() {
        if (server != null) {
            server.resetAll();
            loadDefaultStubs(server);
        }
    }

    public static synchronized void stop() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }
}
