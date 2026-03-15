package com.capstone.fertility.domain.test.service.query;

import com.capstone.fertility.domain.test.converter.TestConverter;
import com.capstone.fertility.domain.test.dto.res.TestResDTO;
import com.capstone.fertility.domain.test.entity.TestSession;
import com.capstone.fertility.domain.test.enums.TestSessionStatus;
import com.capstone.fertility.domain.test.repository.TestSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestQueryServiceImpl implements TestQueryService {

    private final TestSessionRepository testSessionRepository;

    @Override
    public Optional<TestResDTO.CurrentSessionResDTO> getCurrentSession(Long userId) {
        return testSessionRepository
                .findTopByUserIdAndStatusOrderByCreatedAtDesc(userId, TestSessionStatus.IN_PROGRESS)
                .map(TestConverter::toCurrentSessionResDTO);
    }
}
