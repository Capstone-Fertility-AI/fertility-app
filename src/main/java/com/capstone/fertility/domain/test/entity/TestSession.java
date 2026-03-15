package com.capstone.fertility.domain.test.entity;

import com.capstone.fertility.domain.test.enums.TestSessionStatus;
import com.capstone.fertility.domain.user.entity.User;
import com.capstone.fertility.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TestSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    @Setter
    private TestSessionStatus status = TestSessionStatus.IN_PROGRESS;

    /** 마지막 진행 단계 (1~9) */
    @Column(name = "current_step")
    @Setter
    private Integer currentStep;

    // --- 프론트엔드 입력 필드 (모두 null 허용) ---
    @Column(name = "age")
    @Setter
    private Integer age;

    @Column(name = "height")
    @Setter
    private Double height;

    @Column(name = "weight")
    @Setter
    private Double weight;

    @Column(name = "menarche_age")
    @Setter
    private Integer menarcheAge;

    @Column(name = "parity")
    @Setter
    private Integer parity;

    @Column(name = "pcos")
    @Setter
    private Integer pcos;

    @Column(name = "endo")
    @Setter
    private Integer endo;

    @Column(name = "uf")
    @Setter
    private Integer uf;

    @Column(name = "pid")
    @Setter
    private Integer pid;

    @Column(name = "chlam")
    @Setter
    private Integer chlam;

    @Column(name = "gon")
    @Setter
    private Integer gon;

    @Column(name = "smoke_level")
    @Setter
    private Integer smokeLevel;

    @Column(name = "binge12")
    @Setter
    private Integer binge12;

    @Column(name = "sleep_hours")
    @Setter
    private Integer sleepHours;

    // --- 검사 결과(제시 데이터) - COMPLETED 후 채워짐 ---
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
