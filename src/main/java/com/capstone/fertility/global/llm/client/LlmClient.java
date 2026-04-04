package com.capstone.fertility.global.llm.client;

/**
 * LLM API 호출 추상화.
 * OpenAI / Gemini 등 구현체를 교체할 수 있도록 인터페이스로 분리.
 */
public interface LlmClient {

    /**
     * @param systemPrompt 시스템 역할 메시지
     * @param userPrompt   사용자 역할 메시지 (JSON 데이터)
     * @return LLM이 생성한 텍스트 (마크다운)
     */
    String chat(String systemPrompt, String userPrompt);
}
