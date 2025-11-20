-- ==============================================
-- 제품 데이터 마이그레이션 (9개 제품)
-- 프론트엔드 ProductsPage.tsx에서 추출
-- ==============================================

-- 1. 뉴폴 (돼지용)
INSERT INTO products (category, image_url, bg_color, display_order, is_active)
VALUES ('돼지용', '/img/product_Icon.png', '#6B8E23', 1, true);

INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    1,
    'ko',
    '뉴폴',
    '천연 사료첨가제',
    '돼지의 소화기능 향상과 면역력 강화를 위한 천연 사료첨가제입니다. 항생제 대체 효과로 안전한 축산물 생산을 지원합니다.',
    '["천연원료", "항생제 대체", "소화촉진", "면역강화"]'::jsonb
);

-- 2. 나우탄 (소용)
INSERT INTO products (category, image_url, bg_color, display_order, is_active)
VALUES ('소용', '/img/product_Icon.png', '#8B4513', 2, true);

INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    2,
    'ko',
    '나우탄',
    '소 전문 첨가제',
    '젖소와 육우의 건강한 성장과 생산성 향상을 위한 전문 첨가제입니다.',
    '["성장촉진", "생산성향상", "건강증진", "품질개선"]'::jsonb
);

-- 3. 그로프로 (닭용)
INSERT INTO products (category, image_url, bg_color, display_order, is_active)
VALUES ('닭용', '/img/product_Icon.png', '#FF8C00', 3, true);

INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    3,
    'ko',
    '그로프로',
    '육계 종합 영양제',
    '육계의 건강한 성장과 육질 개선을 위한 종합 영양제입니다. 스트레스 저항성을 높이고 폐사율을 낮춥니다.',
    '["성장촉진", "육질개선", "스트레스완화", "폐사율감소"]'::jsonb
);

-- 4. 메탄킹 (소용)
INSERT INTO products (category, image_url, bg_color, display_order, is_active)
VALUES ('소용', '/img/product_Icon.png', '#228B22', 4, true);

INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    4,
    'ko',
    '메탄킹',
    '친환경 사료첨가제',
    '메탄 배출 저감과 환경 개선을 위한 친환경 사료첨가제입니다.',
    '["메탄저감", "환경개선", "친환경", "지속가능"]'::jsonb
);

-- 5. 퀼라야 (전체)
INSERT INTO products (category, image_url, bg_color, display_order, is_active)
VALUES ('전체', '/img/product_Icon.png', '#6B8E23', 5, true);

INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    5,
    'ko',
    '퀼라야',
    '천연 추출물',
    '천연 퀼라야 추출물로 가축의 면역력을 강화하고 건강을 증진시킵니다.',
    '["천연추출물", "면역강화", "건강증진", "안전성"]'::jsonb
);

-- 6. 나우산 777 (돼지용)
INSERT INTO products (category, image_url, bg_color, display_order, is_active)
VALUES ('돼지용', '/img/product_Icon.png', '#6B8E23', 6, true);

INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    6,
    'ko',
    '나우산 777',
    '프리미엄 첨가제',
    '돼지의 장건강과 소화율 향상을 위한 프리미엄 첨가제입니다.',
    '["장건강", "소화율향상", "영양흡수", "성장촉진"]'::jsonb
);

-- 7. 몰임프린트 (전체)
INSERT INTO products (category, image_url, bg_color, display_order, is_active)
VALUES ('전체', '/img/product_Icon.png', '#4682B4', 7, true);

INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    7,
    'ko',
    '몰임프린트',
    '유기 미네랄 복합제',
    '미네랄 흡수율을 극대화한 유기 미네랄 복합제입니다.',
    '["미네랄흡수", "유기미네랄", "영양강화", "건강증진"]'::jsonb
);

-- 8. 몰리맥스 AE (전체)
INSERT INTO products (category, image_url, bg_color, display_order, is_active)
VALUES ('전체', '/img/product_Icon.png', '#4682B4', 8, true);

INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    8,
    'ko',
    '몰리맥스 AE',
    '종합 영양제',
    '필수 미네랄과 비타민을 균형있게 배합한 종합 영양제입니다.',
    '["종합영양", "비타민", "미네랄", "균형배합"]'::jsonb
);

-- 9. 나우스위트 (전체)
INSERT INTO products (category, image_url, bg_color, display_order, is_active)
VALUES ('전체', '/img/product_Icon.png', '#DC143C', 9, true);

INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    9,
    'ko',
    '나우스위트',
    '천연 감미료',
    '기호성 향상으로 사료 섭취량을 증가시키는 천연 감미료입니다.',
    '["기호성향상", "섭취증가", "천연원료", "안전성"]'::jsonb
);

-- 결과 확인
SELECT
    p.id,
    p.category,
    pt.name,
    pt.description,
    pt.features
FROM products p
LEFT JOIN product_translations pt ON p.id = pt.product_id
WHERE pt.language_code = 'ko'
ORDER BY p.display_order;
