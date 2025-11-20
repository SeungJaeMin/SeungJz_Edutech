-- System Settings 테이블 생성
CREATE TABLE IF NOT EXISTS system_settings (
    id BIGSERIAL PRIMARY KEY,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 기본 이메일 설정 데이터 삽입
INSERT INTO system_settings (setting_key, setting_value, description) VALUES
('email.smtp.host', 'smtp.gmail.com', 'SMTP 서버 주소'),
('email.smtp.port', '587', 'SMTP 포트'),
('email.smtp.username', '', '발신 이메일 계정'),
('email.smtp.password', '', '이메일 비밀번호 (앱 비밀번호)'),
('email.admin.address', 'admin@nawbio.com', '관리자 이메일 (알림 수신)'),
('email.enabled', 'false', '이메일 기능 활성화 여부')
ON CONFLICT (setting_key) DO NOTHING;

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_system_settings_key ON system_settings(setting_key);
