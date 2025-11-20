# API 구현 명세서 - 프론트엔드 기반

## 개요

현재 구현된 프론트엔드 온보딩 플로우를 기반으로 백엔드에서 구현해야 할 API 목록입니다.

- **Base URL**: `http://localhost:8080/api`
- **Frontend**: React (Vite) - 5개 온보딩 화면 구현 완료
- **Authentication**: JWT Bearer Token
- **Content-Type**: `application/json`

---

## 구현 우선순위

### 🔴 Phase 1: 필수 (MVP) - 1주차
1. 학습 컨텐츠 (Lecture) 조회 API
2. Lecture 업로드 및 자동 생성 (LLM)
3. 1-2단계 학습 진행 API
4. 음성 답변 제출 및 검증

### 🟡 Phase 2: 3단계 실시간 면접 - 2주차
1. 실시간 녹화/녹음 API
2. 실시간 감정 분석 (DeepFace)
3. 실시간 질문 생성 (LLM)
4. TTS 음성 합성

### 🟢 Phase 3: 결과 분석 및 피드백 - 3주차
1. LLM 기반 결과 분석
2. 파형(Waveform) 표시 API
3. 타임라인 피드백 핀
4. 학습 기록 조회

---

## 1. 학습 컨텐츠 (Lecture) API (Phase 1)

### 1.1 Lecture 목록 조회

```http
GET /api/lectures
```

**설명**: 사용자에게 표시할 Lecture 목록을 조회합니다.

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Query Parameters**:
- `level` (integer, optional): 1, 2 (1-2단계만, 3단계는 별도)
- `page` (integer, optional, default: 0): 페이지 번호
- `size` (integer, optional, default: 10): 페이지 크기

**Response** (200 OK):
```json
{
  "content": [
    {
      "id": 1,
      "title": "BTS 노래로 배우는 한국어",
      "description": "K-Pop을 통해 자연스럽게 한국어 배우기",
      "level": 1,
      "thumbnailUrl": "/img/lecture1.jpg",
      "durationMinutes": 15,
      "quizCount": 5,
      "isCompleted": false,
      "createdAt": "2025-01-20T10:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 25,
    "totalPages": 3
  }
}
```

---

### 1.2 Lecture 상세 조회 (with 퀴즈 데이터)

```http
GET /api/lectures/{lectureId}
```

**Parameters**:
- `lectureId` (integer, required): Lecture ID

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "title": "BTS 노래로 배우는 한국어",
  "description": "K-Pop을 통해 자연스럽게 한국어 배우기",
  "level": 1,
  "thumbnailUrl": "/img/lecture1.jpg",
  "videoUrl": "/api/videos/lecture_1.mp4",
  "durationSeconds": 300,
  "quizzes": [
    {
      "id": 101,
      "sequence": 1,
      "triggerTimeSec": 45,
      "questionText": "방금 들은 인사말은 무엇인가요?",
      "expectedAnswer": "안녕하세요",
      "acceptableAnswers": ["안녕", "안녕하십니까"],
      "hintText": "한국에서 가장 많이 쓰는 인사",
      "correctFeedback": "완벽합니다! 👍",
      "incorrectFeedback": "조금 더 정확하게 말해보세요.",
      "maxAttempts": 3
    },
    {
      "id": 102,
      "sequence": 2,
      "triggerTimeSec": 120,
      "questionText": "자기소개를 해보세요",
      "expectedAnswer": "저는 [이름]입니다",
      "acceptableAnswers": [],
      "hintText": "'저는'으로 시작하세요",
      "correctFeedback": "잘했어요!",
      "incorrectFeedback": "다시 한번 시도해보세요.",
      "maxAttempts": 3
    }
  ],
  "createdAt": "2025-01-20T10:00:00"
}
```

---

### 1.3 Lecture 업로드 및 자동 생성 (LLM)

```http
POST /api/lectures/upload
```

**설명**: MP4 파일을 업로드하면 LLM이 자동으로 Lecture와 퀴즈를 생성합니다.

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: multipart/form-data
```

**Request Body** (Multipart Form):
```
video: (file) lecture_video.mp4
title: "BTS 노래로 배우는 한국어"
description: "K-Pop을 통해 자연스럽게 한국어 배우기"
level: 1
```

**Response** (201 Created):
```json
{
  "lectureId": 1,
  "title": "BTS 노래로 배우는 한국어",
  "videoUrl": "/api/videos/lecture_1.mp4",
  "status": "processing",
  "message": "Lecture 생성 중입니다. LLM이 퀴즈를 자동 생성하고 있습니다.",
  "estimatedTimeSeconds": 60,
  "createdAt": "2025-01-20T10:00:00"
}
```

**Processing 완료 후** (WebSocket 또는 Polling):
```json
{
  "lectureId": 1,
  "status": "completed",
  "generatedQuizCount": 5,
  "quizzes": [
    {
      "id": 101,
      "sequence": 1,
      "triggerTimeSec": 45,
      "questionText": "방금 들은 인사말은 무엇인가요?",
      "expectedAnswer": "안녕하세요"
    }
  ],
  "completedAt": "2025-01-20T10:01:30"
}
```

---

### 1.4 Lecture 언어 선택 저장 (우선순위 낮음)

```http
POST /api/users/onboarding/language
```

**설명**: 사용자가 선택한 학습 언어를 저장합니다.

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body**:
```json
{
  "selectedLanguage": "korean",
  "nativeLanguage": "english"
}
```

**언어 옵션**:
- `korean` - 한국어
- `japanese` - 일본어
- `english` - 영어
- `chinese` - 중국어
- `spanish` - 스페인어
- `french` - 프랑스어

**Response** (200 OK):
```json
{
  "userId": 1,
  "selectedLanguage": "korean",
  "nativeLanguage": "english",
  "updatedAt": "2025-01-20T10:30:00"
}
```

**Error Response** (400 Bad Request):
```json
{
  "error": {
    "code": "INVALID_LANGUAGE",
    "message": "지원하지 않는 언어입니다.",
    "supportedLanguages": ["korean", "japanese", "english", "chinese", "spanish", "french"]
  }
}
```

---

### 1.2 실력 레벨 설정

```http
POST /api/users/onboarding/level
```

**설명**: 사용자의 현재 실력 레벨을 저장합니다.

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body**:
```json
{
  "level": "beginner"
}
```

**레벨 옵션**:
- `beginner` - 초급 (처음 시작)
- `intermediate` - 중급 (어느 정도 경험 있음)
- `advanced` - 고급 (유창한 수준)

**Response** (200 OK):
```json
{
  "userId": 1,
  "level": "beginner",
  "recommendedCourses": [
    {
      "id": 1,
      "title": "기초 한국어 인사말",
      "difficulty": "beginner",
      "thumbnailUrl": "https://..."
    },
    {
      "id": 2,
      "title": "일상 회화 기초",
      "difficulty": "beginner",
      "thumbnailUrl": "https://..."
    }
  ],
  "updatedAt": "2025-01-20T10:35:00"
}
```

---

### 1.3 온보딩 완료

```http
POST /api/users/onboarding/complete
```

**설명**: 온보딩 프로세스를 완료하고 사용자 상태를 업데이트합니다.

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body**:
```json
{
  "selectedLanguage": "korean",
  "level": "beginner",
  "preferredTopics": ["kpop", "drama", "daily"]
}
```

**Response** (200 OK):
```json
{
  "userId": 1,
  "onboardingCompleted": true,
  "profile": {
    "selectedLanguage": "korean",
    "level": "beginner",
    "preferredTopics": ["kpop", "drama", "daily"]
  },
  "nextSteps": {
    "message": "환영합니다! 첫 번째 학습을 시작해보세요.",
    "recommendedCourseId": 1,
    "recommendedCourseTitle": "기초 한국어 인사말"
  },
  "completedAt": "2025-01-20T10:40:00"
}
```

---

### 1.4 사용자 프로필 조회

```http
GET /api/users/profile
```

**설명**: 현재 로그인한 사용자의 프로필 정보를 조회합니다.

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "email": "user@gmail.com",
  "nickname": "홍길동",
  "profileImageUrl": "/img/Abocado_Logo.png",
  "provider": "GOOGLE",
  "selectedLanguage": "korean",
  "level": "beginner",
  "preferredTopics": ["kpop", "drama"],
  "onboardingCompleted": true,
  "createdAt": "2025-01-20T10:00:00",
  "lastLoginAt": "2025-01-20T15:00:00"
}
```

---

## 2. 소셜 로그인 API (Phase 1)

### 2.1 Google 로그인 URL 조회

```http
GET /api/auth/oauth2/google/url
```

**설명**: Google OAuth2 로그인 URL을 반환합니다.

**Response** (200 OK):
```json
{
  "authUrl": "https://accounts.google.com/o/oauth2/v2/auth?client_id=...",
  "state": "random-state-value"
}
```

---

### 2.2 Google 로그인 콜백

```http
POST /api/auth/oauth2/google/callback
```

**설명**: Google OAuth2 인증 후 콜백 처리

**Request Body**:
```json
{
  "code": "4/0AfJohXm...",
  "state": "random-state-value"
}
```

**Response** (200 OK):
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "email": "user@gmail.com",
    "nickname": "홍길동",
    "profileImageUrl": "https://lh3.googleusercontent.com/...",
    "provider": "GOOGLE",
    "onboardingCompleted": false,
    "createdAt": "2025-01-20T10:00:00"
  }
}
```

---

### 2.3 Kakao 로그인 (동일한 구조)

```http
GET /api/auth/oauth2/kakao/url
POST /api/auth/oauth2/kakao/callback
```

---

### 2.4 로그아웃

```http
POST /api/auth/logout
```

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "message": "Successfully logged out",
  "loggedOutAt": "2025-01-20T16:00:00"
}
```

---

### 2.5 토큰 갱신

```http
POST /api/auth/refresh
```

**Request Body**:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response** (200 OK):
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

---

## 3. 학습 컨텐츠 API (Phase 2)

### 3.1 추천 학습 컨텐츠 조회

```http
GET /api/courses/recommended
```

**설명**: 사용자의 언어/레벨에 맞는 추천 컨텐츠를 조회합니다.

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Query Parameters**:
- `limit` (integer, optional, default: 10): 조회할 최대 개수

**Response** (200 OK):
```json
{
  "recommended": [
    {
      "id": 1,
      "title": "BTS 노래로 배우는 한국어",
      "description": "K-Pop을 통해 자연스럽게 한국어 배우기",
      "category": "kpop",
      "difficulty": "beginner",
      "thumbnailUrl": "/img/course1.jpg",
      "durationMinutes": 15,
      "lessonsCount": 10,
      "isCompleted": false,
      "progress": 0,
      "tags": ["음악", "초급", "회화"]
    },
    {
      "id": 2,
      "title": "드라마로 배우는 일상 회화",
      "description": "인기 드라마 대사로 배우는 실용 한국어",
      "category": "drama",
      "difficulty": "beginner",
      "thumbnailUrl": "/img/course2.jpg",
      "durationMinutes": 20,
      "lessonsCount": 15,
      "isCompleted": false,
      "progress": 30,
      "tags": ["드라마", "초급", "문화"]
    }
  ],
  "totalCourses": 25
}
```

---

### 3.2 학습 컨텐츠 목록 조회

```http
GET /api/courses
```

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Query Parameters**:
- `category` (string, optional): `kpop`, `drama`, `daily`, `business`
- `difficulty` (string, optional): `beginner`, `intermediate`, `advanced`
- `page` (integer, optional, default: 0): 페이지 번호
- `size` (integer, optional, default: 10): 페이지 크기
- `sort` (string, optional, default: `popular`): `popular`, `recent`, `difficulty`

**Response** (200 OK):
```json
{
  "content": [
    {
      "id": 1,
      "title": "BTS 노래로 배우는 한국어",
      "category": "kpop",
      "difficulty": "beginner",
      "thumbnailUrl": "/img/course1.jpg",
      "durationMinutes": 15,
      "lessonsCount": 10,
      "enrolledCount": 1234,
      "rating": 4.8,
      "isCompleted": false,
      "progress": 0
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 25,
    "totalPages": 3
  }
}
```

---

### 3.3 학습 컨텐츠 상세 조회

```http
GET /api/courses/{courseId}
```

**Parameters**:
- `courseId` (integer, required): 코스 ID

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "title": "BTS 노래로 배우는 한국어",
  "description": "K-Pop을 통해 자연스럽게 한국어를 배워보세요",
  "category": "kpop",
  "difficulty": "beginner",
  "thumbnailUrl": "/img/course1.jpg",
  "videoUrl": "/api/videos/course_1.mp4",
  "durationMinutes": 15,
  "lessonsCount": 10,
  "enrolledCount": 1234,
  "rating": 4.8,
  "instructor": {
    "name": "김선생",
    "bio": "한국어 교육 전문가",
    "profileImage": "/img/instructor1.jpg"
  },
  "lessons": [
    {
      "id": 101,
      "sequence": 1,
      "title": "인사말 배우기",
      "type": "VIDEO_WITH_QUIZ",
      "durationSeconds": 90,
      "isCompleted": false
    },
    {
      "id": 102,
      "sequence": 2,
      "title": "자기소개하기",
      "type": "VOICE_PRACTICE",
      "durationSeconds": 120,
      "isCompleted": false
    }
  ],
  "userProgress": {
    "isEnrolled": false,
    "completedLessons": 0,
    "totalLessons": 10,
    "progressPercentage": 0,
    "lastAccessedAt": null
  },
  "tags": ["음악", "초급", "회화"],
  "createdAt": "2025-01-15T10:00:00",
  "updatedAt": "2025-01-20T09:00:00"
}
```

---

### 3.4 코스 등록 (Enroll)

```http
POST /api/courses/{courseId}/enroll
```

**Parameters**:
- `courseId` (integer, required): 코스 ID

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (201 Created):
```json
{
  "enrollmentId": 456,
  "courseId": 1,
  "userId": 1,
  "enrolledAt": "2025-01-20T10:00:00",
  "message": "코스에 성공적으로 등록되었습니다."
}
```

---

## 4. 학습 진행 API (Phase 2)

### 4.1 레슨 시작

```http
POST /api/lessons/{lessonId}/start
```

**Parameters**:
- `lessonId` (integer, required): 레슨 ID

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "sessionId": 789,
  "lessonId": 101,
  "lesson": {
    "id": 101,
    "title": "인사말 배우기",
    "type": "VIDEO_WITH_QUIZ",
    "videoUrl": "/api/videos/lesson_101.mp4",
    "durationSeconds": 90,
    "components": [
      {
        "id": 1001,
        "sequence": 1,
        "type": "VIDEO_SEGMENT",
        "startTime": 0,
        "endTime": 30,
        "subtitles": "안녕하세요, 여러분!"
      },
      {
        "id": 1002,
        "sequence": 2,
        "type": "QUESTION",
        "triggerTime": 30,
        "question": "방금 들은 인사말은 무엇인가요?",
        "hint": "한국에서 가장 많이 쓰는 인사"
      },
      {
        "id": 1003,
        "sequence": 3,
        "type": "VOICE_ANSWER",
        "expectedAnswer": "안녕하세요",
        "acceptableVariations": ["안녕", "안녕하십니까"],
        "maxAttempts": 3
      }
    ]
  },
  "startedAt": "2025-01-20T10:05:00"
}
```

---

### 4.2 음성 답변 제출

```http
POST /api/lessons/answer
```

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: multipart/form-data
```

**Request Body** (Multipart Form):
```
sessionId: 789
componentId: 1003
audio: (file) answer.webm
```

**Response** (200 OK - 정답):
```json
{
  "answerId": 2001,
  "sessionId": 789,
  "componentId": 1003,
  "isCorrect": true,
  "transcribedText": "안녕하세요",
  "expectedAnswer": "안녕하세요",
  "similarityScore": 0.95,
  "feedback": "완벽합니다! 발음이 정확해요. 👍",
  "attemptNumber": 1,
  "maxAttempts": 3,
  "nextComponentId": 1004,
  "audioUrl": "/api/audio/answer_2001.webm"
}
```

**Response** (200 OK - 오답):
```json
{
  "answerId": 2002,
  "sessionId": 789,
  "componentId": 1003,
  "isCorrect": false,
  "transcribedText": "안녕",
  "expectedAnswer": "안녕하세요",
  "similarityScore": 0.65,
  "feedback": "조금 더 정확하게 '안녕하세요'라고 말해보세요. (2/3 시도)",
  "attemptNumber": 2,
  "maxAttempts": 3,
  "nextComponentId": 1003,
  "hint": "'세요'를 빠뜨리지 마세요!",
  "audioUrl": "/api/audio/answer_2002.webm"
}
```

---

### 4.3 레슨 완료

```http
POST /api/lessons/{lessonId}/complete
```

**Parameters**:
- `lessonId` (integer, required): 레슨 ID

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body**:
```json
{
  "sessionId": 789,
  "score": 90.0,
  "completedComponents": [1001, 1002, 1003, 1004]
}
```

**Response** (200 OK):
```json
{
  "lessonId": 101,
  "isCompleted": true,
  "finalScore": 90.0,
  "totalQuestions": 5,
  "correctAnswers": 4,
  "completedAt": "2025-01-20T10:20:00",
  "reward": {
    "xp": 100,
    "badge": "첫 레슨 완료",
    "badgeImageUrl": "/img/badges/first_lesson.png"
  },
  "nextLesson": {
    "id": 102,
    "title": "자기소개하기",
    "thumbnailUrl": "/img/lesson_102.jpg"
  }
}
```

---

### 4.4 학습 진도 조회

```http
GET /api/progress
```

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "userId": 1,
  "overview": {
    "totalCoursesEnrolled": 5,
    "completedCourses": 1,
    "inProgressCourses": 2,
    "totalLessonsCompleted": 15,
    "averageScore": 85.5,
    "totalXP": 1500,
    "currentLevel": 3,
    "nextLevelXP": 2000
  },
  "recentProgress": [
    {
      "courseId": 1,
      "courseTitle": "BTS 노래로 배우는 한국어",
      "completedLessons": 5,
      "totalLessons": 10,
      "progressPercentage": 50,
      "lastAccessedAt": "2025-01-20T10:20:00"
    }
  ],
  "achievements": [
    {
      "id": 1,
      "name": "첫 레슨 완료",
      "description": "첫 번째 레슨을 완료했습니다",
      "badgeImageUrl": "/img/badges/first_lesson.png",
      "earnedAt": "2025-01-20T10:20:00"
    }
  ]
}
```

---

## 5. 데이터베이스 스키마 (참고)

### users 테이블
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    provider VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    profile_image_url TEXT,
    selected_language VARCHAR(50),
    level VARCHAR(50),
    preferred_topics JSONB,
    onboarding_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP
);
```

### courses 테이블
```sql
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    difficulty VARCHAR(50) NOT NULL,
    thumbnail_url TEXT,
    video_url TEXT,
    duration_minutes INTEGER,
    lessons_count INTEGER DEFAULT 0,
    enrolled_count INTEGER DEFAULT 0,
    rating DECIMAL(3, 2),
    instructor_id BIGINT,
    tags JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### lessons 테이블
```sql
CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    sequence INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    video_url TEXT,
    duration_seconds INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(course_id, sequence)
);
```

### lesson_components 테이블
```sql
CREATE TABLE lesson_components (
    id BIGSERIAL PRIMARY KEY,
    lesson_id BIGINT REFERENCES lessons(id) ON DELETE CASCADE,
    sequence INTEGER NOT NULL,
    type VARCHAR(50) NOT NULL,
    content JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(lesson_id, sequence)
);
```

### user_progress 테이블
```sql
CREATE TABLE user_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    completed_lessons JSONB DEFAULT '[]',
    progress_percentage DECIMAL(5, 2),
    score DECIMAL(5, 2),
    last_accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, course_id)
);
```

### learning_sessions 테이블
```sql
CREATE TABLE learning_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    lesson_id BIGINT REFERENCES lessons(id) ON DELETE CASCADE,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    score DECIMAL(5, 2),
    is_completed BOOLEAN DEFAULT FALSE
);
```

### answers 테이블
```sql
CREATE TABLE answers (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT REFERENCES learning_sessions(id) ON DELETE CASCADE,
    component_id BIGINT REFERENCES lesson_components(id) ON DELETE CASCADE,
    transcribed_text TEXT,
    audio_url TEXT,
    is_correct BOOLEAN,
    similarity_score DECIMAL(5, 4),
    feedback TEXT,
    attempt_number INTEGER DEFAULT 1,
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 6. 에러 코드

| 코드 | HTTP Status | 설명 |
|------|-------------|------|
| `INVALID_TOKEN` | 401 | 유효하지 않은 토큰 |
| `TOKEN_EXPIRED` | 401 | 만료된 토큰 |
| `UNAUTHORIZED` | 401 | 인증 필요 |
| `FORBIDDEN` | 403 | 권한 없음 |
| `USER_NOT_FOUND` | 404 | 사용자를 찾을 수 없음 |
| `COURSE_NOT_FOUND` | 404 | 코스를 찾을 수 없음 |
| `LESSON_NOT_FOUND` | 404 | 레슨을 찾을 수 없음 |
| `INVALID_LANGUAGE` | 400 | 지원하지 않는 언어 |
| `INVALID_LEVEL` | 400 | 잘못된 레벨 |
| `ALREADY_ENROLLED` | 409 | 이미 등록된 코스 |
| `MAX_ATTEMPTS_EXCEEDED` | 400 | 최대 시도 횟수 초과 |
| `AUDIO_PROCESSING_FAILED` | 500 | 음성 처리 실패 |
| `INTERNAL_SERVER_ERROR` | 500 | 서버 내부 오류 |

---

## 7. 구현 체크리스트

### Phase 1 (1주차)
- [ ] Google OAuth2 로그인 구현
- [ ] Kakao OAuth2 로그인 구현
- [ ] JWT 토큰 생성/검증
- [ ] 언어 선택 API
- [ ] 레벨 설정 API
- [ ] 온보딩 완료 API
- [ ] 사용자 프로필 조회 API

### Phase 2 (2주차)
- [ ] 코스 목록 조회 API
- [ ] 코스 상세 조회 API
- [ ] 코스 등록 API
- [ ] 레슨 시작 API
- [ ] 음성 답변 제출 API (Web Speech API 또는 서버 STT)
- [ ] 레슨 완료 API
- [ ] 학습 진도 조회 API

### Phase 3 (3주차)
- [ ] 3단계 면접 세션 API
- [ ] WebSocket 실시간 통신
- [ ] 감정 분석 API 연동 (DeepFace)
- [ ] AI 피드백 생성 (GPT API)

---

## 8. 테스트 시나리오

### 온보딩 플로우 테스트
```bash
# 1. Google 로그인
GET /api/auth/oauth2/google/url
POST /api/auth/oauth2/google/callback

# 2. 언어 선택
POST /api/users/onboarding/language
{
  "selectedLanguage": "korean",
  "nativeLanguage": "english"
}

# 3. 레벨 설정
POST /api/users/onboarding/level
{
  "level": "beginner"
}

# 4. 온보딩 완료
POST /api/users/onboarding/complete
{
  "selectedLanguage": "korean",
  "level": "beginner",
  "preferredTopics": ["kpop", "drama"]
}

# 5. 추천 코스 조회
GET /api/courses/recommended
```

### 학습 플로우 테스트
```bash
# 1. 코스 등록
POST /api/courses/1/enroll

# 2. 레슨 시작
POST /api/lessons/101/start

# 3. 음성 답변 제출
POST /api/lessons/answer
(multipart form data)

# 4. 레슨 완료
POST /api/lessons/101/complete

# 5. 진도 확인
GET /api/progress
```

---

## 9. 다음 단계

1. **백엔드 스켈레톤 코드 생성**: Spring Boot 프로젝트 구조 생성
2. **데이터베이스 마이그레이션**: PostgreSQL 스키마 생성
3. **OAuth2 통합**: Google/Kakao 로그인 구현
4. **API 엔드포인트 구현**: Controller, Service, Repository 레이어
5. **프론트엔드 API 연동**: Axios 클라이언트 설정
6. **테스트**: Postman/Swagger로 API 테스트
7. **배포**: Docker Compose로 로컬 환경 구축

---

**작성일**: 2025-01-20
**최종 업데이트**: 2025-01-20
