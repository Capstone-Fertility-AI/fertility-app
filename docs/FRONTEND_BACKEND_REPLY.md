# [백엔드 → 프론트] Capstone 앱 API 회신 (2026-05-20)

배포 Base: `http://3.27.238.246:8080`  
Swagger: http://3.27.238.246:8080/swagger-ui/index.html

---

## 회신 요청 답변

### 1) JSON 샘플

#### `GET /results/history?year=2026&month=5`

```json
{
  "isSuccess": true,
  "code": "RESULT_HISTORY_FETCHED",
  "message": "검사 이력 조회에 성공했습니다.",
  "result": {
    "year": 2026,
    "month": 5,
    "items": [
      {
        "resultId": 42,
        "sessionId": 38,
        "userId": 7,
        "aiScore": 89,
        "riskLevel": "SAFE",
        "topFactors": ["흡연", "음주"],
        "createdAt": "2026-05-20T14:32:10",
        "inspectedAt": "2026-05-20T14:32:10",
        "label": "89점 · SAFE"
      }
    ]
  }
}
```

- `year`·`month` 생략 시 **서버 현재 연·월(KST)** 기준.
- `label`: 보관함 카드용 (`{aiScore}점 · {riskLevel}`). `score` 필드명은 **`aiScore`** (홈 `recentTest.score`와 이름만 다름).

#### `GET /api/results/{resultId}`

```json
{
  "isSuccess": true,
  "code": "REPORT_GENERATED",
  "message": "상세 리포트가 생성되었습니다.",
  "result": {
    "resultId": 42,
    "nickname": "용암",
    "age": 36,
    "gender": "여성",
    "score": 29,
    "riskLevel": "DANGER",
    "intro": {
      "greeting": "안녕하세요, 용암님!",
      "scoreMessage": "현재 건강 점수는 29점으로, 개선이 필요해 보입니다.",
      "comfortMessage": "..."
    },
    "condition": {
      "sleepLabel": "다소 부족한 수면 (6시간)",
      "stressLabel": "높음 (PSS 30점/40점)",
      "summary": "..."
    },
    "factorAnalyses": [
      {
        "factor": "PCOS",
        "category": "질환",
        "mateThought": "...",
        "expectedChange": "..."
      }
    ],
    "missions": [
      {
        "missionId": 101,
        "title": "가벼운 유산소",
        "description": "...",
        "linkedFactor": "비만",
        "category": "EXERCISE",
        "frequency": { "type": "WEEKLY", "count": 3, "unit": "회" },
        "duration": { "value": 30, "unit": "분" },
        "difficulty": "EASY",
        "userAdjustable": true
      }
    ],
    "closing": "...",
    "questionnaireGroups": [
      {
        "title": "신체",
        "rows": [
          { "label": "나이", "value": "36세" },
          { "label": "키", "value": "167cm" },
          { "label": "몸무게", "value": "90kg" }
        ]
      },
      {
        "title": "생활습관",
        "rows": [
          { "label": "흡연", "value": "하루 10개비" },
          { "label": "음주", "value": "비음주" },
          { "label": "수면", "value": "6시간" }
        ]
      }
    ],
    "comparisonTable": [
      {
        "item": "BMI",
        "myValue": "32.3",
        "averageValue": "21.9",
        "comparisonResult": "47.1% 더 높습니다",
        "trend": "higher"
      }
    ]
  }
}
```

- **행동 가이드** = `missions` 배열.
- **AI 맞춤 분석** = `intro` + `condition` + `factorAnalyses` + `closing`.
- **설문 요약** = `questionnaireGroups` (프론트 로컬 조합 대신 서버 제공 권장).

---

### 2) `postId` / `commentId` 타입

- **P1 커뮤니티(미구현)** 설계 시 모두 **`number` (Long, JSON 정수)** 로 통일 예정.
- 기존 P0: `resultId`, `sessionId`, `missionId`, `userId` 모두 **Long → JSON number**.

---

### 3) 커뮤니티 비로그인 조회

- **현재**: 커뮤니티 API **미구현** (엔티티 스켈레톤만 존재).
- **1차 오픈안**: **전 구간 JWT 필수** (목록·상세 포함). 비로그인 조회 **미지원**.
- 공개 피드 필요 시 2차에서 `GET .../posts`만 `permitAll` 검토.

---

### 4) 일정

| 구분 | 상태 | 비고 |
|------|------|------|
| **P0** (홈·프로필·검사·리포트·보관함·미션) | **배포됨** (`develop`) | 이번 PR: 홈 `dailyRewardCapReached`·`flowerType`, history `label`, 여성 생활습관 원시값 |
| **P1** 커뮤니티 | **미착수** | API 명세 확정 후 2~3주 별도 PR 예상 (협의 필요) |

---

### 5) Swagger URL

http://3.27.238.246:8080/swagger-ui/index.html

---

## P0 스펙 정리 (프론트 요청 대비)

### `GET /home`

```json
{
  "result": {
    "user": {
      "nickname": "용암",
      "level": 3,
      "exp": 45,
      "dailyRewardCapReached": false,
      "flowerType": null
    },
    "recentTest": {
      "resultId": 42,
      "score": 89,
      "riskLevel": "SAFE",
      "topFactors": ["흡연"],
      "createdAt": "2026-05-20T14:32:10"
    },
    "todayMissions": { "total": 3, "missions": [] },
    "unreadNotiCount": 0,
    "actions": [
      { "type": "TEST", "title": "검사하기" },
      { "type": "GUIDE", "title": "검사 상세 리포트" }
    ]
  }
}
```

- `user`에 **`displayName` 없음** → `GET /users/me` 사용.
- `flowerType`: Lv.5 + 도감 보유 시 `PEONY` 등 enum **이름 문자열**, 그 외 `null`.
- `recentTest` 없으면 `null`, `actions`에 `GUIDE` 없음.

### 생활습관 PATCH (남성 `smokeStatus` 등)

**한글** 또는 **API 코드** 둘 다 수용:

| 구분 | API 코드 | 한글 |
|------|----------|------|
| 흡연 | `NEVER` / `OCCASIONAL` / `DAILY` | 안 피움 / 가끔 피움 / 매일 피움 |
| 음주 | `NEVER` / `MONTHLY_1_TO_3` / `WEEKLY_OR_MORE` | 안 마심 / 월 1~3회 / 주 1회 이상 |
| 폭음 | `NEVER` / `MONTHLY_1` / `WEEKLY_OR_MORE` | 없음 / 월 1회 / 주 1회 이상 |

### 여성 `PATCH .../step/female` (추가)

| 필드 | 설명 |
|------|------|
| `smokeLevel` | 0~2 **또는** 하루 **개비 수**(예: 10) |
| `binge12` | 0~2 **또는** 연간 **5잔+ 일수**(예: 100) |
| `cigarettesPerDay` | 개비 수 (권장, 명시) |
| `bingeDaysPerYear` | 폭음 일수 (권장) |
| `drinkStatus` | 남성과 동일 한글·코드 |

`GET /tests/{sessionId}` 응답에 **`smokeLabel`**, **`drinkLabel`**, **`bingeLabel`** 포함 → 요약 UI는 이 필드 사용 권장.

### 미션

| API | 비고 |
|-----|------|
| `GET /api/missions/today` | 오늘 슬롯 최대 3 |
| `POST /api/missions/{id}/complete` | `dailyRewardCapReached`, `newFlower` 등 |
| `POST /missions/sprout/reset-after-retest` | Lv.5 후 재검사 시 |

---

## P1 커뮤니티

요청 주신 REST 경로·필드는 **합의된 명세로 수용**. 구현 전까지 프론트 로컬 목 데이터 유지. 착수 시 이 문서 기준으로 Swagger·샘플 재공유.

---

## 배포 후 DB (여성 생활습관, 1회)

```sql
ALTER TABLE test_sessions ADD COLUMN IF NOT EXISTS cigarettes_per_day INTEGER;
ALTER TABLE test_sessions ADD COLUMN IF NOT EXISTS binge_days_per_year INTEGER;
```

문의: 백엔드 채널 / 이슈에 `resultId`·`sessionId` 첨부.
