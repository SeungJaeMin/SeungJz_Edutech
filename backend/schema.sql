-- ============================================
-- 나우바이오 데이터베이스 스키마
-- 번역 테이블 분리 구조 (확장 가능한 다국어 지원)
-- ============================================

-- 1. 제품 관리 (언어 독립적 데이터)
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    image_url VARCHAR(500),
    bg_color VARCHAR(20) DEFAULT '#6B8E23',
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 1-1. 제품 번역 테이블
CREATE TABLE IF NOT EXISTS product_translations (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    language_code VARCHAR(5) NOT NULL,
    name VARCHAR(100) NOT NULL,
    subtitle TEXT,
    description TEXT,
    usage TEXT,
    features JSONB,
    benefits JSONB,
    specifications JSONB,
    UNIQUE(product_id, language_code)
);

-- 2. 관리자 계정 (뉴스 작성자 참조를 위해 먼저 생성)
CREATE TABLE IF NOT EXISTS admins (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) DEFAULT 'ADMIN',
    is_active BOOLEAN DEFAULT true,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW()
);

-- 3. 뉴스/공지사항 (언어 독립적 데이터)
CREATE TABLE IF NOT EXISTS news (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    thumbnail_url VARCHAR(500),
    content_images JSONB,
    views INT DEFAULT 0,
    is_published BOOLEAN DEFAULT false,
    published_at TIMESTAMP,
    author_id BIGINT REFERENCES admins(id),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 3-1. 뉴스 번역 테이블
CREATE TABLE IF NOT EXISTS news_translations (
    id BIGSERIAL PRIMARY KEY,
    news_id BIGINT NOT NULL REFERENCES news(id) ON DELETE CASCADE,
    language_code VARCHAR(5) NOT NULL,
    title VARCHAR(200) NOT NULL,
    excerpt TEXT,
    content TEXT,
    UNIQUE(news_id, language_code)
);

-- 3-2. 뉴스 이미지 (선택적 - 더 정교한 관리가 필요할 경우)
CREATE TABLE IF NOT EXISTS news_images (
    id BIGSERIAL PRIMARY KEY,
    news_id BIGINT NOT NULL REFERENCES news(id) ON DELETE CASCADE,
    image_url VARCHAR(500) NOT NULL,
    image_type VARCHAR(20) DEFAULT 'content',
    alt_text VARCHAR(200),
    caption TEXT,
    display_order INT DEFAULT 0,
    file_size INT,
    width INT,
    height INT,
    created_at TIMESTAMP DEFAULT NOW()
);

-- 4. 제품 문의
CREATE TABLE IF NOT EXISTS inquiries (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    subject VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    answer TEXT,
    answered_at TIMESTAMP,
    answered_by BIGINT REFERENCES admins(id),
    created_at TIMESTAMP DEFAULT NOW()
);

-- 5. 트래픽 로그 (Hot Data - 최근 30일)
CREATE TABLE IF NOT EXISTS traffic_logs (
    id BIGSERIAL PRIMARY KEY,
    page_path VARCHAR(200) NOT NULL,
    referrer VARCHAR(500),
    user_agent TEXT,
    ip_address INET,
    country VARCHAR(50),
    device_type VARCHAR(20),
    session_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT NOW()
);

-- 6. 트래픽 통계 (Aggregated - 일별 집계)
CREATE TABLE IF NOT EXISTS traffic_stats (
    id BIGSERIAL PRIMARY KEY,
    date DATE UNIQUE NOT NULL,
    total_visits INT DEFAULT 0,
    unique_visitors INT DEFAULT 0,
    page_views INT DEFAULT 0,
    top_pages JSONB,
    top_products JSONB,
    device_breakdown JSONB,
    created_at TIMESTAMP DEFAULT NOW()
);

-- ============================================
-- 인덱스 생성
-- ============================================

-- Products 인덱스
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_products_active ON products(is_active);
CREATE INDEX IF NOT EXISTS idx_products_display_order ON products(display_order);

-- Product Translations 인덱스
CREATE INDEX IF NOT EXISTS idx_product_trans_product_id ON product_translations(product_id);
CREATE INDEX IF NOT EXISTS idx_product_trans_lang ON product_translations(language_code);
CREATE INDEX IF NOT EXISTS idx_product_trans_composite ON product_translations(product_id, language_code);

-- News 인덱스
CREATE INDEX IF NOT EXISTS idx_news_published ON news(is_published, published_at DESC);
CREATE INDEX IF NOT EXISTS idx_news_author ON news(author_id);
CREATE INDEX IF NOT EXISTS idx_news_category ON news(category);

-- News Translations 인덱스
CREATE INDEX IF NOT EXISTS idx_news_trans_news_id ON news_translations(news_id);
CREATE INDEX IF NOT EXISTS idx_news_trans_lang ON news_translations(language_code);
CREATE INDEX IF NOT EXISTS idx_news_trans_composite ON news_translations(news_id, language_code);

-- News Images 인덱스
CREATE INDEX IF NOT EXISTS idx_news_images_news_id ON news_images(news_id);
CREATE INDEX IF NOT EXISTS idx_news_images_order ON news_images(news_id, display_order);

-- Inquiries 인덱스
CREATE INDEX IF NOT EXISTS idx_inquiries_status ON inquiries(status, created_at DESC);

-- Admins 인덱스
CREATE INDEX IF NOT EXISTS idx_admins_username ON admins(username);
CREATE INDEX IF NOT EXISTS idx_admins_active ON admins(is_active);

-- Traffic Logs 인덱스
CREATE INDEX IF NOT EXISTS idx_traffic_logs_created ON traffic_logs(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_traffic_logs_page ON traffic_logs(page_path);

-- Traffic Stats 인덱스
CREATE INDEX IF NOT EXISTS idx_traffic_stats_date ON traffic_stats(date DESC);
