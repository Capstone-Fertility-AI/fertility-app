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

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 미션 완료/페널티 등으로 인한 경험치 보상을 일관되게 처리하는 도메인 서비스.
 * 정적 Mission, LLM 기반 WellnessMission 두 시스템에서 공통으로 사용한다.
 */
@Service
@RequiredArgsConstructor
public class MissionRewardService {

    private static final List<FlowerType> SPEC_FINAL_FLOWERS = List.of(
            FlowerType.PEONY,
            FlowerType.BABYS_BREATH,
            FlowerType.LAVENDER
    );

    private final MissionSproutLogRepository missionSproutLogRepository;
    private final UserFlowerCollectionRepository userFlowerCollectionRepository;
    private final SecureRandom random = new SecureRandom();

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
     * Lv.5 도달 시 명세 3.1의 3종 꽃 중, 아직 없는 종류를 우선 랜덤 획득.
     */
    private FlowerType tryAwardFlower(User user) {
        List<UserFlowerCollection> owned = userFlowerCollectionRepository.findByUser_IdOrderByAchievedAtDesc(user.getId());
        Set<FlowerType> ownedTypes = new HashSet<>();
        for (UserFlowerCollection c : owned) {
            ownedTypes.add(c.getFlowerType());
        }

        List<FlowerType> candidates = new ArrayList<>();
        for (FlowerType t : SPEC_FINAL_FLOWERS) {
            if (!ownedTypes.contains(t)) {
                candidates.add(t);
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }

        FlowerType picked = candidates.get(random.nextInt(candidates.size()));
        userFlowerCollectionRepository.save(UserFlowerCollection.builder()
                .user(user)
                .flowerType(picked)
                .achievedAt(LocalDateTime.now())
                .build());
        return picked;
    }
}
