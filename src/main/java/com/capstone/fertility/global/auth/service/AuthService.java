package com.capstone.fertility.global.auth.service;

import com.capstone.fertility.domain.user.enums.Role;
import com.capstone.fertility.global.auth.entity.RefreshToken;
import com.capstone.fertility.global.dto.TokenRefreshResponse;
import com.capstone.fertility.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그인/회원가입 시 발급한 refresh 토큰(불투명 문자열, DB 저장)을 검증하고
 * 새 access JWT를 발급합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final RefreshTokenProvider refreshTokenProvider;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenRefreshResponse refreshToken(String refreshToken) {
        RefreshToken validated = refreshTokenProvider.validate(refreshToken);
        Long userId = validated.getUserId();

        String newAccessToken = jwtTokenProvider.createToken(userId, Role.USER);

        return TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }
}
