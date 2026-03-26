package com.capstone.fertility.domain.result.entity;

import com.capstone.fertility.domain.result.enums.RiskLevel;
import com.capstone.fertility.domain.test.entity.TestSession;
import com.capstone.fertility.domain.user.entity.User;
import com.capstone.fertility.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test_results")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TestResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private TestSession testSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "ai_score")
    private Integer aiScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", length = 20)
    private RiskLevel riskLevel;

    @Column(name = "risk_probability")
    private Double riskProbability;

    @Column(name = "top1_factor")
    private String top1Factor;

    @Column(name = "top2_factor")
    private String top2Factor;

    @Column(name = "top3_factor")
    private String top3Factor;

    @Column(name = "llm_advice", columnDefinition = "TEXT")
    private String llmAdvice;

    @Column(name = "medical_evidence", columnDefinition = "TEXT")
    private String medicalEvidence;
}
