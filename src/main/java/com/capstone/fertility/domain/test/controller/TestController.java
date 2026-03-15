package com.capstone.fertility.domain.test.controller;

import com.capstone.fertility.domain.test.dto.req.TestReqDTO;
import com.capstone.fertility.domain.test.dto.res.TestResDTO;
import com.capstone.fertility.domain.test.exception.TestException;
import com.capstone.fertility.domain.test.exception.code.TestErrorCode;
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

    @PostMapping("/{sessionId}/step")
    @Operation(summary = "단계별 임시 저장", description = "각 단계를 넘길 때마다 해당 필드와 currentStep을 저장합니다.")
    public ApiResponse<Void> saveStep(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody @Valid TestReqDTO.StepSaveReqDTO request
    ) {
        testCommandService.saveStep(sessionId, principal.getUserId(), request);
        return ApiResponse.onSuccess(GeneralSuccessCode.OK);
    }

    @GetMapping("/current")
    @Operation(summary = "진행 중인 세션 복구", description = "현재 유저의 IN_PROGRESS 세션 중 가장 최근 항목을 반환합니다. 없으면 404.")
    public ApiResponse<TestResDTO.CurrentSessionResDTO> getCurrentSession(
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        return testQueryService.getCurrentSession(principal.getUserId())
                .map(session -> ApiResponse.onSuccess(GeneralSuccessCode.OK, session))
                .orElseThrow(() -> new TestException(TestErrorCode.NO_IN_PROGRESS_SESSION));
    }

    @PostMapping("/{sessionId}/submit")
    @Operation(summary = "검사 최종 완료", description = "세션 상태를 COMPLETED로 변경하고, 결과 상세 조회에 사용할 resultId를 반환합니다.")
    public ApiResponse<TestResDTO.SubmitResDTO> submit(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        TestResDTO.SubmitResDTO result = testCommandService.submit(sessionId, principal.getUserId());
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
}
