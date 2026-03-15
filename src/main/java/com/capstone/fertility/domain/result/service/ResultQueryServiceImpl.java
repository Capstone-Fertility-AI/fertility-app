package com.capstone.fertility.domain.result.service;

import com.capstone.fertility.domain.result.converter.ResultConverter;
import com.capstone.fertility.domain.result.dto.res.ResultResDTO;
import com.capstone.fertility.domain.result.entity.TestResult;
import com.capstone.fertility.domain.result.repository.TestResultRepository;
import com.capstone.fertility.domain.test.exception.TestException;
import com.capstone.fertility.domain.test.exception.code.TestErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultQueryServiceImpl implements ResultQueryService {

    private final TestResultRepository testResultRepository;

    @Override
    public ResultResDTO.ResultDetailResDTO getResultDetail(Long resultId, Long userId) {
        TestResult result = testResultRepository.findByIdAndTestSession_User_Id(resultId, userId)
                .orElseThrow(() -> new TestException(TestErrorCode.RESULT_NOT_FOUND));
        return ResultConverter.toResultDetailResDTO(result);
    }
}
