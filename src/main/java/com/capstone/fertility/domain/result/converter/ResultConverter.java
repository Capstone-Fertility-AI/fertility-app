package com.capstone.fertility.domain.result.converter;

import com.capstone.fertility.domain.result.dto.res.ResultResDTO;
import com.capstone.fertility.domain.test.entity.TestSession;

public class ResultConverter {

    public static ResultResDTO.ResultDetailResDTO toResultDetailResDTO(TestSession session) {
        return ResultResDTO.ResultDetailResDTO.builder()
                .resultId(session.getId())
                .status(session.getStatus())
                .score(session.getScore())
                .llmAdvice(session.getLlmAdvice())
                .medicalEvidence(session.getMedicalEvidence())
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
