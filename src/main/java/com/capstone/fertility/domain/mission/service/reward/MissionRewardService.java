package com.capstone.fertility.domain.mission.service.reward;

import com.capstone.fertility.domain.mission.entity.MissionSproutLog;
import com.capstone.fertility.domain.mission.entity.UserFlowerCollection;
import com.capstone.fertility.domain.mission.enums.FlowerType;
import com.capstone.fertility.domain.mission.enums.SproutLogAction;
import com.capstone.fertility.domain.mission.repository.MissionSproutLogRepository;
import com.capstone.fertility.domain.mission.repository.UserFlowerCollectionRepository;
import com.capstone.fertility.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 미션 완료/페널티 등으로 인한 경험치 보상을 일관되게 처리하는 도메인 서비스.
 * 정적 Mission, LLM 기반 WellnessMission 두 시스템에서 공통으로 사용한다.
 */
@Service
@RequiredArgsConstructor
public class MissionRewardService {

    /** 현재는 단일 꽃만 지급. 확장 시 후보 풀·랜덤 로직 추가. */
    private static final FlowerType FINAL_FLOWER = FlowerType.PEONY;

    private final MissionSproutLogRepository missionSproutLogRepository;
    private final UserFlowerCollectionRepository userFlowerCollectionRepository;

    /**
     * 미션 완료 보상 적용.
     *
     * @param expReward 0이면 EXP·레벨 변화 없이 마지막 미션 시각·로그만 반영(일일 상한 등).
     */
    public RewardResult grantMissionCompletion(User user, int expReward) {
        boolean leveledUp = expReward != 0 && user.addExp(expReward);
        user.updateLastMissionDate();

        missionSproutLogRepository.save(MissionSproutLog.builder()
                .user(user)
                .action(SproutLogAction.MISSION_COMPLETE)
                .expDelta(expReward)
                .build());

        FlowerType awarded = null;
        if (leveledUp && user.getCurrentLevel() >= 5) {
            awarded = tryAwardFlower(user);
        }

        return RewardResult.builder()
                .expGained(expReward)
                .currentExp(user.getCurrentExp())
                .currentLevel(user.getCurrentLevel())
                .isLevelUp(leveledUp)
                .newFlower(awarded)
                .build();
    }

    /**
     * 미접속 페널티: -10 EXP (명세). 레벨 강등 없음(EXP만 조정).
     */
    public void applyInactivityPenalty(User user) {
        user.addExp(-10);
        missionSproutLogRepository.save(MissionSproutLog.builder()
                .user(user)
                .action(SproutLogAction.INACTIVITY_PENALTY)
                .expDelta(-10)
                .build());
    }

    /**
     * Lv.5 도달 시 단일 꽃(PEONY) 1회 지급. 이미 도감에 있으면 null(멱등).
     */
    private FlowerType tryAwardFlower(User user) {
        List<UserFlowerCollection> owned = userFlowerCollectionRepository.findByUser_IdOrderByAchievedAtDesc(user.getId());
        for (UserFlowerCollection c : owned) {
            if (c.getFlowerType() == FINAL_FLOWER) {
                return null;
            }
        }
        userFlowerCollectionRepository.save(UserFlowerCollection.builder()
                .user(user)
                .flowerType(FINAL_FLOWER)
                .achievedAt(LocalDateTime.now())
                .build());
        return FINAL_FLOWER;
    }
}
