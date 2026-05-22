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

`ROUTINE` · `NUTRITION` · `QA` · `PROGRESS` · `MOTIVATION` (전체는 파라미터 생략)

## 정렬 (`sort`)

`latest`(기본) · `popular` · `comments` — 저장한 글: `bookmarkedOnly=true`

## 작성 Body

```json
{
  "category": "QA",
  "title": "제목(50자)",
  "body": "본문(4000자)",
  "tags": ["태그"],
  "imageUrls": ["https://.../uploaded.jpg"]
}
```

목록·상세: `likedByMe`, `bookmarkedByMe`, `isMine`, `likeCount`, `commentCount`

## 배포

운영 DB: **`docs/community-schema.sql`** 실행 후 배포.  
스모크: `python scripts/e2e_community.py`
