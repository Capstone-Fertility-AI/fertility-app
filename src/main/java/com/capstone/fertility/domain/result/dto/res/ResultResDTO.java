package com.capstone.fertility.domain.result.dto.res;

import com.capstone.fertility.domain.test.enums.TestSessionStatus;
import lombok.Builder;

/**
 * GET /results/{resultId} 응답 - 사용자 입력 데이터 + 제시 데이터(점수, LLM 조언, 의학적 근거) 전체
 */
public class ResultResDTO {

    @Builder
    public record ResultDetailResDTO(
            Long resultId,
            TestSessionStatus status,
            Integer score,
            String llmAdvice,
            String medicalEvidence,
            // --- 사용자 입력 데이터 ---
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
