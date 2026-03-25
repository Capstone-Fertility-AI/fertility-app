package com.capstone.fertility.domain.test.exception.code;

import com.capstone.fertility.global.apiPayLoad.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TestErrorCode implements BaseErrorCode {

    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "TEST404_1", "해당 검사 세션을 찾을 수 없습니다."),
    SESSION_NOT_OWNER(HttpStatus.FORBIDDEN, "TEST403_1", "본인의 검사 세션만 수정할 수 있습니다."),
    SESSION_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "TEST400_1", "이미 완료된 검사 세션은 수정할 수 없습니다."),
    INVALID_STEP(HttpStatus.BAD_REQUEST, "TEST400_2", "유효하지 않은 단계입니다. (남성 1~11, 여성 1~9)"),
    RESULT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "TEST400_3", "이미 제출된 검사 세션입니다. 결과가 존재합니다."),
    INVALID_PSS_ANSWERS(HttpStatus.BAD_REQUEST, "TEST400_4", "PSS 10문항은 각 0~4점이며 정확히 10개여야 합니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "TEST400_5", "요청 데이터가 유효하지 않습니다."),
    SESSION_GENDER_MISMATCH(HttpStatus.BAD_REQUEST, "TEST400_6", "요청 성별과 세션 성별이 일치하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
