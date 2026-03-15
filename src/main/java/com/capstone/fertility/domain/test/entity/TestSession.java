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
    private TestSessionStatus status = TestSessionStatus.IN_PROGRESS;

    // --- 프론트엔드 입력 필드 (모두 null 허용) ---
    @Column(name = "age")
    private Integer age;

    @Column(name = "height")
    private Double height;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "menarche_age")
    private Integer menarcheAge;

    @Column(name = "parity")
    private Integer parity;

    @Column(name = "pcos")
    private Integer pcos;

    @Column(name = "endo")
    private Integer endo;

    @Column(name = "uf")
    private Integer uf;

    @Column(name = "pid")
    private Integer pid;

    @Column(name = "chlam")
    private Integer chlam;

    @Column(name = "gon")
    private Integer gon;

    @Column(name = "smoke_level")
    private Integer smokeLevel;

    @Column(name = "binge12")
    private Integer binge12;

    @Column(name = "sleep_hours")
    @Setter
    private Integer sleepHours;
}
