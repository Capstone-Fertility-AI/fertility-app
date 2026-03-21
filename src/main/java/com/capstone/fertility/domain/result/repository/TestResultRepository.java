package com.capstone.fertility.domain.result.repository;

import com.capstone.fertility.domain.result.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    @Query("""
            SELECT t FROM TestResult t
            WHERE t.userId = :userId
              AND t.createdAt >= :startInclusive
              AND t.createdAt < :endExclusive
            ORDER BY t.createdAt DESC
            """)
    List<TestResult> findByUserIdAndCreatedAtInMonth(
            @Param("userId") Long userId,
            @Param("startInclusive") LocalDateTime startInclusive,
            @Param("endExclusive") LocalDateTime endExclusive
    );
}
