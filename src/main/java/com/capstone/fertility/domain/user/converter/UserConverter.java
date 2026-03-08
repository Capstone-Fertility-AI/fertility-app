package com.capstone.fertility.domain.user.converter;

import com.capstone.fertility.domain.user.dto.res.UserResDTO;
import com.capstone.fertility.domain.user.entity.User;

public class UserConverter {

    public static UserResDTO.UserInfoDTO toUserInfoDTO(User user) {
        return UserResDTO.UserInfoDTO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .isTermsAgreed(user.isTermsAgreed())
                .build();
    }

    // 로그인 응답 DTO (token + userInfo)
    public static UserResDTO.LoginResDTO toLoginResDTO(String accessToken, String refreshToken, User user) {
        return UserResDTO.LoginResDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(toUserInfoDTO(user)) // 위에서 만든 유저 정보 DTO를 조립
                .build();
    }

}