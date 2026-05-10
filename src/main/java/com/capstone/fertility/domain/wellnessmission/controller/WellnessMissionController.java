package com.capstone.fertility.domain.wellnessmission.controller;

import com.capstone.fertility.domain.wellnessmission.dto.req.WellnessMissionReqDTO;
import com.capstone.fertility.domain.wellnessmission.dto.res.WellnessMissionResDTO;
import com.capstone.fertility.domain.wellnessmission.exception.code.WellnessMissionSuccessCode;
import com.capstone.fertility.domain.wellnessmission.service.command.WellnessMissionCommandService;
import com.capstone.fertility.domain.wellnessmission.service.query.WellnessMissionQueryService;
import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import com.capstone.fertility.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class WellnessMissionController {

    private final WellnessMissionQueryService wellnessMissionQueryService;
    private final WellnessMissionCommandService wellnessMissionCommandService;

    @GetMapping("/me")
    @Operation(
            summary = "내 웰니스 미션 목록 조회",
            description = "LLM 리포트로부터 생성·저장된 본인의 모든 웰니스 미션을 최신순으로 반환합니다."
    )
    public ApiResponse<WellnessMissionResDTO.MyMissions> getMyMissions(
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        WellnessMissionResDTO.MyMissions result = wellnessMissionQueryService.getMyMissions(principal.getUserId());
        return ApiResponse.onSuccess(WellnessMissionSuccessCode.WELLNESS_MISSION_LIST_FETCHED, result);
    }

    @GetMapping("/today")
    @Operation(
            summary = "오늘의 웰니스 미션 3개",
            description = "KST 기준 오늘 제공되는 일일 미션(최대 3개)입니다. 자정이 지나 첫 조회 시 완료 상태가 리셋됩니다."
    )
    public ApiResponse<WellnessMissionResDTO.MyMissions> getTodayMissions(
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        WellnessMissionResDTO.MyMissions result = wellnessMissionQueryService.getTodayMissions(principal.getUserId());
        return ApiResponse.onSuccess(WellnessMissionSuccessCode.WELLNESS_MISSION_TODAY_FETCHED, result);
    }

    @PatchMapping("/{missionId}")
    @Operation(
            summary = "웰니스 미션 수정",
            description = "사용자가 자신의 페이스에 맞춰 빈도(frequencyCount), 지속 시간(durationValue), 난이도(difficulty)를 조정합니다. 보낸 필드만 반영됩니다."
    )
    public ApiResponse<WellnessMissionResDTO.MissionItem> update(
            @AuthenticationPrincipal CustomPrincipal principal,
            @Parameter(description = "수정할 미션 ID") @PathVariable Long missionId,
            @Valid @RequestBody WellnessMissionReqDTO.Update req
    ) {
        WellnessMissionResDTO.MissionItem result = wellnessMissionCommandService.update(
                principal.getUserId(), missionId, req
        );
        return ApiResponse.onSuccess(WellnessMissionSuccessCode.WELLNESS_MISSION_UPDATED, result);
    }

    @PostMapping("/{missionId}/complete")
    @Operation(
            summary = "웰니스 미션 완료",
            description = "오늘(KST)의 웰니스 미션만 완료할 수 있습니다. 첫 3회까지 +5 EXP(일일 상한 15), 그 이후에는 완료만 처리되고 EXP는 0입니다. 레벨업 시 EXP 바는 초기화되며 Lv.5 도달 시 명세 3종 꽃 중 미보유 꽃을 자동 획득합니다."
    )
    public ApiResponse<WellnessMissionResDTO.CompleteResult> complete(
            @AuthenticationPrincipal CustomPrincipal principal,
            @Parameter(description = "완료할 미션 ID") @PathVariable Long missionId
    ) {
        WellnessMissionResDTO.CompleteResult result = wellnessMissionCommandService.complete(
                principal.getUserId(), missionId
        );
        return ApiResponse.onSuccess(WellnessMissionSuccessCode.WELLNESS_MISSION_COMPLETED, result);
    }
}
