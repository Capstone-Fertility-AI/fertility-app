package com.capstone.fertility.domain.result.dto.res;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class ResultResDTO {

    @Builder
    public record ResultHistoryItemDTO(
            Long resultId,
            Long sessionId,
            Long userId,
            Integer aiScore,
            String riskLevel,
            String top1Factor,
            String top2Factor,
            String top3Factor,
            String llmAdvice,
            String medicalEvidence,
            LocalDateTime createdAt
    ) {}

    /**
     * 조회에 사용한 연·월과 해당 기간 결과 목록.
     */
    @Builder
    public record ResultHistoryDTO(
            int year,
            int month,
            List<ResultHistoryItemDTO> items
    ) {}
}
