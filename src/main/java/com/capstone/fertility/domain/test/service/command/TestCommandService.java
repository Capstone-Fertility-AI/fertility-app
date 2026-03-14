package com.capstone.fertility.domain.test.service.command;

import com.capstone.fertility.domain.test.dto.res.TestResDTO;

public interface TestCommandService {

    /**
     * 인증된 사용자에 대해 새 검사 세션을 생성하고 sessionId를 반환합니다.
     */
    TestResDTO.CreateSessionResDTO createSession(Long userId);
}
