package com.capstone.fertility.domain.test.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 검사 단계 임시 저장 요청.
 * step(1~9)에 따라 해당 단계에서 입력한 필드만 담아 보냅니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StepSaveReqDTO {

    @NotNull(message = "단계(step)는 필수입니다.")
    @Min(value = 1, message = "단계는 1~9 사이여야 합니다.")
    @Max(value = 9, message = "단계는 1~9 사이여야 합니다.")
    private Integer step;

    // --- 단계별 필드 (해당 단계에서만 채움, 나머지는 null) ---
    private Integer age;           // 1
    private Double height;         // 2
    private Double weight;         // 3
    private Integer menarcheAge;   // 4
    private Integer parity;        // 5
    private Integer pcos;         // 6
    private Integer endo;
    private Integer uf;
    private Integer pid;
    private Integer chlam;
    private Integer gon;
    private Integer smokeLevel;    // 7
    private Integer binge12;       // 8
    private Integer sleepHours;    // 9
}
