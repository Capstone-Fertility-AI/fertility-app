package com.capstone.fertility.domain.test.controller;

import com.capstone.fertility.domain.test.dto.res.TestResDTO;
import com.capstone.fertility.domain.test.service.command.TestCommandService;
import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import com.capstone.fertility.global.apiPayLoad.code.GeneralSuccessCode;
import com.capstone.fertility.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {

    private final TestCommandService testCommandService;

    @PostMapping
    @Operation(summary = "검사 세션 생성", description = "인증된 사용자에 대해 검사 세션을 생성하고 sessionId를 반환합니다.")
    public ApiResponse<TestResDTO.CreateSessionResDTO> createSession(
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        TestResDTO.CreateSessionResDTO result = testCommandService.createSession(principal.getUserId());
        return ApiResponse.onSuccess(GeneralSuccessCode.TEST_SESSION_CREATED, result);
    }
}
