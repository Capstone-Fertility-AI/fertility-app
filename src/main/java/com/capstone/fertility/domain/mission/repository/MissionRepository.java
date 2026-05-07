package com.capstone.fertility.domain.mission.repository;

import com.capstone.fertility.domain.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {
}
