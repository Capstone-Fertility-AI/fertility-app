package com.capstone.fertility.domain.test.exception.code;

import com.capstone.fertility.global.apiPayLoad.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TestErrorCode implements BaseErrorCode {

    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "TEST404_1", "해당 검사 세션을 찾을 수 없습니다."),
    NO_IN_PROGRESS_SESSION(HttpStatus.NOT_FOUND, "TEST404_2", "진행 중인 검사 세션이 없습니다."),
    SESSION_NOT_OWNED(HttpStatus.FORBIDDEN, "TEST403_1", "본인의 검사 세션이 아닙니다."),
    SESSION_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "TEST400_1", "이미 완료된 검사 세션입니다."),
    INVALID_STEP(HttpStatus.BAD_REQUEST, "TEST400_2", "유효하지 않은 단계입니다. (1~9)"),
    INVALID_AGE(HttpStatus.BAD_REQUEST, "TEST400_3", "나이는 15~44 사이여야 합니다."),
    INVALID_MENARCHE_AGE(HttpStatus.BAD_REQUEST, "TEST400_4", "초경 연령은 8~18 사이여야 합니다."),
    INVALID_STEP_DATA(HttpStatus.BAD_REQUEST, "TEST400_5", "해당 단계의 입력값이 올바르지 않습니다."),
    RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "TEST404_3", "해당 검사 결과를 찾을 수 없거나 아직 완료되지 않았습니다.");

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
