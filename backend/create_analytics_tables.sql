-- Analytics 테이블 생성
-- 페이지 방문 기록
CREATE TABLE IF NOT EXISTS page_views (
    id BIGSERIAL PRIMARY KEY,
    page_path VARCHAR(500) NOT NULL,
    visitor_hash VARCHAR(64) NOT NULL,  -- IP 해시값 (개인정보 보호)
    user_agent TEXT,
    device_type VARCHAR(20),  -- mobile, desktop, tablet
    country VARCHAR(100),
    referrer VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_page_views_path ON page_views(page_path);
CREATE INDEX IF NOT EXISTS idx_page_views_created_at ON page_views(created_at);
CREATE INDEX IF NOT EXISTS idx_page_views_visitor_hash ON page_views(visitor_hash);
CREATE INDEX IF NOT EXISTS idx_page_views_device_type ON page_views(device_type);

-- 일별 집계 테이블 (성능 최적화용)
CREATE TABLE IF NOT EXISTS daily_stats (
    id BIGSERIAL PRIMARY KEY,
    stat_date DATE NOT NULL,
    page_path VARCHAR(500),
    total_views INTEGER DEFAULT 0,
    unique_visitors INTEGER DEFAULT 0,
    device_type VARCHAR(20),
    country VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (stat_date, page_path, device_type, country)
);

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_daily_stats_date ON daily_stats(stat_date);
CREATE INDEX IF NOT EXISTS idx_daily_stats_path_date ON daily_stats(page_path, stat_date);

-- 코멘트 추가
COMMENT ON TABLE page_views IS 'Analytics 페이지 방문 원본 데이터';
COMMENT ON TABLE daily_stats IS 'Analytics 일별 집계 데이터';
COMMENT ON COLUMN page_views.visitor_hash IS 'IP 주소를 SHA-256 해시한 값 (개인정보 보호)';
COMMENT ON COLUMN page_views.device_type IS 'mobile, desktop, tablet 중 하나';
