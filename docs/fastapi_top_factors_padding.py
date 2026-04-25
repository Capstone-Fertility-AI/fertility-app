# -*- coding: utf-8 -*-
"""
[DEPRECATED — 2026-04 이후]
이 모듈은 SHAP 결과를 항상 3개로 패딩(top1~3_factor 또는 top_factors 길이 3 강제)하기 위해 만들어졌습니다.

서비스 정책 변경:
  - 더 이상 위험 요인을 Top 3로 제한하지 않습니다.
  - AI(SHAP)가 산출한 위험 요인을 "전부, 입력된 순서 그대로" 응답으로 내려줍니다.
  - Spring 백엔드는 단일 컬럼(top_factors, JSON 배열)에 가변 길이 List<String>으로 보관하며,
    LLM 프롬프트는 factors 배열 전체를 동적으로 반복 렌더링합니다.

따라서 본 패딩 함수(`ensure_top_factors_length_three`)는 사용을 중단합니다.
대신 빈 값/공백 정리만 수행하는 가벼운 정규화 함수(`sanitize_risk_factors`)만 노출합니다.
"""

from __future__ import annotations


def sanitize_risk_factors(risk_factors: list[str] | None) -> list[str]:
    """
    SHAP 등에서 받은 위험 요인 문자열 리스트를 정리하여 반환합니다.

    - None / 빈 문자열 / 공백-only 항목 제거
    - 양 끝 공백 트리밍
    - 입력 순서(=중요도 순서) 보존
    - 길이 제한 없음 (서비스 정책: 전부 노출)

    :param risk_factors: 모델이 뽑은 위험 요인 이름들 (0~N개, 순서 == 중요도)
    :return: 깨끗하게 정리된 위험 요인 리스트 (길이 0~N 가능)
    """
    if not risk_factors:
        return []

    cleaned: list[str] = []
    for x in risk_factors:
        if x is None:
            continue
        t = str(x).strip()
        if t:
            cleaned.append(t)
    return cleaned


# -----------------------------------------------------------------------------
# 사용 예 (FastAPI 라우트 또는 inference_service 내부):
#
#   raw = shap_top_feature_names(...)            # 예: ["비만", "흡연 노출", "수면 부족"]
#   factors = sanitize_risk_factors(raw)
#   result_payload["top_factors"] = factors      # 길이 N 그대로 응답
# -----------------------------------------------------------------------------
