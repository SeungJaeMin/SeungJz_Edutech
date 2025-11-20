-- ==============================================
-- 뉴스 데이터 마이그레이션 (15개 뉴스)
-- 프론트엔드 NewsPage.tsx에서 추출
-- ==============================================

-- 1. 2024년 하반기 신제품 출시 안내
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('공지사항', NULL, NULL, true, '2024-09-15', 1248);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    1,
    'ko',
    '2024년 하반기 신제품 출시 안내',
    '나우바이오에서 개발한 차세대 친환경 사료첨가제 3종이 하반기에 출시됩니다. 더욱 안전하고 효과적인 제품으로 고객 여러분께 다가가겠습니다.',
    '나우바이오에서 개발한 차세대 친환경 사료첨가제 3종이 하반기에 출시됩니다. 더욱 안전하고 효과적인 제품으로 고객 여러분께 다가가겠습니다.'
);

-- 2. 나우산 플러스 리뉴얼 출시
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('제품소식', NULL, NULL, true, '2024-09-10', 856);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    2,
    'ko',
    '나우산 플러스 리뉴얼 출시',
    '기존 나우산 제품이 더욱 개선된 나우산 플러스로 리뉴얼되었습니다. 향상된 효능과 안전성으로 더 나은 축산 환경을 만들어갑니다.',
    '기존 나우산 제품이 더욱 개선된 나우산 플러스로 리뉴얼되었습니다. 향상된 효능과 안전성으로 더 나은 축산 환경을 만들어갑니다.'
);

-- 3. 2024 축산업 전망 및 트렌드 분석
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('업계동향', NULL, NULL, true, '2024-09-05', 2156);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    3,
    'ko',
    '2024 축산업 전망 및 트렌드 분석',
    '지속가능한 축산업의 미래와 친환경 사료첨가제의 역할에 대해 업계 전문가들의 의견을 종합해 분석했습니다.',
    '지속가능한 축산업의 미래와 친환경 사료첨가제의 역할에 대해 업계 전문가들의 의견을 종합해 분석했습니다.'
);

-- 4. 2024 축산박람회 참가 안내
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('행사안내', NULL, NULL, true, '2024-08-28', 673);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    4,
    'ko',
    '2024 축산박람회 참가 안내',
    '오는 10월 개최되는 국제축산박람회에 나우바이오가 참가합니다. 새로운 제품 라인업과 기술을 직접 만나보실 수 있습니다.',
    '오는 10월 개최되는 국제축산박람회에 나우바이오가 참가합니다. 새로운 제품 라인업과 기술을 직접 만나보실 수 있습니다.'
);

-- 5. ISO 22000 식품안전경영시스템 인증 획득
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('공지사항', NULL, NULL, true, '2024-08-20', 1432);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    5,
    'ko',
    'ISO 22000 식품안전경영시스템 인증 획득',
    '나우바이오가 ISO 22000 식품안전경영시스템 인증을 획득하여 더욱 안전한 제품 생산 체계를 구축했습니다.',
    '나우바이오가 ISO 22000 식품안전경영시스템 인증을 획득하여 더욱 안전한 제품 생산 체계를 구축했습니다.'
);

-- 6. 여름철 축산농가 지원 프로그램 시행
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('제품소식', NULL, NULL, true, '2024-08-15', 924);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    6,
    'ko',
    '여름철 축산농가 지원 프로그램 시행',
    '무더운 여름철 가축 스트레스 관리를 위한 특별 제품 할인 프로그램을 시행합니다. 건강한 축산업을 위해 함께하겠습니다.',
    '무더운 여름철 가축 스트레스 관리를 위한 특별 제품 할인 프로그램을 시행합니다. 건강한 축산업을 위해 함께하겠습니다.'
);

-- 7. 친환경 축산의 미래, 스마트팜 기술 동향
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('업계동향', NULL, NULL, true, '2024-08-10', 1567);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    7,
    'ko',
    '친환경 축산의 미래, 스마트팜 기술 동향',
    '최신 스마트팜 기술과 친환경 축산의 결합으로 지속가능한 축산업의 새로운 패러다임이 열리고 있습니다.',
    '최신 스마트팜 기술과 친환경 축산의 결합으로 지속가능한 축산업의 새로운 패러다임이 열리고 있습니다.'
);

-- 8. 나우바이오 공식 홈페이지 리뉴얼 안내
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('공지사항', NULL, NULL, true, '2024-08-05', 892);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    8,
    'ko',
    '나우바이오 공식 홈페이지 리뉴얼 안내',
    '고객 여러분께 더 나은 서비스를 제공하기 위해 공식 홈페이지를 전면 리뉴얼하였습니다.',
    '고객 여러분께 더 나은 서비스를 제공하기 위해 공식 홈페이지를 전면 리뉴얼하였습니다.'
);

-- 9. 메탄킹 제품 수출 확대
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('제품소식', NULL, NULL, true, '2024-07-28', 1123);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    9,
    'ko',
    '메탄킹 제품 수출 확대',
    '메탄 저감 제품 메탄킹이 유럽 시장에서 큰 호응을 얻으며 수출이 확대되고 있습니다.',
    '메탄 저감 제품 메탄킹이 유럽 시장에서 큰 호응을 얻으며 수출이 확대되고 있습니다.'
);

-- 10. 축산농가 대상 무료 기술 세미나
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('행사안내', NULL, NULL, true, '2024-07-20', 745);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    10,
    'ko',
    '축산농가 대상 무료 기술 세미나',
    '축산농가의 생산성 향상을 위한 무료 기술 세미나를 개최합니다. 많은 참여 바랍니다.',
    '축산농가의 생산성 향상을 위한 무료 기술 세미나를 개최합니다. 많은 참여 바랍니다.'
);

-- 11. 2024 글로벌 사료첨가제 시장 분석
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('업계동향', NULL, NULL, true, '2024-07-15', 1876);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    11,
    'ko',
    '2024 글로벌 사료첨가제 시장 분석',
    '글로벌 사료첨가제 시장의 최신 동향과 전망에 대해 심층 분석하였습니다.',
    '글로벌 사료첨가제 시장의 최신 동향과 전망에 대해 심층 분석하였습니다.'
);

-- 12. 하계 휴무 안내
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('공지사항', NULL, NULL, true, '2024-07-10', 654);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    12,
    'ko',
    '하계 휴무 안내',
    '나우바이오의 하계 휴무 일정을 안내드립니다. 고객 여러분의 양해 부탁드립니다.',
    '나우바이오의 하계 휴무 일정을 안내드립니다. 고객 여러분의 양해 부탁드립니다.'
);

-- 13. 퀼라야 제품 품질 개선
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('제품소식', NULL, NULL, true, '2024-07-05', 987);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    13,
    'ko',
    '퀼라야 제품 품질 개선',
    '퀼라야 제품의 추출 공정을 개선하여 더욱 순수하고 효과적인 제품을 공급합니다.',
    '퀼라야 제품의 추출 공정을 개선하여 더욱 순수하고 효과적인 제품을 공급합니다.'
);

-- 14. 신규 거래처 모집 설명회
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('행사안내', NULL, NULL, true, '2024-06-28', 1234);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    14,
    'ko',
    '신규 거래처 모집 설명회',
    '나우바이오와 함께할 신규 거래처를 모집합니다. 설명회에서 자세한 내용을 안내드립니다.',
    '나우바이오와 함께할 신규 거래처를 모집합니다. 설명회에서 자세한 내용을 안내드립니다.'
);

-- 15. 항생제 대체제 시장 급성장
INSERT INTO news (category, thumbnail_url, content_images, is_published, published_at, views)
VALUES ('업계동향', NULL, NULL, true, '2024-06-20', 2345);

INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    15,
    'ko',
    '항생제 대체제 시장 급성장',
    '항생제 사용 규제 강화로 천연 대체제 시장이 급성장하고 있습니다.',
    '항생제 사용 규제 강화로 천연 대체제 시장이 급성장하고 있습니다.'
);

-- 결과 확인
SELECT
    n.id,
    n.category,
    nt.title,
    n.published_at,
    n.views
FROM news n
LEFT JOIN news_translations nt ON n.id = nt.news_id
WHERE nt.language_code = 'ko'
ORDER BY n.published_at DESC;
