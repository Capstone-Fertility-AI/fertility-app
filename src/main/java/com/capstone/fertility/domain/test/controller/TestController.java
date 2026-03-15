package com.capstone.fertility.domain.test.controller;

import com.capstone.fertility.domain.test.dto.req.StepSaveReqDTO;
import com.capstone.fertility.domain.test.dto.res.TestResDTO;
import com.capstone.fertility.domain.test.service.command.TestCommandService;
import com.capstone.fertility.domain.test.service.query.TestQueryService;
import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import com.capstone.fertility.global.apiPayLoad.code.GeneralSuccessCode;
import com.capstone.fertility.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {

    private final TestCommandService testCommandService;
    private final TestQueryService testQueryService;

    @PostMapping
    @Operation(summary = "검사 세션 생성", description = "인증된 사용자에 대해 검사 세션을 생성하고 sessionId를 반환합니다.")
    public ApiResponse<TestResDTO.CreateSessionResDTO> createSession(
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        TestResDTO.CreateSessionResDTO result = testCommandService.createSession(principal.getUserId());
        return ApiResponse.onSuccess(GeneralSuccessCode.TEST_SESSION_CREATED, result);
    }

    @GetMapping("/{sessionId}")
    @Operation(summary = "검사 세션 조회(임시 저장 복구)", description = "저장된 세션의 currentStep과 입력값을 반환합니다. 재진입 시 폼 복구용이며, 본인 세션만 조회 가능합니다.")
    public ApiResponse<TestResDTO.SessionDetailDTO> getSession(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PathVariable Long sessionId
    ) {
        TestResDTO.SessionDetailDTO result = testQueryService.getSession(principal.getUserId(), sessionId);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }

    @PostMapping("/{sessionId}/step")
    @Operation(summary = "검사 단계 임시 저장", description = "지정한 단계(1~9)의 입력값을 세션에 임시 저장합니다. 중도 이탈 방지용이며, 본인 세션만 수정 가능합니다.")
    public ApiResponse<Void> saveStep(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PathVariable Long sessionId,
            @Valid @RequestBody StepSaveReqDTO request
    ) {
        testCommandService.saveStep(principal.getUserId(), sessionId, request);
        return ApiResponse.onSuccess(GeneralSuccessCode.TEST_STEP_SAVED, null);
    }
}
