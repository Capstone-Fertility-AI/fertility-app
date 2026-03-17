package com.capstone.fertility.domain.test.service.query;

import com.capstone.fertility.domain.test.dto.res.TestResDTO;

public interface TestQueryService {

    /**
     * 검사 세션 상세 조회. 임시 저장된 currentStep과 입력값을 반환하며, 본인 세션만 조회 가능.
     */
    TestResDTO.SessionDetailDTO getSession(Long userId, Long sessionId);
}
