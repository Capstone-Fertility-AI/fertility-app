package com.capstone.fertility.domain.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResDTO {
        private String accessToken;
        private String refreshToken;
        private UserInfoDTO user;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoDTO {
        private Long userId;
        private String nickname;
        private String profileImageUrl;
        private boolean isTermsAgreed;
    }
}