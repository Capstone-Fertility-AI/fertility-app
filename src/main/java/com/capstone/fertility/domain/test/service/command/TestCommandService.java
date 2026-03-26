package com.capstone.fertility.domain.test.service.command;

import com.capstone.fertility.domain.test.dto.req.TestReqDTO;
import com.capstone.fertility.domain.test.dto.res.TestResDTO;

public interface TestCommandService {

    /**
     * 0단계: 성별 선택 후 세션 생성
     */
    TestResDTO.CreateSessionResDTO start(Long userId, TestReqDTO.Start request);

    /**
     * 남성 전용 임시 저장(1~11)
     */
    void saveMaleStep(Long userId, Long sessionId, TestReqDTO.MaleStepSave request);

    /**
     * 여성 전용 임시 저장(1~9)
     */
    void saveFemaleStep(Long userId, Long sessionId, TestReqDTO.FemaleStepSave request);
}
