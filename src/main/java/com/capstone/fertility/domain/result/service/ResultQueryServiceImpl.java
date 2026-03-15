package com.capstone.fertility.domain.result.service;

import com.capstone.fertility.domain.result.converter.ResultConverter;
import com.capstone.fertility.domain.result.dto.res.ResultResDTO;
import com.capstone.fertility.domain.test.entity.TestSession;
import com.capstone.fertility.domain.test.enums.TestSessionStatus;
import com.capstone.fertility.domain.test.exception.TestException;
import com.capstone.fertility.domain.test.exception.code.TestErrorCode;
import com.capstone.fertility.domain.test.repository.TestSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultQueryServiceImpl implements ResultQueryService {

    private final TestSessionRepository testSessionRepository;

    @Override
    public ResultResDTO.ResultDetailResDTO getResultDetail(Long resultId, Long userId) {
        TestSession session = testSessionRepository.findByIdAndUser_Id(resultId, userId)
                .orElseThrow(() -> new TestException(TestErrorCode.RESULT_NOT_FOUND));

        if (session.getStatus() != TestSessionStatus.COMPLETED) {
            throw new TestException(TestErrorCode.RESULT_NOT_FOUND);
        }

        return ResultConverter.toResultDetailResDTO(session);
    }
}
