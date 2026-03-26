# -*- coding: utf-8 -*-
"""
FastAPI AI 예측 응답에서 top_factors(또는 top1~3_factor)를 항상 3개로 맞추기 위한 패딩 로직 예시.
fertility-ai-server 프로젝트의 추론 서비스(예: inference_service.py)에서 SHAP 결과 직후에 호출하면 됩니다.

작동 원리:
1. SHAP/특성중요도로 나온 위험 요인 문자열 리스트에서 빈 값을 제거합니다.
2. 길이가 3 이상이면 앞 3개만 잘라 반환합니다.
3. 3 미만이면, 건강에 유리한 '양호' 문구 풀에서 아직 리스트에 없는 항목을 순서대로 붙여 길이 3을 만듭니다.
   (UI에서 null 대신 항상 짧은 한글 라벨이 보이도록 함)
"""

from __future__ import annotations

# 부족 분만큼 순서대로 사용할 긍정 요인 라벨 (실제 서비스 톤에 맞게 조정 가능)
DEFAULT_POSITIVE_PADDING: list[str] = [
    "양호한 생활 습관 유지 중",
    "적절한 수면 패턴 유지 중",
    "비흡연·절주 등 건강 행동 유지 중",
    "규칙적인 컨디션 관리 유지 중",
    "스트레스 관리 양호 유지 중",
]


def ensure_top_factors_length_three(
    risk_factors: list[str] | None,
    positive_candidates: list[str] | None = None,
) -> tuple[str, str, str]:
    """
    위험 요인 리스트를 받아 (top1, top2, top3) 튜플로 반환합니다. None은 반환하지 않습니다.

    :param risk_factors: 모델이 뽑은 위험 요인 이름들 (0~N개)
    :param positive_candidates: 패딩에 쓸 '양호한 ~ 유지 중' 문구 풀 (None이면 DEFAULT_POSITIVE_PADDING 사용)
    :return: 항상 길이 3의 문자열 튜플
    """
    pool = positive_candidates if positive_candidates is not None else DEFAULT_POSITIVE_PADDING
    # 앞쪽 공백 제거, 빈 문자열 제거
    cleaned: list[str] = []
    for x in risk_factors or []:
        if not x:
            continue
        t = str(x).strip()
        if t:
            cleaned.append(t)

    if len(cleaned) >= 3:
        return cleaned[0], cleaned[1], cleaned[2]

    out = list(cleaned)
    used_lower = {s.casefold() for s in out}
    i = 0
    while len(out) < 3 and i < len(pool):
        candidate = pool[i]
        i += 1
        if candidate.casefold() in used_lower:
            continue
        out.append(candidate)
        used_lower.add(candidate.casefold())

    # 이론상 풀이 부족하면 일반 문구로 채움
    while len(out) < 3:
        out.append("건강 관리 양호 유지 중")

    return out[0], out[1], out[2]


# 사용 예 (FastAPI 라우트 또는 서비스 내부):
#
#   raw = shap_top_feature_names(...)  # 예: ["비만", "흡연 노출"]
#   t1, t2, t3 = ensure_top_factors_length_three(raw)
#   result_payload["top_factors"] = [t1, t2, t3]
