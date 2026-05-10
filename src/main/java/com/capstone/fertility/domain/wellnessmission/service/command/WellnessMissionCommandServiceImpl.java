package com.capstone.fertility.domain.wellnessmission.service.command;

import com.capstone.fertility.domain.wellnessmission.dto.req.WellnessMissionReqDTO;
import com.capstone.fertility.domain.wellnessmission.dto.res.WellnessMissionResDTO;
import com.capstone.fertility.domain.wellnessmission.entity.WellnessMission;
import com.capstone.fertility.domain.wellnessmission.enums.Difficulty;
import com.capstone.fertility.domain.wellnessmission.exception.WellnessMissionException;
import com.capstone.fertility.domain.wellnessmission.exception.code.WellnessMissionErrorCode;
import com.capstone.fertility.domain.wellnessmission.repository.WellnessMissionRepository;
import com.capstone.fertility.domain.wellnessmission.support.WellnessMissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WellnessMissionCommandServiceImpl implements WellnessMissionCommandService {

    private final WellnessMissionRepository wellnessMissionRepository;

    @Override
    public WellnessMissionResDTO.MissionItem update(Long userId, Long missionId, WellnessMissionReqDTO.Update req) {
        WellnessMission mission = wellnessMissionRepository.findById(missionId)
                .orElseThrow(() -> new WellnessMissionException(WellnessMissionErrorCode.WELLNESS_MISSION_NOT_FOUND));

        if (!mission.getUser().getId().equals(userId)) {
            throw new WellnessMissionException(WellnessMissionErrorCode.WELLNESS_MISSION_NOT_OWNER);
        }
        if (!mission.isUserAdjustable()) {
            throw new WellnessMissionException(WellnessMissionErrorCode.WELLNESS_MISSION_NOT_ADJUSTABLE);
        }

        Difficulty parsedDifficulty = null;
        if (req.difficulty() != null && !req.difficulty().isBlank()) {
            try {
                parsedDifficulty = Difficulty.valueOf(req.difficulty().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new WellnessMissionException(WellnessMissionErrorCode.WELLNESS_MISSION_INVALID_FIELD);
            }
        }

        mission.adjust(req.frequencyCount(), req.durationValue(), parsedDifficulty);

        return WellnessMissionMapper.toItem(mission);
    }
}
