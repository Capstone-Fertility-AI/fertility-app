package com.capstone.fertility.global.auth.controller;

import com.capstone.fertility.domain.user.dto.req.UserReqDTO;
import com.capstone.fertility.domain.user.dto.res.UserResDTO;
import com.capstone.fertility.domain.user.exception.code.UserSuccessCode;
import com.capstone.fertility.domain.user.service.command.UserCommandService;
import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserCommandService userCommandService;

    @PostMapping("/signup")
    public ApiResponse<UserResDTO.LoginResDTO> signUp(@RequestBody @Valid UserReqDTO.SignUpReqDTO request){

        UserResDTO.LoginResDTO response = userCommandService.signUp(request);

        return ApiResponse.onSuccess(UserSuccessCode.USER_SIGNUP_SUCCESS, response);
    }
}
