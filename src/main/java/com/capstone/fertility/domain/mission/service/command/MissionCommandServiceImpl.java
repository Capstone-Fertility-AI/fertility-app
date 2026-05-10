package com.capstone.fertility.domain.mission.service.command;

import com.capstone.fertility.domain.mission.dto.res.MissionResDTO;
import com.capstone.fertility.domain.mission.entity.Mission;
import com.capstone.fertility.domain.mission.entity.UserMission;
import com.capstone.fertility.domain.mission.exception.MissionException;
import com.capstone.fertility.domain.mission.exception.code.MissionErrorCode;
import com.capstone.fertility.domain.mission.repository.MissionRepository;
import com.capstone.fertility.domain.mission.repository.UserMissionRepository;
import com.capstone.fertility.domain.mission.service.reward.MissionRewardService;
import com.capstone.fertility.domain.mission.service.reward.RewardResult;
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
    private final UserRepository userRepository;
    private final MissionRewardService missionRewardService;

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
                    .currentLevel(user.getCurrentLevel())
                    .requiredExpForCurrentLevel(user.getRequiredExpForCurrentLevel())
                    .isLevelUp(false)
                    .alreadyCompleted(true)
                    .newFlower(null)
                    .build();
        }

        UserMission userMission = UserMission.builder()
                .user(user)
                .mission(mission)
                .completedAt(LocalDateTime.now())
                .build();
        userMissionRepository.save(userMission);

        RewardResult reward = missionRewardService.grantMissionCompletion(user, mission.getRewardExp());

        return MissionResDTO.MissionCompleteDTO.builder()
                .expGained(reward.expGained())
                .currentExp(reward.currentExp())
                .currentLevel(reward.currentLevel())
                .requiredExpForCurrentLevel(user.getRequiredExpForCurrentLevel())
                .isLevelUp(reward.isLevelUp())
                .alreadyCompleted(false)
                .newFlower(reward.newFlower() != null ? reward.newFlower().name() : null)
                .build();
    }
}
