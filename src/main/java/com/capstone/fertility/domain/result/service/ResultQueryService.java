package com.capstone.fertility.domain.result.service;

import com.capstone.fertility.domain.result.dto.res.ResultResDTO;

public interface ResultQueryService {

    /**
     * 특정 검사 결과 상세 리포트 조회 (사용자 입력 데이터 + score, llmAdvice, medicalEvidence).
     * resultId = 완료된 세션의 sessionId. 본인 세션이고 COMPLETED인 경우만 반환.
     */
    ResultResDTO.ResultDetailResDTO getResultDetail(Long resultId, Long userId);
}
