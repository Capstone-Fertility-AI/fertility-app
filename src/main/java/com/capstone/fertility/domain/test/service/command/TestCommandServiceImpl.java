package com.capstone.fertility.domain.test.service.command;

import com.capstone.fertility.domain.test.converter.TestConverter;
import com.capstone.fertility.domain.test.dto.req.StepSaveReqDTO;
import com.capstone.fertility.domain.test.dto.res.TestResDTO;
import com.capstone.fertility.domain.test.entity.TestSession;
import com.capstone.fertility.domain.test.enums.TestSessionStatus;
import com.capstone.fertility.domain.test.exception.TestException;
import com.capstone.fertility.domain.test.exception.code.TestErrorCode;
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

    @Override
    public void saveStep(Long userId, Long sessionId, StepSaveReqDTO request) {
        TestSession session = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new TestException(TestErrorCode.SESSION_NOT_FOUND));

        if (!session.getUser().getId().equals(userId)) {
            throw new TestException(TestErrorCode.SESSION_NOT_OWNER);
        }
        if (session.getStatus() != TestSessionStatus.IN_PROGRESS) {
            throw new TestException(TestErrorCode.SESSION_ALREADY_COMPLETED);
        }

        int step = request.getStep();
        session.setCurrentStep(step);

        if (request.getAge() != null) session.setAge(request.getAge());
        if (request.getHeight() != null) session.setHeight(request.getHeight());
        if (request.getWeight() != null) session.setWeight(request.getWeight());
        if (request.getMenarcheAge() != null) session.setMenarcheAge(request.getMenarcheAge());
        if (request.getParity() != null) session.setParity(request.getParity());
        if (request.getPcos() != null) session.setPcos(request.getPcos());
        if (request.getEndo() != null) session.setEndo(request.getEndo());
        if (request.getUf() != null) session.setUf(request.getUf());
        if (request.getPid() != null) session.setPid(request.getPid());
        if (request.getChlam() != null) session.setChlam(request.getChlam());
        if (request.getGon() != null) session.setGon(request.getGon());
        if (request.getSmokeLevel() != null) session.setSmokeLevel(request.getSmokeLevel());
        if (request.getBinge12() != null) session.setBinge12(request.getBinge12());
        if (request.getSleepHours() != null) session.setSleepHours(request.getSleepHours());

        // 9단계여도 수면 시간이 null이거나 0이면 미입력으로 간주 → IN_PROGRESS 유지. 1 이상일 때만 COMPLETED.
        Integer sleepHours = session.getSleepHours();
        boolean completedWithSleep = (step == 9 && sleepHours != null && sleepHours > 0);
        if (completedWithSleep) {
            session.setStatus(TestSessionStatus.COMPLETED);
        }

        testSessionRepository.save(session);
    }
}
