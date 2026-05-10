package com.capstone.fertility.global.llm.client;

/**
 * LLM API 호출 추상화.
 * OpenAI / Gemini 등 구현체를 교체할 수 있도록 인터페이스로 분리.
 */
public interface LlmClient {

    /**
     * 자유 텍스트 응답을 받는다.
     */
    String chat(String systemPrompt, String userPrompt);

    /**
     * 응답을 JSON 객체 문자열로 강제한다.
     * (OpenAI의 response_format=json_object 와 동일한 효과)
     */
    String chatJson(String systemPrompt, String userPrompt);
}
