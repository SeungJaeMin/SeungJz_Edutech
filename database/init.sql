-- ==============================================
-- SeungJz Edutech Database Initialization
-- ==============================================

-- 기존 테이블 삭제 (개발용)
DROP TABLE IF EXISTS interview_feedback CASCADE;
DROP TABLE IF EXISTS emotion_analysis CASCADE;
DROP TABLE IF EXISTS interview_qa_pairs CASCADE;
DROP TABLE IF EXISTS interview_sessions CASCADE;
DROP TABLE IF EXISTS answer_history CASCADE;
DROP TABLE IF EXISTS user_progress CASCADE;
DROP TABLE IF EXISTS components CASCADE;
DROP TABLE IF EXISTS lectures CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ENUM 타입 삭제
DROP TYPE IF EXISTS component_type CASCADE;
DROP TYPE IF EXISTS session_status CASCADE;
DROP TYPE IF EXISTS question_type CASCADE;
DROP TYPE IF EXISTS emotion_type CASCADE;
DROP TYPE IF EXISTS gaze_direction CASCADE;
DROP TYPE IF EXISTS feedback_type CASCADE;
DROP TYPE IF EXISTS severity CASCADE;

-- ==============================================
-- ENUM Types
-- ==============================================

CREATE TYPE component_type AS ENUM (
    'VIDEO_SEGMENT',
    'QUESTION',
    'MULTIPLE_CHOICE',
    'VOICE_ANSWER'
);

CREATE TYPE session_status AS ENUM (
    'waiting',
    'in_progress',
    'completed',
    'cancelled'
);

CREATE TYPE question_type AS ENUM (
    'initial',
    '5W1H',
    'follow_up',
    'clarification'
);

CREATE TYPE emotion_type AS ENUM (
    'happy',
    'sad',
    'angry',
    'fear',
    'surprise',
    'disgust',
    'neutral',
    'anxious',
    'confident',
    'confused',
    'engaged',
    'bored'
);

CREATE TYPE gaze_direction AS ENUM (
    'camera',
    'away',
    'down',
    'up',
    'left',
    'right'
);

CREATE TYPE feedback_type AS ENUM (
    'missing_keyword',
    'awkward_context',
    'poor_emotion',
    'poor_gaze',
    'speech_pace',
    'filler_words',
    'positive'
);

CREATE TYPE severity AS ENUM (
    'low',
    'medium',
    'high'
);

-- ==============================================
-- 1. Users Table (Phase 1)
-- ==============================================

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    provider VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    profile_image_url TEXT,
    preferred_language VARCHAR(10) DEFAULT 'ko',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP,
    UNIQUE(provider, provider_id)
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_provider ON users(provider, provider_id);

COMMENT ON TABLE users IS '사용자 정보 (소셜 로그인)';
COMMENT ON COLUMN users.provider IS 'GOOGLE, KAKAO';

-- ==============================================
-- 2. Lectures Table (Phase 2)
-- ==============================================

CREATE TABLE lectures (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    level INTEGER NOT NULL CHECK (level IN (1, 2, 3)),
    thumbnail_url TEXT,
    video_url TEXT,
    duration_sec INTEGER,
    category VARCHAR(50),
    difficulty VARCHAR(20),
    is_published BOOLEAN DEFAULT FALSE,
    view_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_lectures_level ON lectures(level);
CREATE INDEX idx_lectures_category ON lectures(category);
CREATE INDEX idx_lectures_published ON lectures(is_published);

COMMENT ON TABLE lectures IS '학습 컨텐츠 (1-2단계)';
COMMENT ON COLUMN lectures.level IS '1: K-Pop, 2: 드라마, 3: 면접(사용안함)';
COMMENT ON COLUMN lectures.category IS 'kpop, drama, business';
COMMENT ON COLUMN lectures.difficulty IS 'beginner, intermediate, advanced';

-- ==============================================
-- 3. Components Table (Phase 2)
-- ==============================================

CREATE TABLE components (
    id BIGSERIAL PRIMARY KEY,
    lecture_id BIGINT NOT NULL REFERENCES lectures(id) ON DELETE CASCADE,
    type component_type NOT NULL,
    sequence INTEGER NOT NULL,
    trigger_time_sec INTEGER,
    content JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(lecture_id, sequence)
);

CREATE INDEX idx_components_lecture ON components(lecture_id, sequence);

COMMENT ON TABLE components IS 'Lecture 구성 요소 (비디오, 질문, 답변)';
COMMENT ON COLUMN components.content IS 'JSONB 형태의 유연한 컨텐츠 저장';

-- ==============================================
-- 4. User Progress Table (Phase 2)
-- ==============================================

CREATE TABLE user_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    lecture_id BIGINT NOT NULL REFERENCES lectures(id) ON DELETE CASCADE,
    completed_components JSONB DEFAULT '[]',
    current_component_id BIGINT REFERENCES components(id),
    score DECIMAL(5,2),
    total_questions INTEGER DEFAULT 0,
    correct_answers INTEGER DEFAULT 0,
    last_accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_completed BOOLEAN DEFAULT FALSE,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    UNIQUE(user_id, lecture_id)
);

CREATE INDEX idx_progress_user ON user_progress(user_id);
CREATE INDEX idx_progress_lecture ON user_progress(lecture_id);
CREATE INDEX idx_progress_last_accessed ON user_progress(last_accessed_at DESC);

COMMENT ON TABLE user_progress IS '사용자 학습 진도';
COMMENT ON COLUMN user_progress.completed_components IS 'Component ID 배열 [1,2,3,5]';

-- ==============================================
-- 5. Answer History Table (Phase 2)
-- ==============================================

CREATE TABLE answer_history (
    id BIGSERIAL PRIMARY KEY,
    progress_id BIGINT NOT NULL REFERENCES user_progress(id) ON DELETE CASCADE,
    component_id BIGINT NOT NULL REFERENCES components(id) ON DELETE CASCADE,
    user_answer_text TEXT,
    audio_url TEXT,
    is_correct BOOLEAN NOT NULL,
    feedback_text TEXT,
    similarity_score DECIMAL(5,4),
    attempt_count INTEGER DEFAULT 1,
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_answer_progress ON answer_history(progress_id);
CREATE INDEX idx_answer_component ON answer_history(component_id);

COMMENT ON TABLE answer_history IS '학습 답변 이력';
COMMENT ON COLUMN answer_history.audio_url IS 'S3에 저장된 음성 파일 URL';

-- ==============================================
-- 6. Interview Sessions Table (Phase 4)
-- ==============================================

CREATE TABLE interview_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    prompt_text TEXT NOT NULL,
    duration_sec INTEGER DEFAULT 180,
    video_recording_url TEXT,
    audio_recording_url TEXT,
    overall_score DECIMAL(5,2),
    emotion_summary JSONB,
    transcript_summary TEXT,
    status session_status DEFAULT 'waiting',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_interview_user ON interview_sessions(user_id);
CREATE INDEX idx_interview_status ON interview_sessions(status);
CREATE INDEX idx_interview_created ON interview_sessions(created_at DESC);

COMMENT ON TABLE interview_sessions IS '3단계 면접 세션';
COMMENT ON COLUMN interview_sessions.emotion_summary IS '감정 통계 JSONB';

-- ==============================================
-- 7. Interview QA Pairs Table (Phase 4)
-- ==============================================

CREATE TABLE interview_qa_pairs (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES interview_sessions(id) ON DELETE CASCADE,
    sequence INTEGER NOT NULL,
    question_text TEXT NOT NULL,
    question_type question_type DEFAULT '5W1H',
    question_metadata JSONB,
    user_answer_transcript TEXT,
    answer_audio_url TEXT,
    answer_duration_sec INTEGER,
    missing_keywords JSONB DEFAULT '[]',
    context_score DECIMAL(5,4),
    is_context_awkward BOOLEAN DEFAULT FALSE,
    asked_at TIMESTAMP,
    answered_at TIMESTAMP,
    UNIQUE(session_id, sequence)
);

CREATE INDEX idx_qa_session ON interview_qa_pairs(session_id, sequence);

COMMENT ON TABLE interview_qa_pairs IS '면접 질문-답변 쌍';
COMMENT ON COLUMN interview_qa_pairs.missing_keywords IS '누락된 키워드 배열';

-- ==============================================
-- 8. Emotion Analysis Table (Phase 5)
-- ==============================================

CREATE TABLE emotion_analysis (
    id BIGSERIAL PRIMARY KEY,
    qa_pair_id BIGINT REFERENCES interview_qa_pairs(id) ON DELETE CASCADE,
    session_id BIGINT NOT NULL REFERENCES interview_sessions(id) ON DELETE CASCADE,
    timeline_sec INTEGER NOT NULL,
    emotion_type emotion_type NOT NULL,
    confidence_score DECIMAL(5,4) CHECK (confidence_score BETWEEN 0 AND 1),
    face_landmarks JSONB,
    gaze_direction gaze_direction,
    smile_intensity DECIMAL(5,4) CHECK (smile_intensity BETWEEN 0 AND 1),
    head_pose JSONB,
    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_emotion_session ON emotion_analysis(session_id, timeline_sec);
CREATE INDEX idx_emotion_qa ON emotion_analysis(qa_pair_id);

COMMENT ON TABLE emotion_analysis IS 'DeepFace 감정 분석 결과 (1초 단위)';

-- ==============================================
-- 9. Interview Feedback Table (Phase 6)
-- ==============================================

CREATE TABLE interview_feedback (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES interview_sessions(id) ON DELETE CASCADE,
    feedback_type feedback_type NOT NULL,
    timeline_sec INTEGER,
    severity severity DEFAULT 'medium',
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    suggestion TEXT,
    related_qa_id BIGINT REFERENCES interview_qa_pairs(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_feedback_session ON interview_feedback(session_id);
CREATE INDEX idx_feedback_type ON interview_feedback(feedback_type);

COMMENT ON TABLE interview_feedback IS '면접 피드백 (타임라인별)';

-- ==============================================
-- Full-text Search Indexes
-- ==============================================

CREATE INDEX idx_lectures_title_search ON lectures USING GIN(to_tsvector('korean', title));
CREATE INDEX idx_lectures_desc_search ON lectures USING GIN(to_tsvector('korean', description));

-- ==============================================
-- Success Message
-- ==============================================

DO $$
BEGIN
    RAISE NOTICE 'Database initialization completed successfully!';
    RAISE NOTICE 'Tables created: 9';
    RAISE NOTICE 'ENUM types created: 7';
END $$;
