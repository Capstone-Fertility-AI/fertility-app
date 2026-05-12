package com.capstone.fertility.global.apiPayLoad.handler;

import com.capstone.fertility.global.apiPayLoad.ApiResponse;
import com.capstone.fertility.global.apiPayLoad.code.GeneralErrorCode;
import com.capstone.fertility.global.apiPayLoad.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GeneralExceptionAdvice {

    private final Environment environment;

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Void>> handleException(GeneralException ex) {
        return ResponseEntity.status(ex.getCode().getStatus())
                .body(ApiResponse.onFailure(ex.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.onFailure(GeneralErrorCode.REQUEST_BODY_VALIDATION_FAILED, message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadableBody(HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.onFailure(GeneralErrorCode.JSON_PARSE_ERROR));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<String>> handleDataAccess(DataAccessException e) {
        log.error("Database access failed", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.onFailure(GeneralErrorCode.INTERNAL_SERVER_ERROR, localDebugDetail(e)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleUnexpected(Exception e) {
        log.error("Unhandled server error", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.onFailure(GeneralErrorCode.INTERNAL_SERVER_ERROR, localDebugDetail(e)));
    }

    private String localDebugDetail(Exception e) {
        if (!Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            return null;
        }
        String message = e.getMessage();
        if (message == null || message.isBlank()) {
            return e.getClass().getSimpleName();
        }
        return e.getClass().getSimpleName() + ": " + message;
    }
}
