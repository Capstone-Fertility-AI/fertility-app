package com.capstone.fertility.domain.home.service;

import com.capstone.fertility.domain.home.dto.res.HomeResDTO;
import com.capstone.fertility.domain.user.entity.User;
import com.capstone.fertility.domain.user.exception.UserException;
import com.capstone.fertility.domain.user.exception.code.UserErrorCode;
import com.capstone.fertility.domain.user.repository.UserRepository;
import com.capstone.fertility.domain.wellnessmission.dto.res.WellnessMissionResDTO;
import com.capstone.fertility.domain.wellnessmission.service.query.WellnessMissionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeQueryServiceImpl implements HomeQueryService {

    private final UserRepository userRepository;
    private final WellnessMissionQueryService wellnessMissionQueryService;

    @Override
    @Transactional
    public HomeResDTO.HomeDTO getHome(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_ID_NOT_FOUND));

        HomeResDTO.UserSummary userSummary = HomeResDTO.UserSummary.builder()
                .nickname(user.getNickname())
                .level(user.getCurrentLevel())
                .exp(user.getCurrentExp())
                .build();

        WellnessMissionResDTO.MyMissions today = wellnessMissionQueryService.getTodayMissions(userId);
        List<WellnessMissionResDTO.MissionItem> todayMissions =
                today != null && today.missions() != null ? today.missions() : List.of();

        List<HomeResDTO.ActionCard> actions = List.of(
                HomeResDTO.ActionCard.builder().type("TEST").title("검사하기").build(),
                HomeResDTO.ActionCard.builder().type("BODY_STATUS").title("내 몸상태 조회").build(),
                HomeResDTO.ActionCard.builder().type("GUIDE").title("행동 가이드").build(),
                HomeResDTO.ActionCard.builder().type("SETTINGS").title("설정").build()
        );

        return HomeResDTO.HomeDTO.builder()
                .user(userSummary)
                .recentTest(null)
                .todayMissions(todayMissions)
                .unreadNotiCount(0)
                .actions(actions)
                .build();
    }
}
