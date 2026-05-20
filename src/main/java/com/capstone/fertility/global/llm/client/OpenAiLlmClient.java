package com.capstone.fertility.global.llm.client;

import com.capstone.fertility.global.llm.config.LlmProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OpenAI ChatCompletion API 호출 구현체.
 * <p>
 * {@code llm.provider} 설정값이 {@code openai} 이거나, 미설정인 경우 활성화된다.
 * (provider 값을 {@code gemini} 로 바꾸면 {@link GeminiLlmClient} 가 대신 주입된다.)
 */
@Slf4j
@Component
@EnableConfigurationProperties(LlmProperties.class)
@ConditionalOnProperty(prefix = "llm", name = "provider", havingValue = "openai", matchIfMissing = true)
public class OpenAiLlmClient implements LlmClient {

    private final WebClient webClient;
    private final LlmProperties props;

    public OpenAiLlmClient(LlmProperties props) {
        this.props = props;

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, props.getConnectTimeoutMs())
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(props.getReadTimeoutMs(), TimeUnit.MILLISECONDS)));

        this.webClient = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String chat(String systemPrompt, String userPrompt) {
        return call(systemPrompt, userPrompt, false);
    }

    @Override
    public String chatJson(String systemPrompt, String userPrompt) {
        return call(systemPrompt, userPrompt, true);
    }

    private String call(String systemPrompt, String userPrompt, boolean jsonMode) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", props.getModel());
        body.put("temperature", props.getTemperature());
        body.put("max_tokens", props.getMaxTokens());
        body.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
        ));
        if (jsonMode) {
            body.put("response_format", Map.of("type", "json_object"));
        }

        try {
            Map<?, ?> response = webClient.post()
                    .uri("/v1/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + props.getApiKey())
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                throw new RuntimeException("LLM 응답이 null입니다.");
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new RuntimeException("LLM choices가 비어 있습니다.");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");

        } catch (WebClientResponseException e) {
            log.error("LLM API 호출 실패: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("LLM API 호출 실패: " + e.getMessage(), e);
        }
    }
}
