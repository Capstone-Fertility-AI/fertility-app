package com.capstone.fertility.domain.wellnessmission.service.query;

import com.capstone.fertility.domain.wellnessmission.dto.res.WellnessMissionResDTO;
import com.capstone.fertility.domain.wellnessmission.entity.WellnessMission;
import com.capstone.fertility.domain.wellnessmission.repository.WellnessMissionRepository;
import com.capstone.fertility.domain.wellnessmission.service.WellnessMissionDailyRolloverService;
import com.capstone.fertility.domain.wellnessmission.support.WellnessMissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WellnessMissionQueryServiceImpl implements WellnessMissionQueryService {

    private final WellnessMissionRepository wellnessMissionRepository;
    private final WellnessMissionDailyRolloverService wellnessMissionDailyRolloverService;

    @Override
    public WellnessMissionResDTO.MyMissions getMyMissions(Long userId) {
        List<WellnessMission> missions = wellnessMissionRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        List<WellnessMissionResDTO.MissionItem> items = missions.stream()
                .map(WellnessMissionMapper::toItem)
                .toList();
        return WellnessMissionResDTO.MyMissions.builder()
                .total(items.size())
                .missions(items)
                .build();
    }

    @Override
    @Transactional
    public WellnessMissionResDTO.MyMissions getTodayMissions(Long userId) {
        wellnessMissionDailyRolloverService.ensureTodaysServingWindow(userId);
        LocalDate today = LocalDate.now(WellnessMissionDailyRolloverService.KST);
        Long latestResultId = wellnessMissionRepository.findMaxTestResultIdByUserId(userId).orElse(null);
        if (latestResultId == null) {
            return WellnessMissionResDTO.MyMissions.builder()
                    .total(0)
                    .missions(List.of())
                    .build();
        }
        List<WellnessMission> rows = wellnessMissionRepository.findByUser_IdAndTestResult_IdOrderByIdAsc(userId, latestResultId);
        List<WellnessMission> top3 = rows.size() > 3 ? rows.subList(0, 3) : rows;
        List<WellnessMissionResDTO.MissionItem> items = new ArrayList<>();
        for (WellnessMission m : top3) {
            if (m.getServingLocalDate() != null && m.getServingLocalDate().equals(today)) {
                items.add(WellnessMissionMapper.toItem(m));
            }
        }
        return WellnessMissionResDTO.MyMissions.builder()
                .total(items.size())
                .missions(items)
                .build();
    }
}
