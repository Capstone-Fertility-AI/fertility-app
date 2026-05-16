package com.capstone.fertility.domain.mission.exception.code;

import com.capstone.fertility.global.apiPayLoad.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MissionSuccessCode implements BaseSuccessCode {

    MISSION_HISTORY_FETCHED(HttpStatus.OK, "MISSION200_2", "새싹 성장 기록을 조회했습니다."),
    MISSION_COLLECTIONS_FETCHED(HttpStatus.OK, "MISSION200_3", "꽃 도감을 조회했습니다."),
    SPROUT_CYCLE_RESET(HttpStatus.OK, "MISSION200_4", "재검사 후 새싹 성장이 Lv.1부터 다시 시작되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
