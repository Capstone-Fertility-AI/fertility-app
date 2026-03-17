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
    INVALID_STEP(HttpStatus.BAD_REQUEST, "TEST400_2", "유효하지 않은 단계입니다. (1~9)");

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
