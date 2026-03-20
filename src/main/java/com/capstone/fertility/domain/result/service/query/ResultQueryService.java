package com.capstone.fertility.domain.result.service.query;

import com.capstone.fertility.domain.result.dto.res.ResultResDTO;

public interface ResultQueryService {

    /**
     * 파라미터 없음: 올해·이번 달.
     * year·month 둘 다 있으면 해당 연·월.
     * 하나만 있으면 잘못된 요청으로 처리합니다.
     */
    ResultResDTO.ResultHistoryDTO getHistory(Long userId, Integer year, Integer month);
}
