package com.capstone.fertility.domain.test.dto.res;

import com.capstone.fertility.domain.test.enums.TestSessionStatus;
import lombok.Builder;

public class TestResDTO {

    /**
     * 검사 세션 생성 응답 DTO (sessionId 반환)
     */
    @Builder
    public record CreateSessionResDTO(Long sessionId) {}

    /**
     * 검사 세션 조회 응답 DTO (임시 저장 복구용). currentStep과 저장된 입력값을 그대로 반환.
     */
    @Builder
    public record SessionDetailDTO(
            Long sessionId,
            TestSessionStatus status,
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
