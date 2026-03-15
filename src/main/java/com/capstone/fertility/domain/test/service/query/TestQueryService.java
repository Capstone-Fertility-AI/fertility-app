package com.capstone.fertility.domain.test.service.query;

import com.capstone.fertility.domain.test.dto.res.TestResDTO;

import java.util.Optional;

public interface TestQueryService {

    /**
     * 현재 로그인한 유저의 진행 중(IN_PROGRESS) 세션 중 가장 최근 항목을 조회하여 복구용 데이터 반환.
     * 없으면 Optional.empty().
     */
    Optional<TestResDTO.CurrentSessionResDTO> getCurrentSession(Long userId);
}
