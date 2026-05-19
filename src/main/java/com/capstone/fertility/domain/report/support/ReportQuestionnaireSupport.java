package com.capstone.fertility.domain.report.support;

import com.capstone.fertility.domain.report.dto.res.ReportResDTO;
import com.capstone.fertility.domain.test.entity.TestSession;
import com.capstone.fertility.domain.user.enums.Gender;
import com.capstone.fertility.global.ai.mapping.AiLifestyleCategoryMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 상세 리포트 {@code questionnaireGroups} — 프론트 healthReportApi 기준 라벨(키·몸무게·흡연·음주).
 */
public final class ReportQuestionnaireSupport {

    public static final String LABEL_HEIGHT = "키";
    public static final String LABEL_WEIGHT = "몸무게";
    public static final String LABEL_SMOKE = "흡연";
    public static final String LABEL_DRINK = "음주";

    private ReportQuestionnaireSupport() {
    }

    public static List<ReportResDTO.QuestionnaireGroup> buildFrom(TestSession session) {
        if (session == null) {
            return List.of();
        }

        List<ReportResDTO.QuestionnaireRow> rows = new ArrayList<>();

        addRow(rows, LABEL_HEIGHT, formatHeight(session.getHeight()));
        addRow(rows, LABEL_WEIGHT, formatWeight(session.getWeight()));
        addRow(rows, LABEL_SMOKE, formatSmoke(session));
        addRow(rows, LABEL_DRINK, formatDrink(session.getDrinkStatus()));

        if (rows.isEmpty()) {
            return List.of();
        }

        return List.of(ReportResDTO.QuestionnaireGroup.builder()
                .title("검사 응답")
                .rows(rows)
                .build());
    }

    private static void addRow(List<ReportResDTO.QuestionnaireRow> rows, String label, String value) {
        if (value != null && !value.isBlank()) {
            rows.add(ReportResDTO.QuestionnaireRow.builder()
                    .label(label)
                    .value(value)
                    .build());
        }
    }

    private static String formatHeight(Double heightCm) {
        if (heightCm == null) {
            return null;
        }
        if (heightCm == Math.rint(heightCm)) {
            return ((int) Math.rint(heightCm)) + " cm";
        }
        return String.format("%.1f cm", heightCm);
    }

    private static String formatWeight(Double weightKg) {
        if (weightKg == null) {
            return null;
        }
        if (weightKg == Math.rint(weightKg)) {
            return ((int) Math.rint(weightKg)) + " kg";
        }
        return String.format("%.1f kg", weightKg);
    }

    private static String formatSmoke(TestSession session) {
        if (session.getGender() == Gender.F) {
            return formatFemaleSmokeLabel(session.getSmokeLevel());
        }
        return formatMaleSmokeLabel(session.getSmokeStatus());
    }

    private static String formatMaleSmokeLabel(String smokeStatus) {
        if (smokeStatus == null || smokeStatus.isBlank()) {
            return null;
        }
        String v = smokeStatus.trim();
        return switch (v) {
            case "NONE", "안 피움" -> "안 피움";
            case "SOMETIMES", "가끔 피움" -> "가끔 피움";
            case "DAILY", "매일 피움" -> "매일 피움";
            default -> v;
        };
    }

    private static String formatFemaleSmokeLabel(Integer smokeLevel) {
        if (smokeLevel == null) {
            return null;
        }
        return switch (AiLifestyleCategoryMapper.mapFemaleSmokeLevelToAi(smokeLevel)) {
            case 0 -> "안 피움";
            case 1 -> "가끔 피움";
            case 2 -> "매일 피움";
            default -> null;
        };
    }

    private static String formatDrink(String drinkStatus) {
        if (drinkStatus == null || drinkStatus.isBlank()) {
            return null;
        }
        String v = drinkStatus.trim();
        return switch (v) {
            case "NONE", "안 마심" -> "안 마심";
            case "SOMETIMES", "월 1~3회" -> "월 1~3회";
            case "WEEKLY", "주 1회 이상" -> "주 1회 이상";
            default -> v;
        };
    }
}
