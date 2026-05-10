package com.capstone.fertility.domain.wellnessmission.support;

import com.capstone.fertility.domain.wellnessmission.dto.res.WellnessMissionResDTO;
import com.capstone.fertility.domain.wellnessmission.entity.WellnessMission;

public final class WellnessMissionMapper {

    private WellnessMissionMapper() {}

    public static WellnessMissionResDTO.MissionItem toItem(WellnessMission e) {
        return WellnessMissionResDTO.MissionItem.builder()
                .missionId(e.getId())
                .resultId(e.getTestResult() != null ? e.getTestResult().getId() : null)
                .title(e.getTitle())
                .description(e.getDescription())
                .linkedFactor(e.getLinkedFactor())
                .category(e.getCategory() != null ? e.getCategory().name() : null)
                .frequency(WellnessMissionResDTO.Frequency.builder()
                        .type(e.getFrequencyType() != null ? e.getFrequencyType().name() : null)
                        .count(e.getFrequencyCount())
                        .unit(e.getFrequencyUnit())
                        .build())
                .duration(WellnessMissionResDTO.Duration.builder()
                        .value(e.getDurationValue())
                        .unit(e.getDurationUnit())
                        .build())
                .difficulty(e.getDifficulty() != null ? e.getDifficulty().name() : null)
                .userAdjustable(e.isUserAdjustable())
                .userAdjusted(e.isUserAdjusted())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
