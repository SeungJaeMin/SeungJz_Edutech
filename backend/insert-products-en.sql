-- ==============================================
-- 제품 영어 번역 데이터 추가
-- 기존 9개 제품에 대한 영어(en) 번역
-- ==============================================

-- 1. 뉴폴 (Newpol)
INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    1,
    'en',
    'Newpol',
    'Natural Feed Additive',
    'A natural feed additive designed to improve digestive function and strengthen immunity in pigs. It serves as an antibiotic alternative, supporting safe livestock production.',
    '["Natural Ingredients", "Antibiotic Alternative", "Digestive Support", "Immunity Enhancement"]'::jsonb
);

-- 2. 나우탄 (Nowtan)
INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    2,
    'en',
    'Nowtan',
    'Cattle Specialized Additive',
    'A specialized additive for healthy growth and improved productivity of dairy and beef cattle.',
    '["Growth Promotion", "Productivity Enhancement", "Health Improvement", "Quality Enhancement"]'::jsonb
);

-- 3. 그로프로 (GroPro)
INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    3,
    'en',
    'GroPro',
    'Broiler Comprehensive Nutrition',
    'A comprehensive nutritional supplement for healthy growth and meat quality improvement in broilers. Enhances stress resistance and reduces mortality rate.',
    '["Growth Promotion", "Meat Quality", "Stress Relief", "Mortality Reduction"]'::jsonb
);

-- 4. 메탄킹 (MethanKing)
INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    4,
    'en',
    'MethanKing',
    'Eco-Friendly Feed Additive',
    'An eco-friendly feed additive for methane emission reduction and environmental improvement.',
    '["Methane Reduction", "Environmental Improvement", "Eco-Friendly", "Sustainable"]'::jsonb
);

-- 5. 퀼라야 (Quillaja)
INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    5,
    'en',
    'Quillaja',
    'Natural Extract',
    'Strengthens livestock immunity and promotes health with natural Quillaja extract.',
    '["Natural Extract", "Immunity Enhancement", "Health Promotion", "Safety"]'::jsonb
);

-- 6. 나우산 777 (Nowsan 777)
INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    6,
    'en',
    'Nowsan 777',
    'Premium Additive',
    'A premium additive for intestinal health and improved digestibility in pigs.',
    '["Intestinal Health", "Digestibility", "Nutrient Absorption", "Growth Promotion"]'::jsonb
);

-- 7. 몰임프린트 (MolImprint)
INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    7,
    'en',
    'MolImprint',
    'Organic Mineral Complex',
    'An organic mineral complex that maximizes mineral absorption.',
    '["Mineral Absorption", "Organic Minerals", "Nutrition Enhancement", "Health Promotion"]'::jsonb
);

-- 8. 몰리맥스 AE (MolliMax AE)
INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    8,
    'en',
    'MolliMax AE',
    'Comprehensive Nutrition',
    'A comprehensive nutritional supplement with a balanced blend of essential minerals and vitamins.',
    '["Comprehensive Nutrition", "Vitamins", "Minerals", "Balanced Formula"]'::jsonb
);

-- 9. 나우스위트 (NowSweet)
INSERT INTO product_translations (product_id, language_code, name, subtitle, description, features)
VALUES (
    9,
    'en',
    'NowSweet',
    'Natural Sweetener',
    'A natural sweetener that increases feed intake by improving palatability.',
    '["Palatability Enhancement", "Intake Increase", "Natural Ingredients", "Safety"]'::jsonb
);

-- 결과 확인
SELECT
    p.id,
    p.category,
    pt_ko.name as name_ko,
    pt_en.name as name_en,
    pt_en.subtitle
FROM products p
LEFT JOIN product_translations pt_ko ON p.id = pt_ko.product_id AND pt_ko.language_code = 'ko'
LEFT JOIN product_translations pt_en ON p.id = pt_en.product_id AND pt_en.language_code = 'en'
ORDER BY p.display_order;
