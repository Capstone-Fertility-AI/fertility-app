package com.capstone.fertility.domain.wellnessmission.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public class WellnessMissionReqDTO {

    /**
     * 사용자가 미션을 자기 페이스에 맞게 조정할 때 보내는 페이로드.
     * 모든 필드는 선택값이며, 보낸 필드만 반영된다.
     */
    @Schema(description = "웰니스 미션 수정 요청")
    public record Update(
            @Schema(description = "수행 빈도 횟수 (예: 1, 2, 3)", example = "2")
            @Min(value = 0, message = "frequencyCount는 0 이상이어야 합니다.")
            Integer frequencyCount,

            @Schema(description = "지속 시간 값 (예: 30분이면 30)", example = "20")
            @Min(value = 0, message = "durationValue는 0 이상이어야 합니다.")
            Integer durationValue,

            @Schema(description = "난이도", example = "EASY", allowableValues = {"EASY", "MEDIUM", "HARD"})
            String difficulty
    ) {}
}
