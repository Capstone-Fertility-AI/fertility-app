package com.capstone.fertility.domain.user.dto.res;

import lombok.Builder;

public class UserResDTO {

    @Builder
    public record LoginResDTO (
        String accessToken,
        String refreshToken,
        UserInfoDTO user
    ) {}

    @Builder
    public record UserInfoDTO (
        Long userId,
        String nickname,
        String profileImageUrl,
        boolean isTermsAgreed
    ) {}
}