package com.capstone.fertility.global.ai.mapping;

/**
 * 프론트 설문 API 코드(NEVER, MONTHLY_1_TO_3 등) 및 한국어 라벨을
 * AI 0~2 스케일·리포트 표시용 한글로 변환합니다.
 * <p>
 * 프론트 전송 규약: {@code toApiSmokeStatus}, {@code toApiDrinkStatus}, {@code toApiBingeStatus}
 * <ul>
 *   <li>흡연: NEVER | OCCASIONAL | DAILY</li>
 *   <li>음주: NEVER | MONTHLY_1_TO_3 | WEEKLY_OR_MORE</li>
 *   <li>폭음: NEVER | MONTHLY_1 | WEEKLY_OR_MORE</li>
 * </ul>
 */
public final class AiLifestyleCategoryMapper {

    private AiLifestyleCategoryMapper() {
    }

    /** 흡연 표시용 한글 라벨 */
    public static String toSmokeDisplayLabel(String smokeStatus) {
        if (smokeStatus == null || smokeStatus.isBlank()) {
            return null;
        }
        return switch (normalizeToken(smokeStatus)) {
            case "NEVER", "NONE" -> "안 피움";
            case "OCCASIONAL", "OCCASIONALLY", "SOMETIMES" -> "가끔 피움";
            case "DAILY" -> "매일 피움";
            case "안 피움" -> "안 피움";
            case "가끔 피움" -> "가끔 피움";
            case "매일 피움" -> "매일 피움";
            default -> smokeStatus.trim();
        };
    }

    public static int mapSmokeStatusToAi(String smokeStatus) {
        if (smokeStatus == null || smokeStatus.isBlank()) {
            return 0;
        }
        return switch (normalizeToken(smokeStatus)) {
            case "NEVER", "NONE", "안 피움" -> 0;
            case "OCCASIONAL", "OCCASIONALLY", "SOMETIMES", "가끔 피움" -> 1;
            case "DAILY", "매일 피움" -> 2;
            default -> 0;
        };
    }

    /** 음주 표시용 한글 라벨 */
    public static String toDrinkDisplayLabel(String drinkStatus) {
        if (drinkStatus == null || drinkStatus.isBlank()) {
            return null;
        }
        return switch (normalizeToken(drinkStatus)) {
            case "NEVER", "NONE" -> "안 마심";
            case "MONTHLY_1_TO_3", "SOMETIMES" -> "월 1~3회";
            case "WEEKLY_OR_MORE", "WEEKLY", "WEEKLY_1_OR_MORE" -> "주 1회 이상";
            case "안 마심" -> "안 마심";
            case "월 1~3회" -> "월 1~3회";
            case "주 1회 이상" -> "주 1회 이상";
            default -> drinkStatus.trim();
        };
    }

    public static int mapDrinkStatusToAi(String drinkStatus) {
        if (drinkStatus == null || drinkStatus.isBlank()) {
            return 0;
        }
        return switch (normalizeToken(drinkStatus)) {
            case "NEVER", "NONE", "안 마심" -> 0;
            case "MONTHLY_1_TO_3", "SOMETIMES", "월 1~3회" -> 1;
            case "WEEKLY_OR_MORE", "WEEKLY", "WEEKLY_1_OR_MORE", "주 1회 이상" -> 2;
            default -> 0;
        };
    }

    /** 폭음 표시용 한글 라벨 */
    public static String toBingeDisplayLabel(String bingeStatus) {
        if (bingeStatus == null || bingeStatus.isBlank()) {
            return null;
        }
        return switch (normalizeToken(bingeStatus)) {
            case "NEVER", "NONE" -> "없음";
            case "MONTHLY_1", "MONTHLY" -> "월 1회";
            case "WEEKLY_OR_MORE", "WEEKLY", "WEEKLY_1_OR_MORE" -> "주 1회 이상";
            case "없음" -> "없음";
            case "월 1회" -> "월 1회";
            case "주 1회 이상" -> "주 1회 이상";
            default -> bingeStatus.trim();
        };
    }

    public static int mapBingeStatusToAi(String bingeStatus) {
        if (bingeStatus == null || bingeStatus.isBlank()) {
            return 0;
        }
        return switch (normalizeToken(bingeStatus)) {
            case "NEVER", "NONE", "없음" -> 0;
            case "MONTHLY_1", "MONTHLY", "월 1회" -> 1;
            case "WEEKLY_OR_MORE", "WEEKLY", "WEEKLY_1_OR_MORE", "주 1회 이상" -> 2;
            default -> 0;
        };
    }

    private static String normalizeToken(String raw) {
        return raw.trim().toUpperCase();
    }

    /**
     * 여성 세션: smoke_level 정수 (프론트가 0,1,2 또는 구버전 1,3,5로 줄 수 있음)
     */
    public static int mapFemaleSmokeLevelToAi(Integer smokeLevel) {
        if (smokeLevel == null) {
            return 0;
        }
        int x = smokeLevel;
        if (x >= 0 && x <= 2) {
            return x;
        }
        return switch (x) {
            case 1 -> 0;
            case 3 -> 1;
            case 5 -> 2;
            default -> 0;
        };
    }

    /**
     * 여성 세션: binge12 정수 (동일 스케일 가정)
     */
    public static int mapFemaleBingeLevelToAi(Integer binge12) {
        return mapFemaleSmokeLevelToAi(binge12);
    }
}
