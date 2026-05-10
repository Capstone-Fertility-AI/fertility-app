package com.capstone.fertility.domain.wellnessmission.service;

import com.capstone.fertility.domain.wellnessmission.entity.WellnessMission;
import com.capstone.fertility.domain.wellnessmission.repository.WellnessMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * KST 기준 '오늘의 미션' 윈도우를 맞춘다.
 * 최신 검사 결과에 붙은 상위 3개 웰니스 미션의 serving 날짜가 오늘이 아니면 완료를 초기화하고 날짜만 오늘로 맞춘다(명세: 자정 이후 리셋).
 */
@Service
@RequiredArgsConstructor
public class WellnessMissionDailyRolloverService {

    public static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final WellnessMissionRepository wellnessMissionRepository;

    @Transactional
    public void ensureTodaysServingWindow(Long userId) {
        LocalDate today = LocalDate.now(KST);
        Long latestResultId = wellnessMissionRepository.findMaxTestResultIdByUserId(userId).orElse(null);
        if (latestResultId == null) {
            return;
        }
        List<WellnessMission> rows = wellnessMissionRepository.findByUser_IdAndTestResult_IdOrderByIdAsc(userId, latestResultId);
        if (rows.isEmpty()) {
            return;
        }
        List<WellnessMission> top3 = rows.size() > 3 ? rows.subList(0, 3) : rows;
        WellnessMission first = top3.get(0);
        if (first.getServingLocalDate() != null && first.getServingLocalDate().equals(today)) {
            return;
        }
        for (WellnessMission m : top3) {
            m.rolloverServingDay(today);
        }
    }
}
