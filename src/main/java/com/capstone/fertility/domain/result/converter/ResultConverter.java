package com.capstone.fertility.domain.result.converter;

import com.capstone.fertility.domain.result.dto.res.ResultResDTO;
import com.capstone.fertility.domain.result.entity.TestResult;

import java.util.Collections;

public final class ResultConverter {

    private ResultConverter() {}

    public static ResultResDTO.ResultHistoryItemDTO toHistoryItemDTO(TestResult entity) {
        return ResultResDTO.ResultHistoryItemDTO.builder()
                .resultId(entity.getId())
                .sessionId(entity.getTestSession() != null ? entity.getTestSession().getId() : null)
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .aiScore(entity.getAiScore())
                .riskLevel(entity.getRiskLevel() != null ? entity.getRiskLevel().name() : null)
                .topFactors(entity.getTopFactors() != null
                        ? entity.getTopFactors()
                        : Collections.emptyList())
                .llmAdvice(entity.getLlmAdvice())
                .medicalEvidence(entity.getMedicalEvidence())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
