package com.capstone.fertility.domain.result.entity;

import com.capstone.fertility.domain.test.entity.TestSession;
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

    @Column(name = "score")
    @Setter
    private Integer score;

    @Column(name = "llm_advice", columnDefinition = "TEXT")
    @Setter
    private String llmAdvice;

    @Column(name = "medical_evidence", columnDefinition = "TEXT")
    @Setter
    private String medicalEvidence;
}
