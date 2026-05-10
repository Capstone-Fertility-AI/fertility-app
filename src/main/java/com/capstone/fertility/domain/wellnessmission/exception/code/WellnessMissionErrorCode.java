package com.capstone.fertility.domain.wellnessmission.exception.code;

import com.capstone.fertility.global.apiPayLoad.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WellnessMissionErrorCode implements BaseErrorCode {

    WELLNESS_MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "WMISSION404_1", "해당 웰니스 미션을 찾을 수 없습니다."),
    WELLNESS_MISSION_NOT_OWNER(HttpStatus.FORBIDDEN, "WMISSION403_1", "본인의 미션만 수정/조회할 수 있습니다."),
    WELLNESS_MISSION_NOT_ADJUSTABLE(HttpStatus.BAD_REQUEST, "WMISSION400_1", "이 미션은 사용자 조정이 허용되지 않습니다."),
    WELLNESS_MISSION_INVALID_FIELD(HttpStatus.BAD_REQUEST, "WMISSION400_2", "수정 요청 값이 올바르지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
