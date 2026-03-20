package com.capstone.fertility.domain.result.service.query;

import com.capstone.fertility.domain.result.converter.ResultConverter;
import com.capstone.fertility.domain.result.dto.res.ResultResDTO;
import com.capstone.fertility.domain.result.entity.TestResult;
import com.capstone.fertility.domain.result.repository.TestResultRepository;
import com.capstone.fertility.global.apiPayLoad.code.GeneralErrorCode;
import com.capstone.fertility.global.apiPayLoad.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultQueryServiceImpl implements ResultQueryService {

    private static final int MIN_YEAR = 2000;
    private static final int MAX_YEAR = 2100;

    private final TestResultRepository testResultRepository;

    @Override
    public ResultResDTO.ResultHistoryDTO getHistory(Long userId, Integer year, Integer month) {
        int targetYear;
        int targetMonth;

        if (year == null && month == null) {
            LocalDate today = LocalDate.now();
            targetYear = today.getYear();
            targetMonth = today.getMonthValue();
        } else if (year != null && month != null) {
            targetYear = year;
            targetMonth = month;
        } else {
            throw new GeneralException(GeneralErrorCode.BAD_REQUEST);
        }

        validateYearMonth(targetYear, targetMonth);

        LocalDateTime startInclusive = LocalDateTime.of(LocalDate.of(targetYear, targetMonth, 1), LocalTime.MIN);
        LocalDateTime endExclusive = startInclusive.plusMonths(1);

        List<TestResult> rows = testResultRepository.findByUserIdAndCreatedAtInMonth(
                userId, startInclusive, endExclusive);

        List<ResultResDTO.ResultHistoryItemDTO> items = rows.stream()
                .map(ResultConverter::toHistoryItemDTO)
                .toList();

        return ResultResDTO.ResultHistoryDTO.builder()
                .year(targetYear)
                .month(targetMonth)
                .items(items)
                .build();
    }

    private void validateYearMonth(int year, int month) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new GeneralException(GeneralErrorCode.BAD_REQUEST);
        }
        if (month < 1 || month > 12) {
            throw new GeneralException(GeneralErrorCode.BAD_REQUEST);
        }
    }
}
