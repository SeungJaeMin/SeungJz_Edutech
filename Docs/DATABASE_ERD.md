# PostgreSQL ERD - SeungJz 해커톤 애플리케이션

## ERD 다이어그램

```
┌─────────────────────────────────────────────────────────────────┐
│                         데이터베이스 구조                          │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────┐         ┌──────────────────┐
│     users        │         │    lectures      │
├──────────────────┤         ├──────────────────┤
│ id (PK)          │         │ id (PK)          │
│ email            │         │ title            │
│ provider         │         │ description      │
│ provider_id      │         │ level (1,2,3)    │
│ nickname         │         │ thumbnail_url    │
│ profile_image    │         │ video_url        │
│ created_at       │         │ duration_sec     │
└────────┬─────────┘         │ category         │
         │                   │ created_at       │
         │                   └────────┬─────────┘
         │                            │
         │                            │ 1:N
         │                            │
         │                   ┌────────▼─────────┐
         │                   │   components     │
         │                   ├──────────────────┤
         │                   │ id (PK)          │
         │                   │ lecture_id (FK)  │
         │                   │ type             │
         │                   │ sequence         │
         │                   │ content (JSONB)  │
         │                   │ trigger_time_sec │
         │                   │ created_at       │
         │                   └──────────────────┘
         │
         │ 1:N
         │
┌────────▼─────────┐         ┌──────────────────┐
│  user_progress   │   N:1   │    lectures      │
├──────────────────┤◄────────┤                  │
│ id (PK)          │         └──────────────────┘
│ user_id (FK)     │
│ lecture_id (FK)  │
│ completed_components (JSONB) │  [1,2,3,5]
│ current_component_id │
│ score            │
│ total_questions  │
│ correct_answers  │
│ last_accessed_at │
│ is_completed     │
│ started_at       │
│ completed_at     │
└────────┬─────────┘
         │
         │ 1:N
         │
┌────────▼─────────┐
│ answer_history   │
├──────────────────┤
│ id (PK)          │
│ progress_id (FK) │
│ component_id (FK)│
│ user_answer_text │
│ audio_url        │  (녹음 파일 S3 URL)
│ is_correct       │
│ feedback_text    │
│ attempt_count    │
│ answered_at      │
└──────────────────┘


┌──────────────────┐
│ users            │
└────────┬─────────┘
         │
         │ 1:N
         │
┌────────▼─────────────────┐
│  interview_sessions      │  (3단계 전용)
├──────────────────────────┤
│ id (PK)                  │
│ user_id (FK)             │
│ prompt_text              │  사용자 입력 프롬프트
│ duration_sec             │  180초 (3분)
│ video_recording_url      │  녹화 영상 S3 URL
│ audio_recording_url      │  음성 파일 S3 URL
│ overall_score            │  종합 점수
│ emotion_summary (JSONB)  │  감정 통계
│ transcript_summary       │  전사 요약
│ status                   │  'in_progress', 'completed'
│ started_at               │
│ completed_at             │
└────────┬─────────────────┘
         │
         │ 1:N
         │
┌────────▼─────────────────┐
│  interview_qa_pairs      │  질문-답변 쌍
├──────────────────────────┤
│ id (PK)                  │
│ session_id (FK)          │
│ sequence                 │  1, 2, 3...
│ question_text            │  AI 생성 질문
│ question_type            │  '5W1H', 'follow_up'
│ user_answer_transcript   │  STT 결과
│ answer_audio_url         │  답변 음성 S3
│ answer_duration_sec      │  답변 길이
│ missing_keywords (JSONB) │  누락된 키워드들
│ is_context_awkward       │  문맥 어색함 여부
│ asked_at                 │
│ answered_at              │
└────────┬─────────────────┘
         │
         │ 1:N
         │
┌────────▼─────────────────┐
│  emotion_analysis        │  감정 분석 결과
├──────────────────────────┤
│ id (PK)                  │
│ qa_pair_id (FK)          │
│ session_id (FK)          │
│ timeline_sec             │  타임라인 위치
│ emotion_type             │  'happy', 'neutral', 'anxious'
│ confidence_score         │  0.0 ~ 1.0
│ face_landmarks (JSONB)   │  얼굴 랜드마크 데이터
│ gaze_direction           │  'camera', 'away', 'down'
│ smile_intensity          │  0.0 ~ 1.0
│ detected_at              │
└──────────────────────────┘


┌────────▼─────────────────┐
│  interview_feedback      │  최종 피드백
├──────────────────────────┤
│ id (PK)                  │
│ session_id (FK)          │
│ feedback_type            │  'keyword', 'context', 'emotion', 'gaze'
│ timeline_sec             │  문제 발생 시점
│ severity                 │  'low', 'medium', 'high'
│ title                    │  "시선 처리 개선 필요"
│ description              │  상세 설명
│ suggestion               │  개선 제안
│ related_qa_id (FK)       │  관련 질문-답변
│ created_at               │
└──────────────────────────┘
```

---

## 테이블 상세 설명

### 1. users (사용자)

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    provider VARCHAR(50) NOT NULL,  -- 'GOOGLE', 'KAKAO'
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
```

---

### 2. lectures (학습 컨텐츠)

```sql
CREATE TABLE lectures (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    level INTEGER NOT NULL CHECK (level IN (1, 2, 3)),
    thumbnail_url TEXT,
    video_url TEXT,  -- S3 URL (1,2단계만)
    duration_sec INTEGER,
    category VARCHAR(50),  -- 'kpop', 'drama', 'business'
    difficulty VARCHAR(20),  -- 'beginner', 'intermediate', 'advanced'
    is_published BOOLEAN DEFAULT FALSE,
    view_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_lectures_level ON lectures(level);
CREATE INDEX idx_lectures_category ON lectures(category);
CREATE INDEX idx_lectures_published ON lectures(is_published);
```

**level 구분:**
- 1: K-Pop 컨텐츠
- 2: 드라마 시나리오
- 3: 실시간 면접 (Lecture 없이 바로 Session 생성)

---

### 3. components (Lecture 구성요소)

```sql
CREATE TYPE component_type AS ENUM (
    'VIDEO_SEGMENT',      -- 비디오 구간
    'QUESTION',           -- 질문
    'MULTIPLE_CHOICE',    -- 객관식
    'VOICE_ANSWER'        -- 음성 답변
);

CREATE TABLE components (
    id BIGSERIAL PRIMARY KEY,
    lecture_id BIGINT NOT NULL REFERENCES lectures(id) ON DELETE CASCADE,
    type component_type NOT NULL,
    sequence INTEGER NOT NULL,  -- 순서 (0, 1, 2, ...)
    trigger_time_sec INTEGER,   -- 비디오 멈추는 시점
    content JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(lecture_id, sequence)
);

CREATE INDEX idx_components_lecture ON components(lecture_id, sequence);
```

**content JSONB 예시:**

```json
// VIDEO_SEGMENT
{
  "videoUrl": "https://s3.../lecture1/segment1.mp4",
  "startTime": 0,
  "endTime": 30,
  "subtitles": "안녕하세요..."
}

// QUESTION
{
  "questionText": "이 상황에서 적절한 표현은?",
  "questionAudio": "https://s3.../question1.mp3",
  "hintText": "격식 있는 표현을 사용하세요"
}

// MULTIPLE_CHOICE
{
  "choices": [
    {"id": 1, "text": "안녕하세요", "audioUrl": "..."},
    {"id": 2, "text": "안녕", "audioUrl": "..."}
  ],
  "correctChoiceId": 1
}

// VOICE_ANSWER
{
  "expectedAnswer": "감사합니다",
  "acceptableVariations": ["감사해요", "고맙습니다"],
  "similarityThreshold": 0.8,
  "maxAttempts": 3
}
```

---

### 4. user_progress (학습 진도)

```sql
CREATE TABLE user_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    lecture_id BIGINT NOT NULL REFERENCES lectures(id) ON DELETE CASCADE,
    completed_components JSONB DEFAULT '[]',  -- [1, 2, 3, 5]
    current_component_id BIGINT REFERENCES components(id),
    score DECIMAL(5,2),  -- 정답률 (0.00 ~ 100.00)
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
```

---

### 5. answer_history (답변 이력)

```sql
CREATE TABLE answer_history (
    id BIGSERIAL PRIMARY KEY,
    progress_id BIGINT NOT NULL REFERENCES user_progress(id) ON DELETE CASCADE,
    component_id BIGINT NOT NULL REFERENCES components(id) ON DELETE CASCADE,
    user_answer_text TEXT,  -- STT 결과 또는 선택지
    audio_url TEXT,  -- 음성 녹음 파일 S3 URL
    is_correct BOOLEAN NOT NULL,
    feedback_text TEXT,  -- "발음이 조금 부정확합니다"
    similarity_score DECIMAL(5,4),  -- 0.0000 ~ 1.0000
    attempt_count INTEGER DEFAULT 1,
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_answer_progress ON answer_history(progress_id);
CREATE INDEX idx_answer_component ON answer_history(component_id);
```

---

### 6. interview_sessions (3단계 면접 세션)

```sql
CREATE TYPE session_status AS ENUM ('waiting', 'in_progress', 'completed', 'cancelled');

CREATE TABLE interview_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    prompt_text TEXT NOT NULL,  -- "마케팅 인턴 면접을 준비하고 있습니다"
    duration_sec INTEGER DEFAULT 180,
    video_recording_url TEXT,  -- 전체 세션 녹화 영상
    audio_recording_url TEXT,
    overall_score DECIMAL(5,2),  -- 종합 점수 (0 ~ 100)
    emotion_summary JSONB,  -- 감정 통계
    transcript_summary TEXT,  -- 전체 대화 요약
    status session_status DEFAULT 'waiting',
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_interview_user ON interview_sessions(user_id);
CREATE INDEX idx_interview_status ON interview_sessions(status);
CREATE INDEX idx_interview_created ON interview_sessions(created_at DESC);
```

**emotion_summary JSONB 예시:**

```json
{
  "avgConfidence": 0.75,
  "avgSmileIntensity": 0.4,
  "gazeCameraPercent": 65,
  "emotionDistribution": {
    "happy": 20,
    "neutral": 60,
    "anxious": 15,
    "confused": 5
  },
  "emotionTimeline": [
    {"time": 0, "emotion": "neutral", "confidence": 0.9},
    {"time": 30, "emotion": "anxious", "confidence": 0.7}
  ]
}
```

---

### 7. interview_qa_pairs (면접 질문-답변)

```sql
CREATE TYPE question_type AS ENUM ('initial', '5W1H', 'follow_up', 'clarification');

CREATE TABLE interview_qa_pairs (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES interview_sessions(id) ON DELETE CASCADE,
    sequence INTEGER NOT NULL,  -- 1, 2, 3...
    question_text TEXT NOT NULL,
    question_type question_type DEFAULT '5W1H',
    question_metadata JSONB,  -- GPT 생성 시 메타데이터
    user_answer_transcript TEXT,
    answer_audio_url TEXT,
    answer_duration_sec INTEGER,
    missing_keywords JSONB DEFAULT '[]',  -- ["팀워크", "리더십"]
    context_score DECIMAL(5,4),  -- 문맥 자연스러움 (0 ~ 1)
    is_context_awkward BOOLEAN DEFAULT FALSE,
    asked_at TIMESTAMP,
    answered_at TIMESTAMP,
    UNIQUE(session_id, sequence)
);

CREATE INDEX idx_qa_session ON interview_qa_pairs(session_id, sequence);
```

---

### 8. emotion_analysis (감정 분석 결과)

```sql
CREATE TYPE emotion_type AS ENUM (
    'happy', 'sad', 'angry', 'fear', 'surprise', 'disgust', 'neutral',
    'anxious', 'confident', 'confused', 'engaged', 'bored'
);

CREATE TYPE gaze_direction AS ENUM ('camera', 'away', 'down', 'up', 'left', 'right');

CREATE TABLE emotion_analysis (
    id BIGSERIAL PRIMARY KEY,
    qa_pair_id BIGINT REFERENCES interview_qa_pairs(id) ON DELETE CASCADE,
    session_id BIGINT NOT NULL REFERENCES interview_sessions(id) ON DELETE CASCADE,
    timeline_sec INTEGER NOT NULL,  -- 세션 시작 후 초
    emotion_type emotion_type NOT NULL,
    confidence_score DECIMAL(5,4) CHECK (confidence_score BETWEEN 0 AND 1),
    face_landmarks JSONB,  -- DeepFace 랜드마크 데이터
    gaze_direction gaze_direction,
    smile_intensity DECIMAL(5,4) CHECK (smile_intensity BETWEEN 0 AND 1),
    head_pose JSONB,  -- {"pitch": 10, "yaw": -5, "roll": 2}
    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_emotion_session ON emotion_analysis(session_id, timeline_sec);
CREATE INDEX idx_emotion_qa ON emotion_analysis(qa_pair_id);
```

---

### 9. interview_feedback (피드백)

```sql
CREATE TYPE feedback_type AS ENUM (
    'missing_keyword',   -- 키워드 누락
    'awkward_context',   -- 어색한 문맥
    'poor_emotion',      -- 감정 표현 부족
    'poor_gaze',         -- 시선 처리 미흡
    'speech_pace',       -- 말하기 속도
    'filler_words',      -- 불필요한 추임새
    'positive'           -- 잘한 점
);

CREATE TYPE severity AS ENUM ('low', 'medium', 'high');

CREATE TABLE interview_feedback (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES interview_sessions(id) ON DELETE CASCADE,
    feedback_type feedback_type NOT NULL,
    timeline_sec INTEGER,  -- 문제 발생 시점 (NULL이면 전체 피드백)
    severity severity DEFAULT 'medium',
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    suggestion TEXT,  -- 개선 제안
    related_qa_id BIGINT REFERENCES interview_qa_pairs(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_feedback_session ON interview_feedback(session_id);
CREATE INDEX idx_feedback_type ON interview_feedback(feedback_type);
```

**피드백 예시:**

```sql
INSERT INTO interview_feedback (session_id, feedback_type, timeline_sec, severity, title, description, suggestion)
VALUES
(1, 'missing_keyword', 45, 'high',
 '핵심 키워드 누락',
 '"팀워크"와 "협업"이라는 키워드를 언급하지 않았습니다.',
 '팀 프로젝트 경험을 이야기할 때는 구체적인 협업 사례를 포함하세요.'),

(1, 'poor_gaze', 120, 'medium',
 '시선 처리 개선 필요',
 '답변 중 35%의 시간 동안 카메라를 보지 않았습니다.',
 '자신감을 보이기 위해 면접관(카메라)과 아이컨택을 유지하세요.');
```

---

## 주요 쿼리 패턴

### 1. 학습 진도 조회

```sql
-- 사용자의 전체 학습 진도
SELECT
    l.id,
    l.title,
    l.level,
    l.thumbnail_url,
    up.score,
    up.is_completed,
    up.last_accessed_at,
    COALESCE(jsonb_array_length(up.completed_components), 0) as completed_count,
    (SELECT COUNT(*) FROM components WHERE lecture_id = l.id) as total_count
FROM lectures l
LEFT JOIN user_progress up ON l.id = up.lecture_id AND up.user_id = 1
WHERE l.is_published = true
ORDER BY l.level, l.id;
```

### 2. Component 순차 로드

```sql
-- Lecture의 모든 Component 순서대로
SELECT *
FROM components
WHERE lecture_id = 1
ORDER BY sequence;
```

### 3. 3단계 면접 결과 조회

```sql
-- 세션 + 감정 통계 + 질문 개수
SELECT
    s.id,
    s.prompt_text,
    s.overall_score,
    s.emotion_summary,
    s.started_at,
    s.completed_at,
    COUNT(DISTINCT qa.id) as total_questions,
    COUNT(DISTINCT f.id) as feedback_count
FROM interview_sessions s
LEFT JOIN interview_qa_pairs qa ON s.id = qa.session_id
LEFT JOIN interview_feedback f ON s.id = f.session_id
WHERE s.user_id = 1 AND s.status = 'completed'
GROUP BY s.id
ORDER BY s.completed_at DESC;
```

### 4. 타임라인별 피드백 (학습 복기)

```sql
-- 특정 세션의 타임라인 피드백
SELECT
    f.timeline_sec,
    f.feedback_type,
    f.severity,
    f.title,
    f.description,
    f.suggestion,
    qa.question_text,
    qa.user_answer_transcript
FROM interview_feedback f
LEFT JOIN interview_qa_pairs qa ON f.related_qa_id = qa.id
WHERE f.session_id = 1
ORDER BY f.timeline_sec;
```

### 5. 감정 변화 타임라인

```sql
-- 세션 중 감정 변화 추이
SELECT
    timeline_sec,
    emotion_type,
    confidence_score,
    smile_intensity,
    gaze_direction
FROM emotion_analysis
WHERE session_id = 1
ORDER BY timeline_sec;
```

---

## 인덱스 전략

```sql
-- 성능 최적화 인덱스
CREATE INDEX idx_components_lecture_seq ON components(lecture_id, sequence);
CREATE INDEX idx_progress_user_last ON user_progress(user_id, last_accessed_at DESC);
CREATE INDEX idx_interview_user_status ON interview_sessions(user_id, status);
CREATE INDEX idx_emotion_session_timeline ON emotion_analysis(session_id, timeline_sec);
CREATE INDEX idx_feedback_session_type ON interview_feedback(session_id, feedback_type);

-- Full-text search (학습 컨텐츠 검색)
CREATE INDEX idx_lectures_title_search ON lectures USING GIN(to_tsvector('korean', title));
CREATE INDEX idx_lectures_desc_search ON lectures USING GIN(to_tsvector('korean', description));
```

---

## 데이터 크기 예측

### 해커톤 3일 + 데모 (가정: 50명 사용자)

| 테이블 | 레코드 수 | 예상 크기 |
|--------|----------|----------|
| users | 50 | 10 KB |
| lectures | 20 | 5 KB |
| components | 200 | 100 KB |
| user_progress | 500 | 50 KB |
| answer_history | 2,000 | 1 MB |
| interview_sessions | 100 | 50 KB |
| interview_qa_pairs | 500 | 200 KB |
| emotion_analysis | 5,000 | 2 MB |
| interview_feedback | 500 | 200 KB |
| **총합** | | **~3.6 MB** |

**결론**: PostgreSQL 무료 티어 충분 (Render.com 1GB / Supabase 500MB)

---

## 백업 및 마이그레이션

```sql
-- 백업
pg_dump -U postgres edutech > backup.sql

-- 복원
psql -U postgres edutech < backup.sql

-- 마이그레이션 (Flyway/Liquibase)
-- V1__init_schema.sql
-- V2__add_emotion_analysis.sql
```
