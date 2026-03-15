package com.capstone.fertility.domain.test.service.command;

import com.capstone.fertility.domain.test.dto.req.TestReqDTO;
import com.capstone.fertility.domain.test.dto.res.TestResDTO;

public interface TestCommandService {

    /**
     * 인증된 사용자에 대해 새 검사 세션을 생성하고 sessionId를 반환합니다.
     */
    TestResDTO.CreateSessionResDTO createSession(Long userId);

    /**
     * 해당 단계(step)의 입력값을 임시 저장하고 currentStep을 업데이트합니다.
     */
    void saveStep(Long sessionId, Long userId, TestReqDTO.StepSaveReqDTO request);

    /**
     * 세션을 최종 완료(COMPLETED)로 마킹합니다. 완료된 결과 조회용 resultId(sessionId)를 반환합니다.
     */
    TestResDTO.SubmitResDTO submit(Long sessionId, Long userId);
}
