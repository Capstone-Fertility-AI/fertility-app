package com.capstone.fertility.domain.wellnessmission.exception.code;

import com.capstone.fertility.global.apiPayLoad.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WellnessMissionSuccessCode implements BaseSuccessCode {

    WELLNESS_MISSION_LIST_FETCHED(HttpStatus.OK, "WMISSION200_1", "내 웰니스 미션 목록을 조회했습니다."),
    WELLNESS_MISSION_UPDATED(HttpStatus.OK, "WMISSION200_2", "웰니스 미션을 수정했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
