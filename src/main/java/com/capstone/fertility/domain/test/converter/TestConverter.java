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

    /**
     * 진행 중인 세션 복구용 DTO로 변환 (저장된 모든 필드 + currentStep)
     */
    public static TestResDTO.CurrentSessionResDTO toCurrentSessionResDTO(TestSession session) {
        return TestResDTO.CurrentSessionResDTO.builder()
                .sessionId(session.getId())
                .currentStep(session.getCurrentStep())
                .age(session.getAge())
                .height(session.getHeight())
                .weight(session.getWeight())
                .menarcheAge(session.getMenarcheAge())
                .parity(session.getParity())
                .pcos(session.getPcos())
                .endo(session.getEndo())
                .uf(session.getUf())
                .pid(session.getPid())
                .chlam(session.getChlam())
                .gon(session.getGon())
                .smokeLevel(session.getSmokeLevel())
                .binge12(session.getBinge12())
                .sleepHours(session.getSleepHours())
                .build();
    }
}
