package com.capstone.fertility.global.oauth.service;

/*
“카카오 사용자 정보를 우리 DB User와 매핑하고 JWT 발급” 책임
소셜 종류(KAKAO, GOOGLE, NAVER 등)가 늘어나도
이 서비스는 공통 “소셜 사용자 처리” 로 사용할 수 있음.
 */

import com.capstone.fertility.domain.user.entity.User;
import com.capstone.fertility.domain.user.repository.UserRepository;
import com.capstone.fertility.global.oauth.model.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OauthUserService {

    private final UserRepository userRepository;

    @Transactional
    public User handleKakaoUser(KakaoUserInfo info) {
        return userRepository.findByKakaoId(info.getKakaoId())
                .orElseGet(() -> createKakaoUser(info));
    }

    private User createKakaoUser(KakaoUserInfo info) {
        User user = User.builder()
                .kakaoId(info.getKakaoId())
                .nickname(info.getNickname())
                .profileImageUrl(info.getProfileImageUrl())
                .build();

        return userRepository.save(user);
    }

}
