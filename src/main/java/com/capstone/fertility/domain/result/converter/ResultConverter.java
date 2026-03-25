package com.capstone.fertility.domain.result.converter;

import com.capstone.fertility.domain.result.dto.res.ResultResDTO;
import com.capstone.fertility.domain.result.entity.TestResult;

public final class ResultConverter {

    private ResultConverter() {}

    public static ResultResDTO.ResultHistoryItemDTO toHistoryItemDTO(TestResult entity) {
        return ResultResDTO.ResultHistoryItemDTO.builder()
                .resultId(entity.getId())
                .sessionId(entity.getTestSession() != null ? entity.getTestSession().getId() : null)
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .aiScore(entity.getAiScore())
                .riskLevel(entity.getRiskLevel() != null ? entity.getRiskLevel().name() : null)
                .top1Factor(entity.getTop1Factor())
                .top2Factor(entity.getTop2Factor())
                .top3Factor(entity.getTop3Factor())
                .llmAdvice(entity.getLlmAdvice())
                .medicalEvidence(entity.getMedicalEvidence())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
