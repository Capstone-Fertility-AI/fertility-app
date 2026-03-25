package com.capstone.fertility.global.ai.client;

import com.capstone.fertility.domain.user.enums.Gender;
import com.capstone.fertility.global.ai.config.AiProperties;
import com.capstone.fertility.global.ai.client.dto.req.AiPredictionReqDTO;
import com.capstone.fertility.global.ai.client.dto.res.AiPredictionResDTO;
import com.capstone.fertility.global.apiPayLoad.exception.GeneralException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class AiPredictionClientImplTest {

    private HttpServer server;
    private AiPredictionClientImpl client;
    private AiProperties properties;
    private volatile String lastPath;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.setExecutor(Executors.newSingleThreadExecutor());

        properties = new AiProperties();
        properties.setBaseUrl("http://127.0.0.1:" + server.getAddress().getPort());
        properties.setConnectTimeoutMs(1000);
        properties.setReadTimeoutMs(1000);
        properties.getPrediction().getPath().setMale("/api/predict/male");
        properties.getPrediction().getPath().setFemale("/api/predict/female");

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(properties.getReadTimeoutMs()));

        client = new AiPredictionClientImpl(
                WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build(),
                properties
        );
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void predictMale_success_mapsResponseAndPath() {
        registerJsonHandler("/api/predict/male", 200, """
                        {
                          "status":"success",
                          "result":{
                            "score":75,
                            "risk_probability":25.0,
                            "top_factors":["요인1","요인2","요인3"],
                            "mission_candidates":["m1","m2"]
                          }
                        }
                        """);
        server.start();

        AiPredictionResDTO.Response response = client.predictMale(dummyRequest());

        assertNotNull(response);
        assertEquals("success", response.status());
        assertNotNull(response.result());
        assertEquals(75, response.result().resolvedAiScore());
        assertEquals(25.0, response.result().riskProbability());
        assertEquals(3, response.result().resolvedTopFactors().size());
        assertEquals(2, response.result().missionCandidates().size());

        assertEquals("/api/predict/male", lastPath);
    }

    @Test
    void predictFemale_server5xx_throwsGeneralException() {
        registerJsonHandler("/api/predict/female", 500, "boom");
        server.start();

        assertThrows(GeneralException.class, () -> client.predictFemale(dummyRequest()));
    }

    @Test
    void predict_invalidStatus_throwsGeneralException() {
        registerJsonHandler("/api/predict/female", 200, """
                        {
                          "status":"fail",
                          "result":{"score":75}
                        }
                        """);
        server.start();

        assertThrows(GeneralException.class, () -> client.predict(dummyRequest(), Gender.F));
    }

    @Test
    void predict_readTimeout_throwsGeneralException() {
        server.createContext("/api/predict/male", exchange -> {
            lastPath = exchange.getRequestURI().getPath();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            byte[] body = """
                        {
                          "status":"success",
                          "result":{"score":80}
                        }
                        """.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body);
            }
        });
        server.start();

        assertThrows(GeneralException.class, () -> client.predict(dummyRequest(), Gender.M));
    }

    private void registerJsonHandler(String path, int statusCode, String responseJson) {
        server.createContext(path, exchange -> writeJson(exchange, path, statusCode, responseJson));
    }

    private void writeJson(HttpExchange exchange, String path, int statusCode, String responseJson) throws IOException {
        lastPath = exchange.getRequestURI().getPath();
        if (!path.equals(lastPath)) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }
        byte[] body = responseJson.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }

    private static AiPredictionReqDTO.Request dummyRequest() {
        return AiPredictionReqDTO.Request.builder()
                .age(30)
                .height(165.0)
                .weight(55.0)
                .chlam(0)
                .gon(0)
                .smoke30(0)
                .drink12(0)
                .binge12Score(0)
                .stressScore(5)
                .stressLevel("LOW")
                .build();
    }
}
