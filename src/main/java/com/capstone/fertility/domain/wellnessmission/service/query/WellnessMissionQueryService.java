package com.capstone.fertility.domain.wellnessmission.service.query;

import com.capstone.fertility.domain.wellnessmission.dto.res.WellnessMissionResDTO;

public interface WellnessMissionQueryService {
    WellnessMissionResDTO.MyMissions getMyMissions(Long userId);
}
