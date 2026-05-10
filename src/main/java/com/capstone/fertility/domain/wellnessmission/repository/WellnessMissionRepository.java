package com.capstone.fertility.domain.wellnessmission.repository;

import com.capstone.fertility.domain.wellnessmission.entity.WellnessMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WellnessMissionRepository extends JpaRepository<WellnessMission, Long> {

    List<WellnessMission> findAllByTestResultIdOrderByIdAsc(Long testResultId);

    List<WellnessMission> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByTestResultId(Long testResultId);
}
