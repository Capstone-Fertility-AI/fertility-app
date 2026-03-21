package com.capstone.fertility.domain.result.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DB 테이블 test_results (스펙: Test_Results) 매핑.
 */
@Entity
@Table(name = "test_results")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "ai_score", nullable = false)
    private Integer aiScore;

    @Column(name = "risk_level", nullable = false, length = 20)
    private String riskLevel;

    @Column(name = "top1_factor", nullable = false, length = 100)
    private String top1Factor;

    @Column(name = "top2_factor", length = 100)
    private String top2Factor;

    @Column(name = "top3_factor", length = 100)
    private String top3Factor;

    @Lob
    @Column(name = "llm_advice")
    private String llmAdvice;

    @Lob
    @Column(name = "medical_evidence")
    private String medicalEvidence;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
