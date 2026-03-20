package com.capstone.fertility.domain.home.service;

import com.capstone.fertility.domain.home.dto.res.HomeResDTO;

public interface HomeQueryService {

    /**
     * 홈 화면 데이터 조회. 현재는 user(nickname, level, exp)만 채우고,
     * recentTest / todayMissions / unreadNotiCount는 null·빈 리스트·0으로 반환.
     */
    HomeResDTO.HomeDTO getHome(Long userId);
}
