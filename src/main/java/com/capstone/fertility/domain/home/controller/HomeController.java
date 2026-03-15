package com.capstone.fertility.domain.home.controller;

import com.capstone.fertility.domain.home.dto.res.HomeResDTO;
import com.capstone.fertility.domain.home.service.HomeQueryService;
import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import com.capstone.fertility.global.apiPayLoad.code.GeneralSuccessCode;
import com.capstone.fertility.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeQueryService homeQueryService;

    @GetMapping
    @Operation(summary = "홈 화면 조회", description = "로그인 사용자의 홈 데이터를 반환합니다. 현재는 user(nickname, level, exp)만 채우고, recentTest/todayMissions/unreadNotiCount는 추후 연동 예정입니다.")
    public ApiResponse<HomeResDTO.HomeDTO> getHome(
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        HomeResDTO.HomeDTO home = homeQueryService.getHome(principal.getUserId());
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, home);
    }
}
