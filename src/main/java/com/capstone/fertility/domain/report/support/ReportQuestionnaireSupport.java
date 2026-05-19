package com.capstone.fertility.domain.report.support;

import com.capstone.fertility.domain.report.dto.res.ReportResDTO;
import com.capstone.fertility.domain.test.entity.TestSession;
import com.capstone.fertility.domain.test.support.SleepInputSupport;
import com.capstone.fertility.domain.user.enums.Gender;
import com.capstone.fertility.global.ai.mapping.AiLifestyleCategoryMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 상세 리포트 {@code questionnaireGroups} — 프론트 신체 / 생활습관 그룹.
 */
public final class ReportQuestionnaireSupport {

    public static final String GROUP_BODY = "신체";
    public static final String GROUP_LIFESTYLE = "생활습관";

    public static final String LABEL_AGE = "나이";
    public static final String LABEL_HEIGHT = "키";
    public static final String LABEL_WEIGHT = "몸무게";
    public static final String LABEL_SMOKE = "흡연";
    public static final String LABEL_DRINK = "음주";
    public static final String LABEL_SLEEP = "수면";

    private ReportQuestionnaireSupport() {
    }

    public static List<ReportResDTO.QuestionnaireGroup> buildFrom(TestSession session) {
        if (session == null) {
            return List.of();
        }

        List<ReportResDTO.QuestionnaireGroup> groups = new ArrayList<>();

        List<ReportResDTO.QuestionnaireRow> bodyRows = new ArrayList<>();
        addRow(bodyRows, LABEL_AGE, formatAge(session.getAge()));
        addRow(bodyRows, LABEL_HEIGHT, formatHeight(session.getHeight()));
        addRow(bodyRows, LABEL_WEIGHT, formatWeight(session.getWeight()));
        if (!bodyRows.isEmpty()) {
            groups.add(ReportResDTO.QuestionnaireGroup.builder()
                    .title(GROUP_BODY)
                    .rows(bodyRows)
                    .build());
        }

        List<ReportResDTO.QuestionnaireRow> lifestyleRows = new ArrayList<>();
        addRow(lifestyleRows, LABEL_SMOKE, formatSmokeShort(session));
        addRow(lifestyleRows, LABEL_DRINK, formatDrinkShort(session.getDrinkStatus()));
        addRow(lifestyleRows, LABEL_SLEEP, formatSleepShort(session.getSleepHours(), session.getSleepMinutes()));
        if (!lifestyleRows.isEmpty()) {
            groups.add(ReportResDTO.QuestionnaireGroup.builder()
                    .title(GROUP_LIFESTYLE)
                    .rows(lifestyleRows)
                    .build());
        }

        return List.copyOf(groups);
    }

    private static void addRow(List<ReportResDTO.QuestionnaireRow> rows, String label, String value) {
        if (value != null && !value.isBlank()) {
            rows.add(ReportResDTO.QuestionnaireRow.builder()
                    .label(label)
                    .value(value)
                    .build());
        }
    }

    private static String formatAge(Integer age) {
        return age == null ? null : age + "세";
    }

    private static String formatHeight(Double heightCm) {
        if (heightCm == null) {
            return null;
        }
        if (heightCm == Math.rint(heightCm)) {
            return ((int) Math.rint(heightCm)) + "cm";
        }
        return String.format("%.1fcm", heightCm);
    }

    private static String formatWeight(Double weightKg) {
        if (weightKg == null) {
            return null;
        }
        if (weightKg == Math.rint(weightKg)) {
            return ((int) Math.rint(weightKg)) + "kg";
        }
        return String.format("%.1fkg", weightKg);
    }

    private static String formatSmokeShort(TestSession session) {
        String label;
        if (session.getGender() == Gender.F) {
            label = formatFemaleSmokeShort(session.getSmokeLevel());
        } else {
            label = AiLifestyleCategoryMapper.toSmokeDisplayLabel(session.getSmokeStatus());
        }
        return shortenSmokeLabel(label);
    }

    private static String shortenSmokeLabel(String label) {
        if (label == null || label.isBlank()) {
            return null;
        }
        return label
                .replace("안 피움", "비흡연")
                .replace("가끔 피움", "가끔")
                .replace("매일 피움", "매일");
    }

    private static String formatFemaleSmokeShort(Integer smokeLevel) {
        if (smokeLevel == null) {
            return null;
        }
        return switch (AiLifestyleCategoryMapper.mapFemaleSmokeLevelToAi(smokeLevel)) {
            case 0 -> "비흡연";
            case 1 -> "가끔";
            case 2 -> "매일";
            default -> null;
        };
    }

    private static String formatDrinkShort(String drinkStatus) {
        return AiLifestyleCategoryMapper.toDrinkDisplayLabel(drinkStatus);
    }

    private static String formatSleepShort(Integer hours, Integer minutes) {
        if (hours == null && minutes == null) {
            return null;
        }
        if (hours == null) {
            return null;
        }
        int m = minutes != null ? minutes : 0;
        if (m == 0) {
            return hours + "시간";
        }
        return SleepInputSupport.formatKorean(hours, m);
    }
}
