-- ==============================================
-- 뉴스 영어 번역 데이터 추가
-- 기존 15개 뉴스에 대한 영어(en) 번역
-- ==============================================

-- 1. 2024년 하반기 신제품 출시 안내
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    1,
    'en',
    '2024 H2 New Product Launch Announcement',
    'Nawbio is launching three next-generation eco-friendly feed additives in the second half of 2024. We will provide safer and more effective products to our customers.',
    'Nawbio is launching three next-generation eco-friendly feed additives in the second half of 2024. We will provide safer and more effective products to our customers.'
);

-- 2. 나우산 플러스 리뉴얼 출시
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    2,
    'en',
    'Nowsan Plus Renewed Release',
    'The existing Nowsan product has been renewed to Nowsan Plus with enhanced efficacy and safety, creating a better livestock environment.',
    'The existing Nowsan product has been renewed to Nowsan Plus with enhanced efficacy and safety, creating a better livestock environment.'
);

-- 3. 2024 축산업 전망 및 트렌드 분석
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    3,
    'en',
    '2024 Livestock Industry Outlook and Trend Analysis',
    'An analysis of industry experts'' opinions on the future of sustainable livestock farming and the role of eco-friendly feed additives.',
    'An analysis of industry experts'' opinions on the future of sustainable livestock farming and the role of eco-friendly feed additives.'
);

-- 4. 2024 축산박람회 참가 안내
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    4,
    'en',
    '2024 Livestock Expo Participation Notice',
    'Nawbio will participate in the International Livestock Expo to be held in October. You can experience our new product lineup and technology firsthand.',
    'Nawbio will participate in the International Livestock Expo to be held in October. You can experience our new product lineup and technology firsthand.'
);

-- 5. ISO 22000 식품안전경영시스템 인증 획득
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    5,
    'en',
    'ISO 22000 Food Safety Management System Certification Acquired',
    'Nawbio has acquired ISO 22000 Food Safety Management System certification, establishing a safer product manufacturing system.',
    'Nawbio has acquired ISO 22000 Food Safety Management System certification, establishing a safer product manufacturing system.'
);

-- 6. 여름철 축산농가 지원 프로그램 시행
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    6,
    'en',
    'Summer Livestock Farm Support Program Implementation',
    'We are implementing a special product discount program for livestock stress management during the hot summer. We will work together for healthy livestock farming.',
    'We are implementing a special product discount program for livestock stress management during the hot summer. We will work together for healthy livestock farming.'
);

-- 7. 친환경 축산의 미래, 스마트팜 기술 동향
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    7,
    'en',
    'The Future of Eco-Friendly Livestock: Smart Farm Technology Trends',
    'A new paradigm of sustainable livestock farming is emerging through the combination of cutting-edge smart farm technology and eco-friendly livestock practices.',
    'A new paradigm of sustainable livestock farming is emerging through the combination of cutting-edge smart farm technology and eco-friendly livestock practices.'
);

-- 8. 나우바이오 공식 홈페이지 리뉴얼 안내
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    8,
    'en',
    'Nawbio Official Website Renewal Notice',
    'We have completely renewed our official website to provide better services to our customers.',
    'We have completely renewed our official website to provide better services to our customers.'
);

-- 9. 메탄킹 제품 수출 확대
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    9,
    'en',
    'MethanKing Product Export Expansion',
    'MethanKing, our methane reduction product, is expanding exports as it receives great response in the European market.',
    'MethanKing, our methane reduction product, is expanding exports as it receives great response in the European market.'
);

-- 10. 축산농가 대상 무료 기술 세미나
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    10,
    'en',
    'Free Technical Seminar for Livestock Farmers',
    'We are hosting a free technical seminar to improve livestock farm productivity. We look forward to your participation.',
    'We are hosting a free technical seminar to improve livestock farm productivity. We look forward to your participation.'
);

-- 11. 2024 글로벌 사료첨가제 시장 분석
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    11,
    'en',
    '2024 Global Feed Additive Market Analysis',
    'An in-depth analysis of the latest trends and outlook for the global feed additive market.',
    'An in-depth analysis of the latest trends and outlook for the global feed additive market.'
);

-- 12. 하계 휴무 안내
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    12,
    'en',
    'Summer Holiday Notice',
    'We would like to inform you of Nawbio''s summer holiday schedule. We appreciate your understanding.',
    'We would like to inform you of Nawbio''s summer holiday schedule. We appreciate your understanding.'
);

-- 13. 퀼라야 제품 품질 개선
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    13,
    'en',
    'Quillaja Product Quality Improvement',
    'We have improved the extraction process for Quillaja products to supply purer and more effective products.',
    'We have improved the extraction process for Quillaja products to supply purer and more effective products.'
);

-- 14. 신규 거래처 모집 설명회
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    14,
    'en',
    'New Business Partner Recruitment Briefing',
    'We are recruiting new business partners to work with Nawbio. Detailed information will be provided at the briefing session.',
    'We are recruiting new business partners to work with Nawbio. Detailed information will be provided at the briefing session.'
);

-- 15. 항생제 대체제 시장 급성장
INSERT INTO news_translations (news_id, language_code, title, excerpt, content)
VALUES (
    15,
    'en',
    'Rapid Growth of Antibiotic Alternative Market',
    'The natural alternative market is growing rapidly due to stricter regulations on antibiotic use.',
    'The natural alternative market is growing rapidly due to stricter regulations on antibiotic use.'
);

-- 결과 확인
SELECT
    n.id,
    n.category,
    nt_ko.title as title_ko,
    nt_en.title as title_en,
    n.published_at
FROM news n
LEFT JOIN news_translations nt_ko ON n.id = nt_ko.news_id AND nt_ko.language_code = 'ko'
LEFT JOIN news_translations nt_en ON n.id = nt_en.news_id AND nt_en.language_code = 'en'
ORDER BY n.published_at DESC
LIMIT 5;
