package com.capstone.fertility.domain.wellnessmission.service.command;

import com.capstone.fertility.domain.wellnessmission.dto.req.WellnessMissionReqDTO;
import com.capstone.fertility.domain.wellnessmission.dto.res.WellnessMissionResDTO;

public interface WellnessMissionCommandService {
    WellnessMissionResDTO.MissionItem update(Long userId, Long missionId, WellnessMissionReqDTO.Update req);
}
