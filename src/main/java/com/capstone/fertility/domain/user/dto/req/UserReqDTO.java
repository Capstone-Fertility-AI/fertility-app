package com.capstone.fertility.domain.user.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserReqDTO {

    /**
     * 회원가입을 위한 요청 DTO
     */
    public record SignUpReqDTO(
            @NotBlank(message = "이메일은 필수 입력 값입니다.")
            @Email(message = "이메일 형식이 올바르지 않습니다.")
            String email,

            @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
            @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
            String password,

            @NotBlank(message = "닉네임은 필수 입력 값입니다.")
            @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
            String nickname
    ) {}

    /**
     * 프로필 수정(닉네임, 프로필 이미지)을 위한 요청 DTO
     * PATCH 요청이므로, 클라이언트가 보내지 않는 필드는 null이 될 수 있어야 합니다.
     */
    @Getter
    @NoArgsConstructor
    public static class UpdateProfileDTO {
        private String nickname;
        private String profileImageUrl;
    }
}