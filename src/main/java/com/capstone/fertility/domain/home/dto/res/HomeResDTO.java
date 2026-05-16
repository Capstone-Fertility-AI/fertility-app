package com.capstone.fertility.domain.home.dto.res;

import com.capstone.fertility.domain.wellnessmission.dto.res.WellnessMissionResDTO;
import lombok.Builder;

import java.util.List;

/**
 * 홈 화면 조회 API 응답.
 * user(닉네임/레벨/EXP)와 todayMissions(최신 검사 기준 오늘의 미션 3개)를 채워준다.
 * recentTest / unreadNotiCount는 추후 연동.
 * actions: 검사하기, 내 몸상태 조회, 행동 가이드 카드용 형태만 제공(연동 없음).
 */
public class HomeResDTO {

    @Builder
    public record HomeDTO(
            UserSummary user,
            Object recentTest,
            List<WellnessMissionResDTO.MissionItem> todayMissions,
            int unreadNotiCount,
            List<ActionCard> actions
    ) {}

    @Builder
    public record UserSummary(
            String nickname,
            int level,
            int exp
    ) {}

    /** 홈 메인 카드용 형태 (검사하기 / 내 몸상태 조회 / 행동 가이드). 실제 연동은 추후. */
    @Builder
    public record ActionCard(
            String type,
            String title
    ) {}
}
