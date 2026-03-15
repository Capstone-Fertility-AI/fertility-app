package com.capstone.fertility.domain.result.converter;

import com.capstone.fertility.domain.result.dto.res.ResultResDTO;
import com.capstone.fertility.domain.result.entity.TestResult;
import com.capstone.fertility.domain.test.entity.TestSession;

public class ResultConverter {

    public static ResultResDTO.ResultDetailResDTO toResultDetailResDTO(TestResult result) {
        TestSession session = result.getTestSession();
        return ResultResDTO.ResultDetailResDTO.builder()
                .resultId(result.getId())
                .status(session.getStatus())
                .score(result.getScore())
                .llmAdvice(result.getLlmAdvice())
                .medicalEvidence(result.getMedicalEvidence())
                .currentStep(session.getCurrentStep())
                .age(session.getAge())
                .height(session.getHeight())
                .weight(session.getWeight())
                .menarcheAge(session.getMenarcheAge())
                .parity(session.getParity())
                .pcos(session.getPcos())
                .endo(session.getEndo())
                .uf(session.getUf())
                .pid(session.getPid())
                .chlam(session.getChlam())
                .gon(session.getGon())
                .smokeLevel(session.getSmokeLevel())
                .binge12(session.getBinge12())
                .sleepHours(session.getSleepHours())
                .build();
    }
}
