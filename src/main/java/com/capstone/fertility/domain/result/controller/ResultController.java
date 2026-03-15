package com.capstone.fertility.domain.result.controller;

import com.capstone.fertility.domain.result.dto.res.ResultResDTO;
import com.capstone.fertility.domain.result.service.ResultQueryService;
import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import com.capstone.fertility.global.apiPayLoad.code.GeneralSuccessCode;
import com.capstone.fertility.global.security.CustomPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/results")
public class ResultController {

    private final ResultQueryService resultQueryService;

    @GetMapping("/{resultId}")
    @Operation(
            summary = "특정 검사 결과 상세 리포트",
            description = "사용자 입력 데이터와 제시 데이터(status, score, llmAdvice, medicalEvidence)를 모두 반환합니다. " +
                    "resultId는 완료된 검사 세션의 ID(sessionId)와 동일하며, submit 완료 시 반환된 resultId로 조회합니다."
    )
    public ApiResponse<ResultResDTO.ResultDetailResDTO> getResultDetail(
            @PathVariable Long resultId,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        ResultResDTO.ResultDetailResDTO result = resultQueryService.getResultDetail(resultId, principal.getUserId());
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
}
