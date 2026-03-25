package com.capstone.fertility.global.ai.mapping;

/**
 * 프론트엔드 한국어 범주 문자열(또는 여성용 정수 단계)을
 * Python AI 서버가 기대하는 0~2 정수 스케일로 변환합니다.
 * <p>
 * 스케일 의미: 0 = 가장 양호(비노출/없음), 1 = 중간, 2 = 가장 높은 노출/빈도
 */
public final class AiLifestyleCategoryMapper {

    private AiLifestyleCategoryMapper() {
    }

    /**
     * 흡연 (smokeStatus → SMOKE30 / smoke_amount 등에 대응)
     * "안 피움"→0, "가끔 피움"→1, "매일 피움"→2
     */
    public static int mapSmokeStatusToAi(String smokeStatus) {
        if (smokeStatus == null || smokeStatus.isBlank()) {
            return 0;
        }
        String v = smokeStatus.trim();
        return switch (v) {
            case "안 피움" -> 0;
            case "가끔 피움" -> 1;
            case "매일 피움" -> 2;
            default -> 0;
        };
    }   

    /**
     * 음주 (drinkStatus → DRINK12 / drink_freq 등에 대응)
     * "안 마심"→0, "월 1~3회"→1, "주 1회 이상"→2
     */
    public static int mapDrinkStatusToAi(String drinkStatus) {
        if (drinkStatus == null || drinkStatus.isBlank()) {
            return 0;
        }
        String v = drinkStatus.trim();
        return switch (v) {
            case "안 마심" -> 0;
            case "월 1~3회" -> 1;
            case "주 1회 이상" -> 2;
            default -> 0;
        };
    }

    /**
     * 폭음 (bingeStatus → BINGE12 / binge_freq 등에 대응)
     * "없음"→0, "월 1회"→1, "주 1회 이상"→2
     */
    public static int mapBingeStatusToAi(String bingeStatus) {
        if (bingeStatus == null || bingeStatus.isBlank()) {
            return 0;
        }
        String v = bingeStatus.trim();
        return switch (v) {
            case "없음" -> 0;
            case "월 1회" -> 1;
            case "주 1회 이상" -> 2;
            default -> 0;
        };
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
