package com.capstone.fertility.domain.test.service.command;

import com.capstone.fertility.domain.test.converter.TestConverter;
import com.capstone.fertility.domain.test.dto.req.TestReqDTO;
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

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class TestCommandServiceImpl implements TestCommandService {

    private static final int AGE_MIN = 15;
    private static final int AGE_MAX = 44;
    private static final int MENARCHE_AGE_MIN = 8;
    private static final int MENARCHE_AGE_MAX = 18;

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
    public void saveStep(Long sessionId, Long userId, TestReqDTO.StepSaveReqDTO request) {
        TestSession session = testSessionRepository.findByIdAndUser_Id(sessionId, userId)
                .orElseThrow(() -> new TestException(TestErrorCode.SESSION_NOT_FOUND));

        if (session.getStatus() == TestSessionStatus.COMPLETED) {
            throw new TestException(TestErrorCode.SESSION_ALREADY_COMPLETED);
        }

        int step = request.getStep();
        if (step < 1 || step > 9) {
            throw new TestException(TestErrorCode.INVALID_STEP);
        }

        Map<String, Object> data = request.getData() != null ? request.getData() : Map.of();
        session.setCurrentStep(step);
        applyStepData(session, step, data);
        // Dirty Checking으로 flush
    }

    @Override
    public TestResDTO.SubmitResDTO submit(Long sessionId, Long userId) {
        TestSession session = testSessionRepository.findByIdAndUser_Id(sessionId, userId)
                .orElseThrow(() -> new TestException(TestErrorCode.SESSION_NOT_FOUND));

        if (session.getStatus() == TestSessionStatus.COMPLETED) {
            throw new TestException(TestErrorCode.SESSION_ALREADY_COMPLETED);
        }

        session.setStatus(TestSessionStatus.COMPLETED);
        return TestResDTO.SubmitResDTO.builder().resultId(session.getId()).build();
    }

    private void applyStepData(TestSession session, int step, Map<String, Object> data) {
        switch (step) {
            case 1 -> {
                Integer age = getInt(data, "age");
                if (age != null && (age < AGE_MIN || age > AGE_MAX)) {
                    throw new TestException(TestErrorCode.INVALID_AGE);
                }
                session.setAge(age);
            }
            case 2 -> session.setHeight(getDouble(data, "height"));
            case 3 -> session.setWeight(getDouble(data, "weight"));
            case 4 -> {
                Integer menarcheAge = getInt(data, "menarcheAge");
                if (menarcheAge != null && (menarcheAge < MENARCHE_AGE_MIN || menarcheAge > MENARCHE_AGE_MAX)) {
                    throw new TestException(TestErrorCode.INVALID_MENARCHE_AGE);
                }
                session.setMenarcheAge(menarcheAge);
            }
            case 5 -> session.setParity(getInt(data, "parity"));
            case 6 -> {
                session.setPcos(getInt(data, "pcos"));
                session.setEndo(getInt(data, "endo"));
                session.setUf(getInt(data, "uf"));
                session.setPid(getInt(data, "pid"));
                session.setChlam(getInt(data, "chlam"));
                session.setGon(getInt(data, "gon"));
            }
            case 7 -> session.setSmokeLevel(getInt(data, "smokeLevel"));
            case 8 -> session.setBinge12(getInt(data, "binge12"));
            case 9 -> session.setSleepHours(getInt(data, "sleepHours"));
            default -> throw new TestException(TestErrorCode.INVALID_STEP);
        }
    }

    private static Integer getInt(Map<String, Object> data, String key) {
        Object v = data.get(key);
        if (v == null) return null;
        if (v instanceof Number n) return n.intValue();
        if (v instanceof String s) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private static Double getDouble(Map<String, Object> data, String key) {
        Object v = data.get(key);
        if (v == null) return null;
        if (v instanceof Number n) return n.doubleValue();
        if (v instanceof String s) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
