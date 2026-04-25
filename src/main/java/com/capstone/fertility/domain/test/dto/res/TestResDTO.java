package com.capstone.fertility.domain.test.dto.res;

import com.capstone.fertility.domain.result.enums.RiskLevel;
import com.capstone.fertility.domain.test.enums.TestSessionStatus;
import com.capstone.fertility.domain.user.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

public class TestResDTO {

    /**
     * 검사 세션 생성 응답 DTO (sessionId 반환)
     */
    @Builder
    public record CreateSessionResDTO(Long sessionId) {}

    /**
     * 최종 제출 및 AI 예측 결과 응답 DTO.
     * <p>
     * AI 서버가 산출한 위험 요인 전체 리스트를 그대로 내려준다.
     * (이전 스펙의 top1/2/3, mission_candidates 필드는 더 이상 사용하지 않음)
     */
    @Builder
    public record SubmitResult(
            @Schema(description = "결과 PK", example = "12") Long resultId,
            @Schema(description = "AI 건강 점수(0~100). AI 서버가 계산한 값을 그대로 보관.", example = "78")
            Integer aiScore,
            @Schema(description = "위험 확률(%)", example = "22.5")
            Double riskProbability,
            @Schema(description = "Spring에서 score 기준으로 매핑한 위험 등급. 80 이상 SAFE, 50 이상 WARNING, 그 외 DANGER.")
            RiskLevel riskLevel,
            @Schema(
                    description = "활성 위험요인 전체 목록(중요도 순). Top 3 고정이 아니며 길이는 0~N. " +
                            "비어있으면 위험 요인 없음 상태로 간주한다.",
                    example = "[\"흡연\", \"수면 부족\", \"BMI 과다\"]"
            )
            List<String> topFactors
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
