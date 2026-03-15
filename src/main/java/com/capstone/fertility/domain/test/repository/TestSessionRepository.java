package com.capstone.fertility.domain.test.repository;

import com.capstone.fertility.domain.test.entity.TestSession;
import com.capstone.fertility.domain.test.enums.TestSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestSessionRepository extends JpaRepository<TestSession, Long> {

    Optional<TestSession> findByIdAndUser_Id(Long sessionId, Long userId);

    Optional<TestSession> findTopByUserIdAndStatusOrderByCreatedAtDesc(Long userId, TestSessionStatus status);
}
