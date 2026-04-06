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

    /**
     * 중간 보고서(수면 + BMI·비만 단계 + 유병률 + 연령·성별 평균 BMI 대비 비율).
     * <ul>
     *   <li>비만 단계(BMI): 25~29.9 → STAGE_1, 30~34.9 → STAGE_2, 35 이상 → STAGE_3, 25 미만 → NONE.</li>
     *   <li>유병률: 만 20~64세, 5세 단위 표와 매칭. 그 밖 연령은 null.</li>
     *   <li>평균 BMI·대비 %: 만 20~69세, 10년 단위(20~29 … 60~69) 표와 매칭. 그 밖 연령은 null.</li>
     *   <li>수면: 만 20~59세는 10년 단위(20대~50대), 만 60세 이상은 동일 기준·라벨 "60대 이상".</li>
     *   <li>평균 BMI가 적용되는 연령대 문자열(예: "20~29")은 응답에 넣지 않음 — 클라이언트가 age로 표기.</li>
     *   <li>최종 제출 후 생성된 {@link SubmitResult}와 동일 필드(resultId ~ top3Factor)를 포함. 미제출 시 null.</li>
     * </ul>
     */
    @Builder
    public record InterimReportDTO(
            boolean sleepCalculated,
            String sleepAgeBand,
            Double sleepAvgHours,
            Double sleepDeltaHours,

            boolean obesityCalculated,
            Double bmi,
            /** NONE | STAGE_1 | STAGE_2 | STAGE_3 */
            String obesityStage,
            /** 현재 비만 단계의 유병률(%). NONE이거나 만 20~64세 밖이면 null */
            Double obesityPrevalencePct,
            /** 만 20~69세·동일 성별의 10년 단위(20~29 … 60~69) 평균 BMI. 해당 없으면 null. 연령대 라벨은 응답에 없음 */
            Double ageSexMeanBmi,
            /** (사용자 BMI − 평균 BMI) / 평균 × 100 (%), 소수 1자리. 양수면 평균보다 높음. 평균 없으면 null */
            Double bmiDeltaVsAgeSexMeanPct,
            /** BMI &lt; 25일 때 1단계 하한(25)까지 거리 등 */
            Double bmiDistanceToStageBoundary,
            String bmiDistanceDirection,

            /** 최종 제출·AI 예측 저장 후에만 값 있음. {@link SubmitResult#resultId()}와 동일 */
            Long resultId,
            Integer aiScore,
            Double riskProbability,
            RiskLevel riskLevel,
            String top1Factor,
            String top2Factor,
            String top3Factor
    ) {}
}
