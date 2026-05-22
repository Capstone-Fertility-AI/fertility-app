# 커뮤니티 API 명세 (P1)

Base: `VITE_API_BASE_URL` · Swagger: `/swagger-ui/index.html`  
인증: **전 API JWT 필수** · ID: **number(Long)**

## 엔드포인트

| Method | Path |
|--------|------|
| GET | `/api/community/posts?category&sort=latest\|popular\|comments&q&bookmarkedOnly&page&size` |
| GET | `/api/community/posts/{postId}` |
| POST | `/api/community/posts` |
| PUT | `/api/community/posts/{postId}` |
| DELETE | `/api/community/posts/{postId}` |
| POST | `/api/community/posts/{postId}/like` |
| POST | `/api/community/posts/{postId}/bookmark` |
| GET | `/api/community/posts/{postId}/comments` |
| POST | `/api/community/posts/{postId}/comments` |
| PUT | `/api/community/comments/{commentId}` |
| DELETE | `/api/community/comments/{commentId}` |
| POST | `/api/community/comments/{commentId}/like` |
| POST | `/api/community/uploads/images` (multipart `file`) |
| POST | `/api/community/posts/{postId}/report` |
| POST | `/api/community/users/{userId}/block` |

## 카테고리 (`category`)

| API 값 (대문자) | UI 라벨 |
|----------------|---------|
| *(파라미터 생략)* | 전체 |
| `ROUTINE` | 운동 루틴 |
| `NUTRITION` | 식단·영양 |
| `QA` | 질문 |
| `PROGRESS` | 진척도 |
| `MOTIVATION` | 성공 스토리 |

## 정렬 (`sort`)

| 값 | UI |
|----|----|
| `latest` (기본) | 최신순 |
| `popular` | 인기순 (공감 많은 글) |
| `comments` | 댓글많은순 |

저장한 글 탭: `bookmarkedOnly=true`

## 작성 Body (`POST /api/community/posts`)

```json
{
  "category": "ROUTINE",
  "title": "주 3회 상체 루틴 공유",
  "body": "- 월: 가슴·삼두\n- 수: 등·이두\n- 금: 어깨·코어\n초보에게 적당한지 봐 주세요!",
  "tags": ["헬스", "루틴", "초보"],
  "imageUrls": ["https://.../uploaded.jpg"]
}
```

| 필드 | 제한 |
|------|------|
| `title` | 1~50자 |
| `body` | 1~4000자, **마크다운 raw 저장** (escape X) |
| `tags` | 최대 8개, 각 1~20자, **`#` 접두는 프론트가 제거 후 전송** |
| `imageUrls` | 최대 5개, `POST /uploads/images` 응답 URL만 |

목록·상세 응답: `likedByMe`, `bookmarkedByMe`, `isMine`, `likeCount`, `commentCount`, `bodyPreview`(목록만, 120자)

## 프론트 처리 권장 사항

- **태그 입력**: 화면의 "쉼표·공백 구분" 텍스트는 프론트에서 split → 배열로 전송  
- **`#` 접두**: UI에서 `#헬스`로 보여줘도 백엔드에는 `"헬스"`로 전송  
- **본문 마크다운**: 카드의 `bodyPreview`는 마크다운 그대로 옴 → 프론트에서 stripping 권장  
- **카드 닉네임 아바타**: `profileImageUrl: null`일 때 닉네임 첫 글자 placeholder  
- **저장한 글 빈 상태**: `bookmarkedOnly=true` 응답 `content: []`이면 빈 상태 UI 노출

## 배포

운영 DB: **`docs/community-schema.sql`** 실행 후 배포.  
스모크: `python scripts/e2e_community.py`
