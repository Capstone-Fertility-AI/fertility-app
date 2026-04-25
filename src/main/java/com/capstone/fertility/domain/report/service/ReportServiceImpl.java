package com.capstone.fertility.domain.report.service;

import com.capstone.fertility.domain.report.dto.res.ReportResDTO;
import com.capstone.fertility.domain.report.exception.ReportException;
import com.capstone.fertility.domain.report.exception.code.ReportErrorCode;
import com.capstone.fertility.domain.result.entity.TestResult;
import com.capstone.fertility.domain.result.repository.TestResultRepository;
import com.capstone.fertility.domain.test.entity.TestSession;
import com.capstone.fertility.domain.user.entity.User;
import com.capstone.fertility.domain.user.enums.Gender;
import com.capstone.fertility.global.llm.client.LlmClient;
import com.capstone.fertility.global.llm.prompt.ReportSystemPrompt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final TestResultRepository testResultRepository;
    private final LlmClient llmClient;
    private final ObjectMapper objectMapper;

    @Override
    public ReportResDTO.DetailReport generateReport(Long userId, Long resultId) {
        TestResult result = testResultRepository.findById(resultId)
                .orElseThrow(() -> new ReportException(ReportErrorCode.RESULT_NOT_FOUND));

        if (!result.getUser().getId().equals(userId)) {
            throw new ReportException(ReportErrorCode.RESULT_NOT_OWNER);
        }

        User user = result.getUser();
        TestSession session = result.getTestSession();

        String userPrompt = buildUserPrompt(user, session, result);

        String reportMarkdown;
        try {
            reportMarkdown = llmClient.chat(ReportSystemPrompt.SYSTEM_INSTRUCTION, userPrompt);
        } catch (Exception e) {
            log.error("LLM API 호출 실패: resultId={}", resultId, e);
            throw new ReportException(ReportErrorCode.LLM_API_FAILED);
        }

        String genderLabel = session.getGender() == Gender.M ? "남성" : "여성";

        return ReportResDTO.DetailReport.builder()
                .resultId(result.getId())
                .nickname(user.getNickname())
                .age(session.getAge())
                .gender(genderLabel)
                .score(result.getAiScore())
                .riskLevel(result.getRiskLevel() != null ? result.getRiskLevel().name() : null)
                .report(reportMarkdown)
                .build();
    }

    private String buildUserPrompt(User user, TestSession session, TestResult result) {
        // AI(SHAP)가 산출한 위험 요인을 모두 그대로 LLM에 전달한다.
        // 빈/공백 문자열은 사전에 걸러내고, factorCount를 함께 전달해 프롬프트가 동적으로 반복 렌더링하도록 한다.
        List<String> factors = result.getTopFactors() != null
                ? result.getTopFactors().stream()
                        .filter(f -> f != null && !f.isBlank())
                        .map(String::trim)
                        .toList()
                : Collections.emptyList();

        String sleepDescription = describeSleep(session.getSleepHours());
        String stressDescription = describeStress(session.getStressLevel(), session.getStressScore());

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("nickname", user.getNickname());
        payload.put("age", session.getAge());
        payload.put("gender", session.getGender() == Gender.M ? "남성" : "여성");
        payload.put("score", result.getAiScore());
        payload.put("riskLevel", result.getRiskLevel() != null ? result.getRiskLevel().name() : "DANGER");
        payload.put("sleep", sleepDescription);
        payload.put("stress", stressDescription);
        payload.put("factors", factors);
        payload.put("factorCount", factors.size());

        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("LLM 페이로드 JSON 변환 실패", e);
        }
    }

    private String describeSleep(Integer sleepHours) {
        if (sleepHours == null) return "정보 없음";
        if (sleepHours >= 7) return "충분한 수면 (" + sleepHours + "시간)";
        if (sleepHours >= 5) return "다소 부족한 수면 (" + sleepHours + "시간)";
        return "수면 부족 (" + sleepHours + "시간)";
    }

    private String describeStress(String stressLevel, Integer stressScore) {
        if (stressLevel == null && stressScore == null) return "정보 없음";

        String levelLabel;
        if (stressLevel != null) {
            levelLabel = switch (stressLevel) {
                case "LOW" -> "낮음";
                case "NORMAL" -> "보통";
                case "HIGH" -> "높음";
                default -> stressLevel;
            };
        } else {
            levelLabel = "측정됨";
        }

        if (stressScore != null) {
            return levelLabel + " (PSS " + stressScore + "점/40점)";
        }
        return levelLabel;
    }
}
