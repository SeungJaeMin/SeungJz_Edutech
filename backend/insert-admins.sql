-- ==============================================
-- 관리자 계정 초기 데이터
-- BCrypt 암호화된 비밀번호 사용
-- ==============================================

-- ⚠️ 보안 경고:
-- 이 계정들은 초기 설정용입니다.
-- 프로덕션 환경에서는 반드시 비밀번호를 변경하세요!

-- 1. SUPER_ADMIN (최고 관리자)
-- username: superadmin
-- password: admin123
INSERT INTO admins (username, password, email, role, is_active, created_at)
VALUES (
    'superadmin',
    '$2a$10$02jcO1Rhk2Q42vX82br0X.Lsvp2zGcxRZCKu.cwR4GY9zaY0dK60e',
    'superadmin@nawbio.com',
    'SUPER_ADMIN',
    true,
    NOW()
);

-- 2. ADMIN (일반 관리자)
-- username: admin
-- password: admin456
-- Note: 기존에 이미 admin 계정이 있다면 아래 주석을 해제하여 비밀번호만 업데이트하세요
-- UPDATE admins SET password = '$2a$10$lAoXDgR5/Lw1uNT3VLhpMuGKmwz2Gtk0yOVzEGdjY/sLIesOKgRb.' WHERE username = 'admin';
INSERT INTO admins (username, password, email, role, is_active, created_at)
VALUES (
    'admin',
    '$2a$10$lAoXDgR5/Lw1uNT3VLhpMuGKmwz2Gtk0yOVzEGdjY/sLIesOKgRb.',
    'admin@nawbio.com',
    'ADMIN',
    true,
    NOW()
)
ON CONFLICT (username) DO UPDATE SET
    password = EXCLUDED.password,
    email = EXCLUDED.email;

-- 3. EDITOR (편집자)
-- username: editor
-- password: editor789
INSERT INTO admins (username, password, email, role, is_active, created_at)
VALUES (
    'editor',
    '$2a$10$aUL/BQnokcG9G5t/ZiHXyOZ9zoBc8FrbwmXVasEcGrrhm1vw9vKEW',
    'editor@nawbio.com',
    'EDITOR',
    true,
    NOW()
);

-- 결과 확인
SELECT id, username, email, role, is_active, created_at
FROM admins
ORDER BY id;

-- 비밀번호 정보 (개발용 참고)
-- superadmin : admin123
-- admin      : admin456
-- editor     : editor789
