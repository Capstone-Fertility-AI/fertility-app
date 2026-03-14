package com.capstone.fertility.domain.test.converter;

import com.capstone.fertility.domain.test.dto.res.TestResDTO;
import com.capstone.fertility.domain.test.entity.TestSession;
import com.capstone.fertility.domain.test.enums.TestSessionStatus;
import com.capstone.fertility.domain.user.entity.User;

public class TestConverter {

    /**
     * 사용자와 연결된 새 검사 세션 엔티티 생성 (초기 상태 IN_PROGRESS, 입력 필드는 모두 null)
     */
    public static TestSession toTestSession(User user) {
        return TestSession.builder()
                .user(user)
                .status(TestSessionStatus.IN_PROGRESS)
                .build();
    }

    /**
     * 검사 세션 생성 응답 DTO로 변환 (sessionId 반환용)
     */
    public static TestResDTO.CreateSessionResDTO toCreateSessionResDTO(TestSession session) {
        return TestResDTO.CreateSessionResDTO.builder()
                .sessionId(session.getId())
                .build();
    }
}
