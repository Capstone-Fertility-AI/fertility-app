package com.capstone.fertility.domain.mission.controller;

import com.capstone.fertility.domain.mission.dto.res.MissionResDTO;
import com.capstone.fertility.domain.mission.exception.code.MissionSuccessCode;
import com.capstone.fertility.domain.mission.service.command.MissionCommandService;
import com.capstone.fertility.domain.mission.service.command.SproutCycleCommandService;
import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import com.capstone.fertility.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/missions")
public class MissionController {

    private final MissionCommandService missionCommandService;
    private final SproutCycleCommandService sproutCycleCommandService;

    @PostMapping("/{missionId}/complete")
    @Operation(
            summary = "미션 완료",
            description = "로그인 사용자가 지정한 미션을 완료 처리합니다. 첫 완료 시 expGained만큼 경험치를 반영하고, 이미 완료된 경우 expGained=0·alreadyCompleted=true로 멱등 응답합니다."
    )
    public ApiResponse<MissionResDTO.MissionCompleteDTO> complete(
            @AuthenticationPrincipal CustomPrincipal principal,
            @Parameter(description = "미션 ID")
            @PathVariable Long missionId
    ) {
        MissionResDTO.MissionCompleteDTO result = missionCommandService.complete(principal.getUserId(), missionId);
        return ApiResponse.onSuccess(MissionSuccessCode.MISSION_COMPLETED, result);
    }

    @PostMapping("/sprout/reset-after-retest")
    @Operation(
            summary = "재검사 후 새싹 사이클 초기화",
            description = "명세: Lv.5(꽃 달성) 상태에서 AI 건강 재검사를 마친 뒤 호출합니다. 꽃 도감은 유지하고 새싹 레벨·EXP만 Lv.1·0으로 초기화합니다."
    )
    public ApiResponse<String> resetSproutAfterRetest(
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        sproutCycleCommandService.resetAfterRetest(principal.getUserId());
        return ApiResponse.onSuccess(MissionSuccessCode.SPROUT_CYCLE_RESET, "OK");
    }
}
