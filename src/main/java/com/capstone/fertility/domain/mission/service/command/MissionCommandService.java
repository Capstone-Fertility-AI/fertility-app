package com.capstone.fertility.domain.mission.service.command;

import com.capstone.fertility.domain.mission.dto.res.MissionResDTO;

public interface MissionCommandService {

    MissionResDTO.MissionCompleteDTO complete(Long userId, Long missionId);
}
