#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""배포 서버 커뮤니티 API 스모크 테스트 (JWT 필요)."""
from __future__ import annotations

import json
import time
import urllib.error
import urllib.request

BASE = "http://3.27.238.246:8080"


def api(method: str, path: str, body: dict | None = None, token: str | None = None) -> dict:
    url = BASE + path
    headers = {"Content-Type": "application/json", "Accept": "application/json"}
    if token:
        headers["Authorization"] = f"Bearer {token}"
    data = json.dumps(body, ensure_ascii=False).encode("utf-8") if body is not None else None
    req = urllib.request.Request(url, data=data, headers=headers, method=method)
    try:
        with urllib.request.urlopen(req, timeout=60) as resp:
            return json.loads(resp.read().decode("utf-8"))
    except urllib.error.HTTPError as e:
        raw = e.read().decode("utf-8", errors="replace")
        raise RuntimeError(f"{method} {path} -> HTTP {e.code}: {raw}") from e


def assert_success(payload: dict, step: str) -> dict:
    if not payload.get("isSuccess"):
        raise RuntimeError(f"{step} failed: {payload}")
    return payload["result"]


def main() -> None:
    ts = int(time.time())
    email = f"e2e.community.{ts}@capstone.test"
    password = "TestPass12!"

    print("=== signup ===")
    signup = api(
        "POST",
        "/auth/signup",
        {"email": email, "password": password, "nickname": f"c{ts % 100000000}", "isTermsAgreed": True},
    )
    token = signup["result"]["accessToken"]

    print("=== create post ===")
    created = assert_success(
        api(
            "POST",
            "/api/community/posts",
            {
                "category": "QA",
                "title": "E2E 질문",
                "body": "커뮤니티 API 스모크 테스트 본문입니다.",
                "tags": ["e2e", "test"],
                "imageUrls": [],
            },
            token,
        ),
        "create post",
    )
    post_id = created["postId"]
    print(f"postId={post_id}")

    print("=== list posts ===")
    page = assert_success(
        api("GET", "/api/community/posts?sort=latest&page=0&size=5", token=token),
        "list",
    )
    assert any(item["postId"] == post_id for item in page["content"]), "created post missing in list"

    print("=== like / bookmark ===")
    like = assert_success(api("POST", f"/api/community/posts/{post_id}/like", token=token), "like")
    assert like["liked"] is True
    bm = assert_success(api("POST", f"/api/community/posts/{post_id}/bookmark", token=token), "bookmark")
    assert bm["bookmarked"] is True

    print("=== comment ===")
    comment = assert_success(
        api(
            "POST",
            f"/api/community/posts/{post_id}/comments",
            {"body": "응원합니다!", "parentCommentId": None},
            token,
        ),
        "comment",
    )
    comment_id = comment["commentId"]

    print("=== detail ===")
    detail = assert_success(api("GET", f"/api/community/posts/{post_id}", token=token), "detail")
    assert detail["commentCount"] >= 1
    assert detail["likedByMe"] is True
    assert detail["bookmarkedByMe"] is True

    print("=== bookmarked list ===")
    saved = assert_success(
        api("GET", "/api/community/posts?bookmarkedOnly=true", token=token),
        "bookmarked list",
    )
    assert any(item["postId"] == post_id for item in saved["content"])

    print("=== comment like ===")
    cl = assert_success(
        api("POST", f"/api/community/comments/{comment_id}/like", token=token),
        "comment like",
    )
    assert cl["liked"] is True

    print("OK community smoke passed")


if __name__ == "__main__":
    main()
