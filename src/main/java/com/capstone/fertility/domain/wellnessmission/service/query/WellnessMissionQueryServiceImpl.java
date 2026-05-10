package com.capstone.fertility.domain.wellnessmission.service.query;

import com.capstone.fertility.domain.wellnessmission.dto.res.WellnessMissionResDTO;
import com.capstone.fertility.domain.wellnessmission.entity.WellnessMission;
import com.capstone.fertility.domain.wellnessmission.repository.WellnessMissionRepository;
import com.capstone.fertility.domain.wellnessmission.support.WellnessMissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WellnessMissionQueryServiceImpl implements WellnessMissionQueryService {

    private final WellnessMissionRepository wellnessMissionRepository;

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
}
