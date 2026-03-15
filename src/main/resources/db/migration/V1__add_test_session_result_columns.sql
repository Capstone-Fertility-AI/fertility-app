-- TestSession 엔티티 추가 컬럼 (임시 저장/복구 및 검사 결과 상세 API용)
-- 수동 실행 또는 Flyway/Liquibase 도입 시 사용

ALTER TABLE test_sessions ADD COLUMN IF NOT EXISTS current_step INTEGER;
ALTER TABLE test_sessions ADD COLUMN IF NOT EXISTS sleep_hours INTEGER;
ALTER TABLE test_sessions ADD COLUMN IF NOT EXISTS score INTEGER;
ALTER TABLE test_sessions ADD COLUMN IF NOT EXISTS llm_advice TEXT;
ALTER TABLE test_sessions ADD COLUMN IF NOT EXISTS medical_evidence TEXT;

COMMENT ON COLUMN test_sessions.current_step IS '마지막 진행 단계 (1~9)';
COMMENT ON COLUMN test_sessions.sleep_hours IS '9번 질문: 수면 시간';
COMMENT ON COLUMN test_sessions.score IS '검사 점수';
COMMENT ON COLUMN test_sessions.llm_advice IS 'LLM 조언';
COMMENT ON COLUMN test_sessions.medical_evidence IS '의학적 근거';
