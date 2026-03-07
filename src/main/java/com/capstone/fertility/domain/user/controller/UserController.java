package com.capstone.fertility.domain.user.controller;

import com.capstone.fertility.domain.user.dto.res.UserResDTO;
import com.capstone.fertility.domain.user.exception.code.UserSuccessCode;
import com.capstone.fertility.domain.user.service.query.UserQueryService;
import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import com.capstone.fertility.global.security.CustomPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserQueryService userQueryService;

    @GetMapping("/me")
    public ApiResponse<UserResDTO.UserInfoDTO> getMyInfo(
            @AuthenticationPrincipal CustomPrincipal principal
    ) {
        UserResDTO.UserInfoDTO response = userQueryService.getMyInfo(principal.getUserId());
        return ApiResponse.onSuccess(UserSuccessCode.USER_FETCH_SUCCESS, response);
    }
}

