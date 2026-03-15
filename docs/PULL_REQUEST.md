# PR: 건강 진단 검사 임시 저장/복구 및 결과 상세 API

## 개요
사용자가 9단계 건강 진단 질문을 진행하는 동안 **데이터 유실 방지**를 위한 임시 저장·복구 API와, 완료 후 **검사 결과 상세 리포트** API를 추가했습니다.

---

## 추가·수정 사항 요약

### 1. Entity

#### `TestSession` 수정
- **추가 필드**
  - `currentStep` (Integer): 마지막 진행 단계(1~9) 기록
  - `sleepHours` (Integer): 9번 질문(수면 시간) 데이터
  - `score` (Integer): 검사 점수 (COMPLETED 후 채움)
  - `llmAdvice` (String, TEXT): LLM 조언
  - `medicalEvidence` (String, TEXT): 의학적 근거
- 기존 입력 필드 및 `status`에 setter 추가 → Dirty Checking 활용

#### `TestResult` 정리
- 현재 미사용으로 `@Deprecated` 처리 및 주석 추가  
- 결과는 완료된 `TestSession`으로 표현, 상세는 `GET /results/{resultId}`로 제공

---

### 2. API

| Method | Path | 설명 |
|--------|------|------|
| **POST** | `/tests` | 검사 세션 생성 (기존) |
| **POST** | `/tests/{sessionId}/step` | **단계별 임시 저장** – step·data로 currentStep 및 해당 단계 필드 업데이트 |
| **GET** | `/tests/current` | **진행 중인 세션 복구** – IN_PROGRESS인 가장 최근 세션 반환 (없으면 404) |
| **POST** | `/tests/{sessionId}/submit` | **최종 완료** – status=COMPLETED, **응답에 resultId 포함** (신규) |
| **GET** | `/results/{resultId}` | **검사 결과 상세 리포트** – 사용자 입력 + status, score, llmAdvice, medicalEvidence (신규) |

- `resultId` = 완료된 세션의 `sessionId`. submit 응답의 `resultId`로 바로 상세 조회 가능.

---

### 3. 검증 및 예외
- **step 저장**: 나이 15~44, 초경 연령 8~18 등 단계별 유효성 검사
- **예외 코드 추가**: `RESULT_NOT_FOUND` (결과 없음/미완료)

---

### 4. 기타 수정

- **submit 응답**: `{ "resultId": sessionId }` 반환 → 완료 직후 결과 상세 조회용
- **GET /results/{resultId}**: 스웨거에 “resultId = 완료된 세션의 sessionId” 설명 보강
- **DB 마이그레이션**: `src/main/resources/db/migration/V1__add_test_session_result_columns.sql`  
  - `current_step`, `sleep_hours`, `score`, `llm_advice`, `medical_evidence` 추가 (PostgreSQL, 수동 실행 또는 Flyway 사용 가능)

---

## 변경 파일 목록

**Entity**
- `domain/test/entity/TestSession.java` – 필드·setter 추가
- `domain/result/entity/TestResult.java` – Deprecated 및 주석

**DTO**
- `domain/test/dto/req/TestReqDTO.java` – `StepSaveReqDTO`
- `domain/test/dto/res/TestResDTO.java` – `CurrentSessionResDTO`, **`SubmitResDTO`**
- `domain/result/dto/res/ResultResDTO.java` – `ResultDetailResDTO`

**Repository**
- `domain/test/repository/TestSessionRepository.java` – `findByIdAndUser_Id`, `findTopByUserIdAndStatusOrderByCreatedAtDesc`

**Service**
- `domain/test/service/command/TestCommandService(Impl).java` – `saveStep`, **submit 반환값 변경**
- `domain/test/service/query/TestQueryService(Impl).java` – `getCurrentSession`
- `domain/result/service/ResultQueryService(Impl).java` – `getResultDetail`
- `domain/test/converter/TestConverter.java` – `toCurrentSessionResDTO`
- `domain/result/converter/ResultConverter.java` – `toResultDetailResDTO`

**Controller**
- `domain/test/controller/TestController.java` – step, current, **submit 응답 타입 변경**
- `domain/result/controller/ResultController.java` – GET `/results/{resultId}`

**Exception**
- `domain/test/exception/TestException.java`, `exception/code/TestErrorCode.java` – 신규 및 `RESULT_NOT_FOUND` 추가

**DB**
- `src/main/resources/db/migration/V1__add_test_session_result_columns.sql` – 신규

---

## 배포 전 확인

1. **DB**: 마이그레이션 스크립트 실행 또는 JPA ddl-auto로 컬럼 반영 여부 확인  
2. **인증**: `/tests/**`, `/results/**`는 기존대로 인증 필요  
3. **프론트**: submit 후 반환되는 `resultId`로 `GET /results/{resultId}` 호출 시 결과 상세 연동 확인
