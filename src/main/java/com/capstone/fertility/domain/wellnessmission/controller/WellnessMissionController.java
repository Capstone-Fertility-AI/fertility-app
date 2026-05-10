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
}
