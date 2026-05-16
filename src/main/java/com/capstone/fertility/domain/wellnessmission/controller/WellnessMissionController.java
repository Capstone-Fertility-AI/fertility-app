package com.capstone.fertility.domain.wellnessmission.controller;

import com.capstone.fertility.domain.wellnessmission.dto.res.WellnessMissionResDTO;
import com.capstone.fertility.domain.wellnessmission.exception.code.WellnessMissionSuccessCode;
import com.capstone.fertility.domain.wellnessmission.service.command.WellnessMissionCommandService;
import com.capstone.fertility.domain.wellnessmission.service.query.WellnessMissionQueryService;
import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import com.capstone.fertility.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
            summary = "진행 중 웰니스 미션(최대 3개)",
            description = "최신 검사 결과의 미션 풀에서 아직 완료하지 않은 항목만 후보로 하여 최대 3개를 노출합니다. "
                    + "완료한 미션은 슬롯에서 빠지고 미완료 항목으로 다시 채워집니다. 풀 전체를 한 번씩 완료하면 다음 사이클이 시작되며 후보 구성이 다시 랜덤으로 섞입니다."
    )
    public ApiResponse<WellnessMissionResDTO.MyMissions> getTodayMissions(
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        WellnessMissionResDTO.MyMissions result = wellnessMissionQueryService.getTodayMissions(principal.getUserId());
        return ApiResponse.onSuccess(WellnessMissionSuccessCode.WELLNESS_MISSION_TODAY_FETCHED, result);
    }

    @PostMapping("/{missionId}/complete")
    @Operation(
            summary = "웰니스 미션 완료",
            description = "현재 GET /api/missions/today 로 노출된 미션만 완료할 수 있습니다. "
                    + "KST 기준 하루 처음 3회까지 +5 EXP(일일 상한 15), 이후에는 EXP 0으로 완료만 기록됩니다."
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
