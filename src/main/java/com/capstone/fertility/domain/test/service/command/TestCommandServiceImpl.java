package com.capstone.fertility.domain.test.service.command;

import com.capstone.fertility.domain.test.converter.TestConverter;
import com.capstone.fertility.domain.test.dto.res.TestResDTO;
import com.capstone.fertility.domain.test.entity.TestSession;
import com.capstone.fertility.domain.test.repository.TestSessionRepository;
import com.capstone.fertility.domain.user.entity.User;
import com.capstone.fertility.domain.user.exception.UserException;
import com.capstone.fertility.domain.user.exception.code.UserErrorCode;
import com.capstone.fertility.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TestCommandServiceImpl implements TestCommandService {

    private final TestSessionRepository testSessionRepository;
    private final UserRepository userRepository;

    @Override
    public TestResDTO.CreateSessionResDTO createSession(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_ID_NOT_FOUND));

        TestSession session = TestConverter.toTestSession(user);
        TestSession saved = testSessionRepository.save(session);

        return TestConverter.toCreateSessionResDTO(saved);
    }
}
