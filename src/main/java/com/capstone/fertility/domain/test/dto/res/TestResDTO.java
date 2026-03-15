package com.capstone.fertility.domain.test.dto.res;

import lombok.Builder;

public class TestResDTO {

    /**
     * 검사 세션 생성 응답 DTO (sessionId 반환)
     */
    @Builder
    public record CreateSessionResDTO(Long sessionId) {}

    /**
     * 검사 최종 완료 응답 (POST /tests/{sessionId}/submit)
     * resultId는 GET /results/{resultId} 호출 시 사용하는 ID와 동일(완료된 세션 ID)
     */
    @Builder
    public record SubmitResDTO(Long resultId) {}

    /**
     * 진행 중인 세션 복구용 DTO (GET /tests/current)
     * 저장된 모든 필드값과 currentStep을 반환하여 프론트가 이전 상태 복구 가능
     */
    @Builder
    public record CurrentSessionResDTO(
            Long sessionId,
            Integer currentStep,
            Integer age,
            Double height,
            Double weight,
            Integer menarcheAge,
            Integer parity,
            Integer pcos,
            Integer endo,
            Integer uf,
            Integer pid,
            Integer chlam,
            Integer gon,
            Integer smokeLevel,
            Integer binge12,
            Integer sleepHours
    ) {}
}
