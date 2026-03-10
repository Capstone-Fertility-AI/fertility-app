package com.capstone.fertility.domain.user.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

// @Builder 어노테이션은 실제 데이터 필드가 있는 클래스에 사용하는 것이 좋습니다.
// 이 클래스는 다른 요청 DTO들을 묶어주는 컨테이너 역할을 하므로 빌더가 필요 없습니다.
public class UserReqDTO {

    /**
     * 프로필 수정(닉네임, 프로필 이미지)을 위한 요청 DTO
     * PATCH 요청이므로, 클라이언트가 보내지 않는 필드는 null이 될 수 있어야 합니다.
     */
    @Getter
    @NoArgsConstructor // Jackson 라이브러리가 JSON을 객체로 변환할 때 기본 생성자가 필요합니다.
    public static class UpdateProfileDTO {
        private String nickname;
        private String profileImageUrl;
    }

    // 예시: 만약 다른 기능(예: 로그인)을 위한 요청 DTO가 필요하다면 여기에 추가할 수 있습니다.
    /*
    @Getter
    @NoArgsConstructor
    public static class LoginDTO {
        private String email;
        private String password;
    }
    */
}