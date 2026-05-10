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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

        String llmJson;
        try {
            llmJson = llmClient.chatJson(ReportSystemPrompt.SYSTEM_INSTRUCTION, userPrompt);
        } catch (Exception e) {
            log.error("LLM API 호출 실패: resultId={}", resultId, e);
            throw new ReportException(ReportErrorCode.LLM_API_FAILED);
        }

        ParsedReport parsed = parseLlmJson(llmJson);

        String genderLabel = session.getGender() == Gender.M ? "남성" : "여성";

        return ReportResDTO.DetailReport.builder()
                .resultId(result.getId())
                .nickname(user.getNickname())
                .age(session.getAge())
                .gender(genderLabel)
                .score(result.getAiScore())
                .riskLevel(result.getRiskLevel() != null ? result.getRiskLevel().name() : null)
                .intro(parsed.intro)
                .condition(parsed.condition)
                .factorAnalyses(parsed.factorAnalyses)
                .missions(parsed.missions)
                .closing(parsed.closing)
                .build();
    }

    private String buildUserPrompt(User user, TestSession session, TestResult result) {
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

    private ParsedReport parseLlmJson(String llmJson) {
        try {
            JsonNode root = objectMapper.readTree(llmJson);
            return new ParsedReport(
                    parseIntro(root.path("intro")),
                    parseCondition(root.path("condition")),
                    parseFactorAnalyses(root.path("factorAnalyses")),
                    parseMissions(root.path("missions")),
                    asText(root.path("closing"))
            );
        } catch (Exception e) {
            log.error("LLM JSON 파싱 실패. raw={}", llmJson, e);
            throw new ReportException(ReportErrorCode.LLM_API_FAILED);
        }
    }

    private ReportResDTO.Intro parseIntro(JsonNode node) {
        if (node == null || node.isMissingNode() || node.isNull()) return null;
        return ReportResDTO.Intro.builder()
                .greeting(asText(node.path("greeting")))
                .scoreMessage(asText(node.path("scoreMessage")))
                .comfortMessage(asText(node.path("comfortMessage")))
                .build();
    }

    private ReportResDTO.Condition parseCondition(JsonNode node) {
        if (node == null || node.isMissingNode() || node.isNull()) return null;
        return ReportResDTO.Condition.builder()
                .sleepLabel(asText(node.path("sleepLabel")))
                .stressLabel(asText(node.path("stressLabel")))
                .summary(asText(node.path("summary")))
                .build();
    }

    private List<ReportResDTO.FactorAnalysis> parseFactorAnalyses(JsonNode node) {
        List<ReportResDTO.FactorAnalysis> out = new ArrayList<>();
        if (node == null || !node.isArray()) return out;
        for (JsonNode item : node) {
            out.add(ReportResDTO.FactorAnalysis.builder()
                    .factor(asText(item.path("factor")))
                    .category(asText(item.path("category")))
                    .mateThought(asText(item.path("mateThought")))
                    .expectedChange(asText(item.path("expectedChange")))
                    .build());
        }
        return out;
    }

    private List<ReportResDTO.Mission> parseMissions(JsonNode node) {
        List<ReportResDTO.Mission> out = new ArrayList<>();
        if (node == null || !node.isArray()) return out;
        for (JsonNode item : node) {
            out.add(ReportResDTO.Mission.builder()
                    .title(asText(item.path("title")))
                    .description(asText(item.path("description")))
                    .linkedFactor(asText(item.path("linkedFactor")))
                    .category(asText(item.path("category")))
                    .frequency(parseFrequency(item.path("frequency")))
                    .duration(parseDuration(item.path("duration")))
                    .difficulty(asText(item.path("difficulty")))
                    .userAdjustable(item.path("userAdjustable").asBoolean(true))
                    .build());
        }
        return out;
    }

    private ReportResDTO.Frequency parseFrequency(JsonNode node) {
        if (node == null || node.isMissingNode() || node.isNull()) return null;
        return ReportResDTO.Frequency.builder()
                .type(asText(node.path("type")))
                .count(node.path("count").isNumber() ? node.path("count").asInt() : null)
                .unit(asText(node.path("unit")))
                .build();
    }

    private ReportResDTO.Duration parseDuration(JsonNode node) {
        if (node == null || node.isMissingNode() || node.isNull()) return null;
        return ReportResDTO.Duration.builder()
                .value(node.path("value").isNumber() ? node.path("value").asInt() : null)
                .unit(asText(node.path("unit")))
                .build();
    }

    private String asText(JsonNode node) {
        if (node == null || node.isMissingNode() || node.isNull()) return null;
        String text = node.asText();
        return (text == null || text.isBlank()) ? null : text;
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

    private record ParsedReport(
            ReportResDTO.Intro intro,
            ReportResDTO.Condition condition,
            List<ReportResDTO.FactorAnalysis> factorAnalyses,
            List<ReportResDTO.Mission> missions,
            String closing
    ) {}
}
