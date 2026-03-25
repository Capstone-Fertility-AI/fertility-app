package com.capstone.fertility.global.ai.client.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

/**
 * Python FastAPI AI 예측 서버 응답 DTO.
 * <pre>
 * {
 *   "status": "success",
 *   "result": {
 *     "gender": "female",
 *     "score": 75,
 *     "risk_probability": 25.0,
 *     "bmi": 24.5,
 *     "top_factors": ["...", "...", "..."]
 *   }
 * }
 * </pre>
 * JSON 키 {@code score}는 앱 DB/API의 {@code aiScore}에 대응합니다.
 * {@code risk_probability}, {@code bmi} 등은 camelCase record 필드로 역직렬화되며, 저장이 필요하면 서비스에서 선택 반영하면 됩니다.
 * grade / risk_level 텍스트는 Python이 내려주지 않으며, Spring에서 score 기반으로 {@link com.capstone.fertility.domain.result.enums.RiskLevel}을 산출합니다.
 */
public class AiPredictionResDTO {

    /**
     * 최상위 래퍼: status + result
     */
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
            @JsonProperty("status") String status,
            @JsonProperty("result") ResultPayload result
    ) {}

    /**
     * result 객체: 필수 매핑 대상은 score, top_factors
     */
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ResultPayload(
            @JsonProperty("gender") String gender,

            /**
             * AI 점수. 서버에 따라 score / aiScore / ai_score 중 하나로 내려올 수 있음.
             */
            @JsonProperty("score")
            @JsonAlias({"aiScore", "ai_score"})
            Integer aiScore,

            @JsonProperty("risk_probability") Double riskProbability,
            @JsonProperty("bmi") Double bmi,
            @JsonProperty("top_factors")
            @JsonAlias({"topFactors"})
            List<String> topFactors,

            @JsonProperty("top1_factor")
            @JsonAlias({"top1Factor"})
            String top1Factor,

            @JsonProperty("top2_factor")
            @JsonAlias({"top2Factor"})
            String top2Factor,

            @JsonProperty("top3_factor")
            @JsonAlias({"top3Factor"})
            String top3Factor,

            @JsonProperty("mission_candidates")
            @JsonAlias({"missionCandidates"})
            List<String> missionCandidates
    ) {
        public Integer resolvedAiScore() {
            return aiScore != null ? aiScore : 0;
        }

        public List<String> resolvedTopFactors() {
            if (topFactors != null && !topFactors.isEmpty()) {
                return topFactors;
            }
            return java.util.List.of(top1Factor, top2Factor, top3Factor).stream()
                    .filter(java.util.Objects::nonNull)
                    .toList();
        }
    }
}
