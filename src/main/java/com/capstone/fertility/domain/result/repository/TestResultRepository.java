package com.capstone.fertility.domain.result.repository;

import com.capstone.fertility.domain.result.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    /** resultId로 조회하며, 해당 결과의 세션이 주어진 userId 소유일 때만 반환 (결과 조회는 result 도메인에서만) */
    Optional<TestResult> findByIdAndTestSession_User_Id(Long resultId, Long userId);
}
