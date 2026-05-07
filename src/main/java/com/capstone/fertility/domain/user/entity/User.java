package com.capstone.fertility.domain.user.entity;

import com.capstone.fertility.domain.user.enums.Gender;
import com.capstone.fertility.domain.user.enums.LoginType;
import com.capstone.fertility.domain.user.enums.UserStatus;
import com.capstone.fertility.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 보호 (Lombok)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", length = 20, nullable = false)
    private LoginType loginType;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "partner_code", length = 50, unique = true)
    private String partnerCode;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Builder.Default
    @Column(name = "current_level")
    private int currentLevel = 1;

    @Builder.Default
    @Column(name = "current_exp")
    private int currentExp = 0;

    @Column(name = "is_terms_agreed", nullable = false)
    private boolean isTermsAgreed;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @Column(name = "last_mission_date")
    private LocalDateTime lastMissionDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    // 파트너(자기 참조 1:1 관계) - 반드시 지연 로딩(LAZY) 적용!
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private User partner;

    // --- 비즈니스 편의 메서드 ---

    /**
     * 마지막 로그인 시간 업데이트
     */
    public void updateLastLoginDate() {
        this.lastLoginDate = LocalDateTime.now();
    }

    /**
     * 마지막 미션 완료 시각 갱신
     */
    public void updateLastMissionDate() {
        this.lastMissionDate = LocalDateTime.now();
    }

    /**
     * 회원가입 시 추가 정보 입력 (온보딩)
     */
    public void updateProfile(Gender gender, Integer birthYear, boolean isTermsAgreed) {
        this.gender = gender;
        this.birthYear = birthYear;
        this.isTermsAgreed = isTermsAgreed;
    }

    public void updateProfile(String nickname, String profileImageUrl) {
        // PATCH 요청의 특성을 반영하여, null이 아닌 값만 변경합니다.
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }
    /**
     * 파트너 연결
     */
    public void linkPartner(User partner) {
        this.partner = partner;
        // 양방향 세팅이 필요하다면 partner.setPartner(this) 등 추가 구현 가능
    }

    /**
     * 경험치 획득 및 레벨업 로직
     */
    public void addExp(int exp) {
        this.currentExp += exp;

        // 미접속 페널티 등으로 경험치가 0 이하로 떨어지는 것을 방지
        if (this.currentExp < 0) {
            this.currentExp = 0;
        }

        updateLevel();
    }

    /**
     * 누적 경험치에 따른 레벨 업데이트 로직
     */
    private void updateLevel() {
        if (this.currentExp >= 250) {
            this.currentLevel = 5;
        } else if (this.currentExp >= 200) {
            this.currentLevel = 4;
        } else if (this.currentExp >= 150) {
            this.currentLevel = 3;
        } else if (this.currentExp >= 100) {
            this.currentLevel = 2;
        } else {
            this.currentLevel = 1;
        }
    }
}