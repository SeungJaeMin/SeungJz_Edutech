-- 초기 관리자 계정 생성
-- 비밀번호: admin123 (BCrypt 암호화)
-- ⚠️ 프로덕션 환경에서는 반드시 비밀번호를 변경하세요!

INSERT INTO admins (username, password, email, is_active, created_at)
VALUES (
    'admin',
    '$2a$10$rYQDqEF5YWQ5YXMxNjQ0OeBqDXR5N3Q0N3Q0N3Q0N3Q0N3Q0N3Q0N',  -- admin123
    'admin@nawbio.com',
    true,
    NOW()
)
ON CONFLICT (username) DO NOTHING;

-- 참고: 실제 BCrypt 해시를 생성하려면 애플리케이션을 실행한 후
-- AdminService의 createAdmin 메서드를 사용하거나
-- 온라인 BCrypt 생성기를 사용하세요.
