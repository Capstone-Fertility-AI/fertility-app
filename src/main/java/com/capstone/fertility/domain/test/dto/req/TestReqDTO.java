package com.capstone.fertility.domain.test.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * 검사 세션 생성 요청 DTO.
 * 현재는 Body 없이 토큰만 사용하지만, 확장성을 위해 빈 구조를 유지합니다.
 */
@Getter
@NoArgsConstructor
public class TestReqDTO {
    // 추후 프론트 입력 필드 추가 시 여기에 정의

    /** 단계별 임시 저장 요청 (POST /tests/{sessionId}/step) */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class StepSaveReqDTO {
        @NotNull(message = "step은 필수입니다.")
        @Min(value = 1, message = "step은 1~9 사이여야 합니다.")
        @Max(value = 9, message = "step은 1~9 사이여야 합니다.")
        private Integer step;

        /** 단계별 입력 데이터 (step에 따라 필드 구성 상이) */
        private Map<String, Object> data;
    }
}
