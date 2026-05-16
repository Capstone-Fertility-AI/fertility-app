package com.capstone.fertility.domain.mission.controller;

import com.capstone.fertility.domain.mission.dto.res.MissionResDTO;
import com.capstone.fertility.domain.mission.exception.code.MissionSuccessCode;
import com.capstone.fertility.domain.mission.service.command.SproutCycleCommandService;
import com.capstone.fertility.domain.mission.service.query.MissionQueryService;
import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import com.capstone.fertility.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/missions")
public class MissionController {

    private final MissionQueryService missionQueryService;
    private final SproutCycleCommandService sproutCycleCommandService;

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

    @GetMapping("/history")
    @Operation(
            summary = "새싹 성장 기록(히스토리)",
            description = "커서 기반 페이지네이션입니다. 첫 요청은 lastLogId 생략, 다음 페이지는 직전 응답의 nextLastLogId를 lastLogId로 전달합니다."
    )
    public ApiResponse<MissionResDTO.MissionHistoryDTO> getHistory(
            @AuthenticationPrincipal CustomPrincipal principal,
            @Parameter(description = "이전 페이지 마지막 항목의 log id보다 오래된 기록만 조회")
            @RequestParam(required = false) Long lastLogId,
            @Parameter(description = "페이지 크기 (기본 20, 최대 100)")
            @RequestParam(required = false, defaultValue = "20") int size
    ) {
        MissionResDTO.MissionHistoryDTO result = missionQueryService.getHistory(principal.getUserId(), lastLogId, size);
        return ApiResponse.onSuccess(MissionSuccessCode.MISSION_HISTORY_FETCHED, result);
    }

    @GetMapping("/collections")
    @Operation(summary = "꽃 도감", description = "획득한 최종 진화체(꽃 Lv.5) 목록입니다. 획득 처리는 별도 성장/진화 플로우에서 저장됩니다.")
    public ApiResponse<List<MissionResDTO.FlowerCollectionItemDTO>> getCollections(
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        List<MissionResDTO.FlowerCollectionItemDTO> result = missionQueryService.getCollections(principal.getUserId());
        return ApiResponse.onSuccess(MissionSuccessCode.MISSION_COLLECTIONS_FETCHED, result);
    }
}
