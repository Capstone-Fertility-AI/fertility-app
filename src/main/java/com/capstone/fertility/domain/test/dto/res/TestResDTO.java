package com.capstone.fertility.domain.test.dto.res;

import lombok.Builder;

public class TestResDTO {

    /**
     * 검사 세션 생성 응답 DTO (sessionId 반환)
     */
    @Builder
    public record CreateSessionResDTO(Long sessionId) {}
}
