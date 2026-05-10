package com.capstone.fertility.domain.mission.service.command;

import com.capstone.fertility.domain.mission.dto.res.MissionResDTO;
import com.capstone.fertility.domain.mission.entity.Mission;
import com.capstone.fertility.domain.mission.entity.MissionSproutLog;
import com.capstone.fertility.domain.mission.entity.UserMission;
import com.capstone.fertility.domain.mission.enums.SproutLogAction;
import com.capstone.fertility.domain.mission.exception.MissionException;
import com.capstone.fertility.domain.mission.exception.code.MissionErrorCode;
import com.capstone.fertility.domain.mission.repository.MissionRepository;
import com.capstone.fertility.domain.mission.repository.MissionSproutLogRepository;
import com.capstone.fertility.domain.mission.repository.UserMissionRepository;
import com.capstone.fertility.domain.user.entity.User;
import com.capstone.fertility.domain.user.exception.UserException;
import com.capstone.fertility.domain.user.exception.code.UserErrorCode;
import com.capstone.fertility.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionCommandServiceImpl implements MissionCommandService {

    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final MissionSproutLogRepository missionSproutLogRepository;
    private final UserRepository userRepository;

    @Override
    public MissionResDTO.MissionCompleteDTO complete(Long userId, Long missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionException(MissionErrorCode.MISSION_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_ID_NOT_FOUND));

        Optional<UserMission> existing = userMissionRepository.findByUser_IdAndMission_Id(userId, missionId);
        if (existing.isPresent()) {
            return MissionResDTO.MissionCompleteDTO.builder()
                    .expGained(0)
                    .currentExp(user.getCurrentExp())
                    .isLevelUp(false)
                    .alreadyCompleted(true)
                    .build();
        }

        LocalDateTime now = LocalDateTime.now();
        UserMission userMission = UserMission.builder()
                .user(user)
                .mission(mission)
                .completedAt(now)
                .build();
        userMissionRepository.save(userMission);

        int reward = mission.getRewardExp();
        int levelBefore = user.getCurrentLevel();
        user.addExp(reward);
        user.updateLastMissionDate();
        boolean levelUp = user.getCurrentLevel() > levelBefore;

        missionSproutLogRepository.save(MissionSproutLog.builder()
                .user(user)
                .action(SproutLogAction.MISSION_COMPLETE)
                .expDelta(reward)
                .build());

        return MissionResDTO.MissionCompleteDTO.builder()
                .expGained(reward)
                .currentExp(user.getCurrentExp())
                .isLevelUp(levelUp)
                .alreadyCompleted(false)
                .build();
    }
}
