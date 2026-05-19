package com.capstone.fertility.domain.report.support;

import com.capstone.fertility.domain.report.dto.res.ReportResDTO;
import com.capstone.fertility.domain.test.entity.TestSession;
import com.capstone.fertility.domain.user.enums.Gender;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ReportQuestionnaireSupportTest {

    @Test
    void buildFrom_maleSession_mapsKoreanLabels() {
        TestSession session = TestSession.builder()
                .gender(Gender.M)
                .height(175.0)
                .weight(72.5)
                .smokeStatus("가끔 피움")
                .drinkStatus("월 1~3회")
                .build();

        Map<String, String> rows = rowMap(ReportQuestionnaireSupport.buildFrom(session));

        assertThat(rows).containsEntry(ReportQuestionnaireSupport.LABEL_HEIGHT, "175 cm");
        assertThat(rows).containsEntry(ReportQuestionnaireSupport.LABEL_WEIGHT, "72.5 kg");
        assertThat(rows).containsEntry(ReportQuestionnaireSupport.LABEL_SMOKE, "가끔 피움");
        assertThat(rows).containsEntry(ReportQuestionnaireSupport.LABEL_DRINK, "월 1~3회");
    }

    @Test
    void buildFrom_femaleSession_mapsSmokeLevelToLabel() {
        TestSession session = TestSession.builder()
                .gender(Gender.F)
                .height(162.0)
                .weight(55.0)
                .smokeLevel(2)
                .build();

        Map<String, String> rows = rowMap(ReportQuestionnaireSupport.buildFrom(session));

        assertThat(rows).containsEntry(ReportQuestionnaireSupport.LABEL_HEIGHT, "162 cm");
        assertThat(rows).containsEntry(ReportQuestionnaireSupport.LABEL_SMOKE, "매일 피움");
        assertThat(rows).doesNotContainKey(ReportQuestionnaireSupport.LABEL_DRINK);
    }

    private static Map<String, String> rowMap(List<ReportResDTO.QuestionnaireGroup> groups) {
        return groups.stream()
                .flatMap(g -> g.rows().stream())
                .collect(Collectors.toMap(ReportResDTO.QuestionnaireRow::label, ReportResDTO.QuestionnaireRow::value));
    }
}
