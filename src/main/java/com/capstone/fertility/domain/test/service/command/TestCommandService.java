package com.capstone.fertility.domain.test.service.command;

import com.capstone.fertility.domain.test.dto.req.TestReqDTO;
import com.capstone.fertility.domain.test.dto.res.TestResDTO;

public interface TestCommandService {

    /**
     * 인증된 사용자에 대해 새 검사 세션을 생성하고 sessionId를 반환합니다.
     */
    TestResDTO.CreateSessionResDTO createSession(Long userId);

    /**
     * 검사 단계(1~9) 입력을 임시 저장합니다. 본인 세션만 수정 가능하며, IN_PROGRESS 상태일 때만 가능합니다.
     * 9단계에서 수면 시간(sleepHours)을 1 이상 입력했을 때만 자동으로 COMPLETED 처리됩니다.
     */
    void saveStep(Long userId, Long sessionId, TestReqDTO.StepSaveReqDTO request);
}
