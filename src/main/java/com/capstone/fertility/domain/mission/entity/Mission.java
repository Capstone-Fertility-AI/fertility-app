package com.capstone.fertility.domain.mission.entity;

import com.capstone.fertility.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "missions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long id;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Builder.Default
    @Column(name = "reward_exp", nullable = false)
    private int rewardExp = 0;
}
