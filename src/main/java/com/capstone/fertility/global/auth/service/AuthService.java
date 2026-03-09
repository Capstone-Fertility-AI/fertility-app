package com.capstone.fertility.global.auth.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.fertility.global.apiPayLoad.exception.GeneralException;
import com.capstone.fertility.global.auth.JwtProvider;
import com.capstone.fertility.global.dto.TokenRefreshResponse;


import com.capstone.fertility.global.apiPayLoad.code.*;;;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    
    private final JwtProvider jwtProvider;
    
    public TokenRefreshResponse refreshToken(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new GeneralException(GeneralErrorCode.INVALID_REFRESH_TOKEN);
        }
        
        Long userId = jwtProvider.getUserIdFromToken(refreshToken);
        
        String newAccessToken = jwtProvider.createAccessToken(userId);
        
        return TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }
}