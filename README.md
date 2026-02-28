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

# 📝 커밋 메시지 규칙 (Commit Convention)

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


# 🤝 Pull Request (PR) 및 코드 리뷰 규칙

다른 팀원의 코드를 합치기(Merge) 전에 확인하는 과정입니다.

PR 목적지 확인: 항상 feature/... 브랜치에서 develop 브랜치로 PR을 엽니다!

PR 제목: 커밋 메시지와 동일한 규칙 적용 (예: feat: 메인 홈 화면 UI 구현)

PR 내용:

무엇을 개발했나요?

스크린샷 (UI 변경이 있다면 캡처 첨부 필수!)

리뷰어가 특별히 봐줬으면 하는 부분 (예: "경험치 계산 로직이 맞는지 확인해 주세요.")

리뷰(Review) 필수: - 혼자서 PR 열고 바로 Merge 하는 것은 금지! ❌

최소 1명 이상의 팀원이 코드를 보고 Approve(승인)를 눌러야 Merge 할 수 있도록 GitHub 설정(Branch Protection)을 걸어둡니다.

# 🎯 이슈 (Issue) 관리

"내가 오늘 할 일", "발생한 버그"는 카카오톡이 아니라 GitHub Issues에 등록합니다.

기능 개발을 시작하기 전 Issue를 먼저 만듭니다. (예: Issue #1: 설문조사 화면 만들기)

브랜치 이름이나 커밋 메시지에 이슈 번호를 적으면 추적이 쉽습니다. (예: feat: 설문조사 UI 구현 (#1))

GitHub Projects(칸반 보드)를 활용해 To Do, In Progress, Done으로 일감을 옮겨가며 프로젝트 진행 상황을 눈으로 확인합니다.
