-- ==============================================
-- SeungJz Edutech - Sample Data (Seed)
-- ==============================================

-- ==============================================
-- 1. Sample Lectures (Phase 2 테스트용)
-- ==============================================

-- 1단계: K-Pop 컨텐츠
INSERT INTO lectures (title, description, level, category, difficulty, thumbnail_url, video_url, duration_sec, is_published) VALUES
('BTS로 배우는 기초 한국어', 'BTS의 노래 가사를 통해 자연스럽게 한국어 기초 표현을 배워보세요', 1, 'kpop', 'beginner', 'https://via.placeholder.com/400x300?text=BTS+Lesson', 'https://www.youtube.com/watch?v=sample', 180, true),
('블랙핑크와 함께하는 한국어 인사', '블랙핑크의 노래로 배우는 다양한 인사 표현들', 1, 'kpop', 'beginner', 'https://via.placeholder.com/400x300?text=BlackPink+Lesson', 'https://www.youtube.com/watch?v=sample2', 150, true);

-- 2단계: 드라마 컨텐츠
INSERT INTO lectures (title, description, level, category, difficulty, thumbnail_url, video_url, duration_sec, is_published) VALUES
('드라마 속 비즈니스 대화', '한국 드라마에서 자주 나오는 회사 내 대화 표현 배우기', 2, 'drama', 'intermediate', 'https://via.placeholder.com/400x300?text=Business+Drama', 'https://www.youtube.com/watch?v=sample3', 200, true),
('일상 대화 표현 익히기', '드라마 속 일상 생활 장면에서 배우는 한국어', 2, 'drama', 'intermediate', 'https://via.placeholder.com/400x300?text=Daily+Drama', 'https://www.youtube.com/watch?v=sample4', 180, true);

-- ==============================================
-- 2. Sample Components (Lecture 1용)
-- ==============================================

-- Lecture 1: BTS로 배우는 기초 한국어

-- Component 0: 비디오 시작 (0-30초)
INSERT INTO components (lecture_id, type, sequence, trigger_time_sec, content) VALUES
(1, 'VIDEO_SEGMENT', 0, NULL, '{
  "startTime": 0,
  "endTime": 30,
  "subtitles": "안녕하세요, 여러분! 오늘은 BTS와 함께 한국어를 배워볼까요?"
}');

-- Component 1: 질문
INSERT INTO components (lecture_id, type, sequence, trigger_time_sec, content) VALUES
(1, 'QUESTION', 1, 30, '{
  "questionText": "방금 들은 인사말을 따라 말해보세요",
  "questionAudio": null,
  "hintText": "한국에서 가장 기본적인 인사입니다"
}');

-- Component 2: 음성 답변
INSERT INTO components (lecture_id, type, sequence, trigger_time_sec, content) VALUES
(1, 'VOICE_ANSWER', 2, NULL, '{
  "expectedAnswer": "안녕하세요",
  "acceptableVariations": ["안녕", "안녕하십니까", "안녕하세요~"],
  "similarityThreshold": 0.75,
  "maxAttempts": 3
}');

-- Component 3: 비디오 계속 (30-60초)
INSERT INTO components (lecture_id, type, sequence, trigger_time_sec, content) VALUES
(1, 'VIDEO_SEGMENT', 3, NULL, '{
  "startTime": 30,
  "endTime": 60,
  "subtitles": "잘하셨어요! 이제 감사 표현을 배워볼게요. 고맙습니다!"
}');

-- Component 4: 질문 2
INSERT INTO components (lecture_id, type, sequence, trigger_time_sec, content) VALUES
(1, 'QUESTION', 4, 60, '{
  "questionText": "감사를 표현하는 말을 해보세요",
  "hintText": "'고맙습니다' 또는 '감사합니다'"
}');

-- Component 5: 음성 답변 2
INSERT INTO components (lecture_id, type, sequence, trigger_time_sec, content) VALUES
(1, 'VOICE_ANSWER', 5, NULL, '{
  "expectedAnswer": "감사합니다",
  "acceptableVariations": ["고맙습니다", "감사해요", "고마워요"],
  "similarityThreshold": 0.75,
  "maxAttempts": 3
}');

-- Component 6: 비디오 마무리 (60-90초)
INSERT INTO components (lecture_id, type, sequence, trigger_time_sec, content) VALUES
(1, 'VIDEO_SEGMENT', 6, NULL, '{
  "startTime": 60,
  "endTime": 90,
  "subtitles": "완벽합니다! 오늘 배운 표현들을 기억해주세요."
}');

-- Component 7: 객관식 퀴즈
INSERT INTO components (lecture_id, type, sequence, trigger_time_sec, content) VALUES
(1, 'MULTIPLE_CHOICE', 7, 90, '{
  "questionText": "다음 중 인사말이 아닌 것은?",
  "choices": [
    {"id": 1, "text": "안녕하세요", "audioUrl": null},
    {"id": 2, "text": "감사합니다", "audioUrl": null},
    {"id": 3, "text": "안녕히 가세요", "audioUrl": null},
    {"id": 4, "text": "맛있어요", "audioUrl": null}
  ],
  "correctChoiceId": 4
}');

-- ==============================================
-- 3. Sample Components (Lecture 2용)
-- ==============================================

INSERT INTO components (lecture_id, type, sequence, trigger_time_sec, content) VALUES
(2, 'VIDEO_SEGMENT', 0, NULL, '{
  "startTime": 0,
  "endTime": 30,
  "subtitles": "블랙핑크와 함께 다양한 인사를 배워봅시다!"
}');

INSERT INTO components (lecture_id, type, sequence, trigger_time_sec, content) VALUES
(2, 'QUESTION', 1, 30, '{
  "questionText": "아침 인사를 해보세요",
  "hintText": "좋은 아침입니다"
}');

INSERT INTO components (lecture_id, type, sequence, trigger_time_sec, content) VALUES
(2, 'VOICE_ANSWER', 2, NULL, '{
  "expectedAnswer": "좋은 아침입니다",
  "acceptableVariations": ["좋은 아침", "아침이에요", "좋은아침이에요"],
  "similarityThreshold": 0.7,
  "maxAttempts": 3
}');

-- ==============================================
-- 4. Sample User (테스트용)
-- ==============================================

INSERT INTO users (email, provider, provider_id, nickname, profile_image_url) VALUES
('test@example.com', 'GOOGLE', 'google_test_123', '테스트사용자', 'https://via.placeholder.com/150');

-- ==============================================
-- 5. Sample Progress (테스트용)
-- ==============================================

-- 사용자 1이 Lecture 1 시작
INSERT INTO user_progress (user_id, lecture_id, completed_components, current_component_id, score, total_questions, correct_answers) VALUES
(1, 1, '[1, 2, 3]', 4, 66.67, 3, 2);

-- ==============================================
-- 6. Sample Answer History (테스트용)
-- ==============================================

INSERT INTO answer_history (progress_id, component_id, user_answer_text, audio_url, is_correct, feedback_text, similarity_score, attempt_count) VALUES
(1, 2, '안녕하세요', 'https://s3.example.com/audio1.webm', true, '완벽합니다! 발음이 정확해요.', 0.95, 1),
(1, 2, '안녕', 'https://s3.example.com/audio2.webm', true, '좋아요! 조금 더 공손하게 말하면 "안녕하세요"입니다.', 0.80, 1),
(1, 5, '감사해요', 'https://s3.example.com/audio3.webm', false, '비슷해요! 좀 더 격식있게 "감사합니다"라고 말해보세요.', 0.70, 2);

-- ==============================================
-- Success Message
-- ==============================================

DO $$
DECLARE
    lecture_count INTEGER;
    component_count INTEGER;
    user_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO lecture_count FROM lectures;
    SELECT COUNT(*) INTO component_count FROM components;
    SELECT COUNT(*) INTO user_count FROM users;

    RAISE NOTICE 'Sample data inserted successfully!';
    RAISE NOTICE 'Lectures: %', lecture_count;
    RAISE NOTICE 'Components: %', component_count;
    RAISE NOTICE 'Users: %', user_count;
END $$;
