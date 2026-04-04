package com.capstone.fertility.domain.report.dto.res;

import lombok.Builder;

public class ReportResDTO {

    @Builder
    public record DetailReport(
            Long resultId,
            String nickname,
            Integer age,
            String gender,
            Integer score,
            String riskLevel,
            String report
    ) {}
}
