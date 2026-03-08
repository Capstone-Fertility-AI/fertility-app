package com.capstone.fertility.global.oauth.controller;

/*
카카오 로그인 시작시키기 (카카오 로그인 페이지로 redirect)
카카오 인증서버가 돌려준 code 받고 JWT 발급해주기.
 */

import com.capstone.fertility.domain.user.converter.UserConverter;
import com.capstone.fertility.domain.user.dto.res.UserResDTO;
import com.capstone.fertility.domain.user.entity.User;
import com.capstone.fertility.domain.user.enums.Role;
import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import com.capstone.fertility.global.apiPayLoad.code.OauthSuccessCode;
import com.capstone.fertility.global.auth.service.RefreshTokenProvider;
import com.capstone.fertility.global.oauth.model.KakaoUserInfo;
import com.capstone.fertility.global.oauth.service.KakaoOauthService;
import com.capstone.fertility.global.oauth.service.OauthUserService;
import com.capstone.fertility.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {

    private final KakaoOauthService kakaoOauthService;
    private final OauthUserService oauthUserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    /**
     * 1) 카카오 로그인 시작
     *    클라이언트가 호출하면 → 카카오 로그인 페이지로 redirect됨
     */
    /* 01-03 카카오 로그인 API */
    @GetMapping("/kakao/login")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoOauthService.generateKakaoLoginUrl());
    }


    /**
     * 2) 카카오 인증 후 redirect_uri로 code가 넘어오는 콜백 URL
     *    예) GET /oauth/kakao/callback?code=xxxx
     */
    @GetMapping("/kakao/callback")
    public ApiResponse<UserResDTO.LoginResDTO> kakaoCallback(
            @RequestParam("code") String code
    ) {
        // 1. 카카오 사용자 정보 조회
        KakaoUserInfo kakaoUser = kakaoOauthService.fetchKakaoUser(code);

        // 2. DB User 조회/생성
        User user = oauthUserService.handleKakaoUser(kakaoUser);

        // 3. 토큰 발급
        String accessToken =
                jwtTokenProvider.createToken(user.getId(), Role.USER);

        String refreshToken =
                refreshTokenProvider.createAndSave(user.getId());

        return ApiResponse.onSuccess(
                OauthSuccessCode.KAKAO_LOGIN_SUCCESS,
                UserConverter.toLoginResDTO(accessToken, refreshToken, user)
        );
    }
}
