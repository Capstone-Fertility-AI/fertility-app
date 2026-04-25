# fertility-app

브랜치 전략 (Branch Strategy)

우리 팀은 간소화된 Git Flow 전략을 사용합니다.
절대 main 브랜치에 직접 Push 하지 마세요!

main: 최종 발표용, 실제 배포되는 완벽하게 작동하는 코드만 들어갑니다.

develop: 개발용 중심 브랜치입니다. 기능이 완성될 때마다 이곳으로 합칩니다.

feature/...: 각자 기능을 개발할 때 파생하는 브랜치입니다.

네이밍 규칙: feature/기능이름

예시: feature/kakao-login, feature/ai-model, feature/ui-home

작업 흐름 (Workflow)

develop 브랜치에서 최신 코드를 받습니다. (git pull origin develop)

내 작업용 브랜치를 만듭니다. (git checkout -b feature/login)

열심히 코딩하고 커밋합니다.

GitHub에 내 브랜치를 올립니다. (git push origin feature/login)

GitHub 웹사이트에서 develop 브랜치로 **Pull Request(PR)**를 생성합니다.

## 📝 커밋 메시지 규칙 (Commit Convention)

커밋 메시지는 "나"를 위한 것이 아니라 "팀원"을 위한 것입니다. 무엇을 작업했는지 한눈에 알 수 있도록 **[태그]**를 반드시 붙여주세요.

태그 종류

feat : 새로운 기능 추가 (예: feat: 카카오 로그인 연동)

fix : 버그 수정 (예: fix: AI 결과 점수 NaN 출력 오류 해결)

design : CSS 등 사용자 UI 디자인 변경 (예: design: 홈 화면 라벤더 테마 적용)

refactor : 기능 변화 없이 코드 구조 개선 (예: refactor: 전처리 함수 모듈화)

docs : README 등 문서 수정 (예: docs: API 명세서 업데이트)

chore : 패키지 설치, 빌드 설정 등 (예: chore: Tailwind CSS 설치)

커밋 메시지 작성 예시

feat: AI 건강 점수 분석 API 연동 완료

- FastAPI 서버와 통신하여 XGBoost 결과값 수신 기능 추가
- 위험도 Top 3 그래프 컴포넌트 추가


## 🤝 Pull Request (PR) 및 코드 리뷰 규칙

다른 팀원의 코드를 합치기(Merge) 전에 확인하는 과정입니다.

PR 목적지 확인: 항상 feature/... 브랜치에서 develop 브랜치로 PR을 엽니다!

PR 제목: 커밋 메시지와 동일한 규칙 적용 (예: feat: 메인 홈 화면 UI 구현)

PR 내용:

무엇을 개발했나요?

스크린샷 (UI 변경이 있다면 캡처 첨부 필수!)

리뷰어가 특별히 봐줬으면 하는 부분 (예: "경험치 계산 로직이 맞는지 확인해 주세요.")

리뷰(Review) 필수: - 혼자서 PR 열고 바로 Merge 하는 것은 금지! ❌

최소 1명 이상의 팀원이 코드를 보고 Approve(승인)를 눌러야 Merge 할 수 있도록 GitHub 설정(Branch Protection)을 걸어둡니다.

## 🎯 이슈 (Issue) 관리

"내가 오늘 할 일", "발생한 버그"는 카카오톡이 아니라 GitHub Issues에 등록합니다.

기능 개발을 시작하기 전 Issue를 먼저 만듭니다. (예: Issue #1: 설문조사 화면 만들기)

브랜치 이름이나 커밋 메시지에 이슈 번호를 적으면 추적이 쉽습니다. (예: feat: 설문조사 UI 구현 (#1))

GitHub Projects(칸반 보드)를 활용해 To Do, In Progress, Done으로 일감을 옮겨가며 프로젝트 진행 상황을 눈으로 확인합니다.

## AI 서버 연동 운영 가이드

Spring Boot는 별도 FastAPI AI 서버를 호출합니다.

- 남성 예측: `POST {AI_BASE_URL}/api/predict/male`
- 여성 예측: `POST {AI_BASE_URL}/api/predict/female`

### 필수 환경변수

- `AI_BASE_URL` (예: `http://127.0.0.1:8000`)
- `AI_CONNECT_TIMEOUT_MS` (기본값: `2000`)
- `AI_READ_TIMEOUT_MS` (기본값: `5000`)
- `AI_PREDICTION_ENABLED` (`true/false`, 기본값: `true`)
- `AI_HEALTH_PATH` (기본값: `/health`)

### 설정 키

- `ai.base-url`
- `ai.connect-timeout-ms`
- `ai.read-timeout-ms`
- `ai.health-path`
- `ai.prediction.enabled`
- `ai.prediction.path.male`
- `ai.prediction.path.female`

### 장애 시 점검 포인트

1. FastAPI 프로세스 기동 여부 (`uvicorn` 로그 확인)
2. 네트워크 경로/방화벽/보안그룹에서 `AI_BASE_URL` 접근 가능 여부
3. 타임아웃 설정 (`AI_CONNECT_TIMEOUT_MS`, `AI_READ_TIMEOUT_MS`)이 과도하게 낮지 않은지
4. 엔드포인트 경로 불일치 여부 (`/api/predict/male`, `/api/predict/female`)
5. Actuator health 확인: `/actuator/health` 내 `aiServer` 상태

## 위험 요인(top_factors) 정책 변경 안내 (2026-04)

기존 `top1_factor / top2_factor / top3_factor` 3개 컬럼 구조에서, **AI(SHAP)가 산출한 활성 위험요인 전체를 단일 컬럼(`top_factors`)에 JSON 배열로 보관**하는 방식으로 전환되었습니다.

- Entity: `TestResult.topFactors : List<String>` (JPA `StringListJsonConverter`로 TEXT 컬럼에 JSON 직렬화)
- 응답 DTO: `SubmitResult.topFactors`, `ResultHistoryItemDTO.topFactors` (가변 길이 배열, 0~N개)
- LLM 프롬프트: `factors` 배열을 동적으로 1~N번까지 반복 렌더링
- 빈 배열(`[]`) = "위험 요인 없음" 상태로 간주 (프론트에서 빈 상태 문구 출력)

### 새 AI 응답 스펙 (2026-04 이후)

```json
{
  "status": "success",
  "result": {
    "gender": "female",
    "score": 75,
    "risk_probability": 25.0,
    "bmi": 24.5,
    "top_factors": ["흡연", "수면 부족", "BMI 과다"]
  }
}
```

- `top_factors`는 **활성 위험요인 전체 목록**(중요도 순). Top 3 고정이 아니며 길이 제한 없음.
- 정상/긍정 요인은 AI 서버에서 사전 필터링되어 포함되지 않음.
- 다음 필드는 더 이상 사용하지 않음 (응답에 포함되어 들어와도 Spring은 `@JsonIgnoreProperties(ignoreUnknown = true)`로 무시):
  - `top1_factor`, `top2_factor`, `top3_factor`
  - `mission_candidates`
- 정상/주의/위험 등급(`riskLevel`) 매핑은 Spring(`RiskLevel.determineLevel`)에서 score 기반으로 수행. AI 서버는 score / risk_probability / top_factors 계산만 담당.

### prod DB 마이그레이션 SQL (필수)

`spring.jpa.hibernate.ddl-auto: validate` 운영 환경에서는 컬럼이 자동 추가되지 않으므로 아래 SQL을 수동 실행해야 부팅됩니다.

```sql
-- 1) 신규 컬럼 추가 (TEXT, JSON 배열 보관)
ALTER TABLE test_results ADD COLUMN IF NOT EXISTS top_factors TEXT;

-- 2) (선택) 기존 데이터 보존이 필요하면 한 번에 마이그레이션
UPDATE test_results
   SET top_factors = to_json(
        ARRAY(
          SELECT v FROM (VALUES (top1_factor), (top2_factor), (top3_factor)) AS t(v)
          WHERE v IS NOT NULL AND v <> ''
        )
       )::text
 WHERE top_factors IS NULL;

-- 3) (선택, 충분히 검증 후) 레거시 컬럼 제거
-- ALTER TABLE test_results DROP COLUMN top1_factor;
-- ALTER TABLE test_results DROP COLUMN top2_factor;
-- ALTER TABLE test_results DROP COLUMN top3_factor;
```

local 환경(`ddl-auto: update`)은 부팅 시 `top_factors` 컬럼이 자동 추가됩니다. 레거시 컬럼은 자동 삭제되지 않으므로 수동 정리하거나 그대로 두어도 됩니다.

### AI 서버(FastAPI) 변경 사항

- 응답 `result.top_factors`는 **고정 길이 3이 아니라 가변 길이 배열**로 내려주세요(중요도 순서 유지).
- 더 이상 빈 자리를 "양호한 ~ 유지 중" 같은 문구로 패딩하지 않습니다.
- 참고: `docs/fastapi_top_factors_padding.py` 의 `sanitize_risk_factors()` (None/공백 정리만 수행).
