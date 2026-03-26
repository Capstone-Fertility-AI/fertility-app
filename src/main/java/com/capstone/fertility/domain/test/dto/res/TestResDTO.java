package com.capstone.fertility.domain.test.dto.res;

import com.capstone.fertility.domain.result.enums.RiskLevel;
import com.capstone.fertility.domain.test.enums.TestSessionStatus;
import com.capstone.fertility.domain.user.enums.Gender;
import lombok.Builder;

public class TestResDTO {

    /**
     * 검사 세션 생성 응답 DTO (sessionId 반환)
     */
    @Builder
    public record CreateSessionResDTO(Long sessionId) {}

    /**
     * 최종 제출 및 AI 예측 결과 응답 DTO
     */
    @Builder
    public record SubmitResult(
            Long resultId,
            Integer aiScore,
            Double riskProbability,
            RiskLevel riskLevel,
            String top1Factor,
            String top2Factor,
            String top3Factor
    ) {}

    /**
     * 검사 세션 조회 응답 DTO (임시 저장 복구용). currentStep과 저장된 입력값을 그대로 반환.
     */
    @Builder
    public record SessionDetailDTO(
            Long sessionId,
            TestSessionStatus status,
            Integer currentStep,
            Gender gender,
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
            Integer sleepHours,

            // 남성/공통 추가 필드
            Integer numBioKid,
            Integer sexFreq,
            Boolean hasSex12Mo,
            String smokeStatus,
            String drinkStatus,
            String bingeStatus
    ) {}
}
