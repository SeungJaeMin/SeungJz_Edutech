# API 구현 명세서 V2 - 수정된 요구사항

## 개요

- **Base URL**: `http://localhost:8080/api`
- **Frontend**: React (Vite)
- **Authentication**: JWT Bearer Token
- **Content-Type**: `application/json`

---

## 구현 우선순위

### 🔴 Phase 1: 필수 (MVP) - 1주차
1. Lecture 조회 API
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

## 1. Lecture API (Phase 1)

### 1.1 Lecture 목록 조회

```http
GET /api/lectures
```

**Query Parameters**:
- `level` (integer, optional): 1, 2 (1-2단계 Lecture)
- `page` (integer, optional, default: 0)
- `size` (integer, optional, default: 10)

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
      "isCompleted": false
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 25
  }
}
```

---

### 1.2 Lecture 상세 조회

```http
GET /api/lectures/{lectureId}
```

**설명**: Lecture의 모든 정보를 JSON 형식으로 가져옵니다 (비디오 URL + 퀴즈 전체).

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
    }
  ]
}
```

---

### 1.3 Lecture 업로드 및 자동 생성 (LLM)

```http
POST /api/lectures/upload
```

**설명**: MP4 파일을 업로드하면 LLM이 자동으로 Lecture와 퀴즈를 생성합니다.

**Request** (Multipart Form):
```
video: (file) lecture_video.mp4
title: "BTS 노래로 배우는 한국어"
description: "K-Pop을 통해..."
level: 1
```

**Response** (201 Created):
```json
{
  "lectureId": 1,
  "status": "processing",
  "message": "LLM이 비디오를 분석하고 퀴즈를 생성 중입니다...",
  "estimatedTimeSeconds": 60
}
```

**LLM 처리 단계**:
1. MP4 비디오 → 음성 추출 (STT)
2. 텍스트 분석 → 주요 키워드/문장 추출
3. 퀴즈 자동 생성 (타임라인 포함)
4. Lecture 저장 완료

---

## 2. 1-2단계 학습 진행 API (Phase 1)

### 학습 시나리오
```
1. startLecture → Lecture 시작
2. MP4 재생 → 특정 시점(triggerTimeSec)에 일시정지
3. 퀴즈 제시 → 사용자 음성 답변
4. submitAnswer → 정답/오답 판정
5. 피드백 표시 → MP4 재생 재개
6. 모든 퀴즈 완료 → completeLecture
7. Result 화면 → "확인" 클릭 → Lecture 선택 화면
```

---

### 2.1 Lecture 시작

```http
POST /api/lectures/{lectureId}/start
```

**Response** (200 OK):
```json
{
  "sessionId": "session_12345",
  "lectureId": 1,
  "startedAt": "2025-01-20T10:00:00",
  "currentQuizSequence": 1,
  "nextQuizTriggerSec": 45
}
```

---

### 2.2 음성 답변 제출

```http
POST /api/lectures/answer
```

**Request** (Multipart Form):
```
sessionId: "session_12345"
quizId: 101
audio: (file) answer.webm
```

**Response** (200 OK - 정답):
```json
{
  "answerId": 5001,
  "isCorrect": true,
  "transcribedText": "안녕하세요",
  "expectedAnswer": "안녕하세요",
  "similarityScore": 0.95,
  "feedback": "완벽합니다! 👍",
  "attemptNumber": 1,
  "nextQuizSequence": 2,
  "nextQuizTriggerSec": 120
}
```

**Response** (200 OK - 오답):
```json
{
  "answerId": 5002,
  "isCorrect": false,
  "transcribedText": "안녕",
  "expectedAnswer": "안녕하세요",
  "similarityScore": 0.65,
  "feedback": "조금 더 정확하게 말해보세요. (시도: 2/3)",
  "attemptNumber": 2,
  "hint": "'세요'를 빠뜨리지 마세요!",
  "nextQuizSequence": 1,
  "remainingAttempts": 1
}
```

---

### 2.3 Lecture 완료

```http
POST /api/lectures/{lectureId}/complete
```

**Request**:
```json
{
  "sessionId": "session_12345",
  "totalQuizzes": 5,
  "correctAnswers": 4
}
```

**Response** (200 OK):
```json
{
  "sessionId": "session_12345",
  "lectureId": 1,
  "isCompleted": true,
  "finalScore": 80.0,
  "totalQuizzes": 5,
  "correctAnswers": 4,
  "completedAt": "2025-01-20T10:15:00",
  "message": "축하합니다! Lecture를 완료했습니다.",
  "reward": {
    "xp": 100,
    "badge": "첫 레슨 완료"
  }
}
```

---

## 3. 학습 기록 조회 API (Phase 3)

### 3.1 완료한 Lecture 목록 (1-2단계)

```http
GET /api/progress/lectures
```

**Query Parameters**:
- `level` (integer, optional): 1, 2

**Response** (200 OK):
```json
{
  "completedLectures": [
    {
      "lectureId": 1,
      "title": "BTS 노래로 배우는 한국어",
      "level": 1,
      "completedAt": "2025-01-20T10:15:00",
      "finalScore": 80.0,
      "correctAnswers": 4,
      "totalQuizzes": 5
    }
  ],
  "totalCompleted": 10,
  "averageScore": 85.5
}
```

---

### 3.2 3단계 면접 결과 목록

```http
GET /api/progress/interviews
```

**Response** (200 OK):
```json
{
  "interviews": [
    {
      "sessionId": "interview_789",
      "title": "3분 말하기 연습",
      "completedAt": "2025-01-20T16:00:00",
      "durationSeconds": 180,
      "overallScore": 75.5,
      "videoUrl": "/api/videos/interview_789.mp4",
      "waveformUrl": "/api/waveforms/interview_789.json",
      "feedbackCount": 5
    }
  ]
}
```

---

## 4. 3단계 실시간 면접 API (Phase 2)

### 4.1 면접 세션 시작

```http
POST /api/interviews/start
```

**Request**:
```json
{
  "prompt": "마케팅 인턴 면접 준비",
  "durationSeconds": 180
}
```

**Response** (200 OK):
```json
{
  "sessionId": "interview_789",
  "status": "recording",
  "durationSeconds": 180,
  "startedAt": "2025-01-20T16:00:00",
  "initialQuestion": {
    "questionId": 1001,
    "questionText": "자기소개를 해주세요",
    "ttsAudioUrl": "/api/tts/question_1001.mp3"
  }
}
```

---

### 4.2 실시간 비디오 프레임 전송 (감정 분석)

```http
POST /api/interviews/{sessionId}/frame
```

**Request** (Multipart Form):
```
frame: (file) frame_001.jpg
timelineSec: 5
```

**Response** (200 OK):
```json
{
  "timelineSec": 5,
  "emotion": {
    "dominantEmotion": "neutral",
    "confidence": 0.85,
    "emotionScores": {
      "happy": 0.2,
      "neutral": 0.7,
      "anxious": 0.1
    },
    "gazeDirection": "camera",
    "smileIntensity": 0.3
  }
}
```

---

### 4.3 실시간 음성 답변 제출 및 질문 생성

```http
POST /api/interviews/{sessionId}/answer
```

**Request** (Multipart Form):
```
audio: (file) answer_001.webm
questionId: 1001
timelineSec: 10
```

**Response** (200 OK):
```json
{
  "qaPairId": 2001,
  "questionId": 1001,
  "transcribedText": "안녕하세요. 저는 마케팅에 관심이 많은 김철수입니다...",
  "answerDurationSec": 25,
  "analysisResult": {
    "hasKeywords": true,
    "missingKeywords": [],
    "isCoherent": true,
    "confidence": 0.9
  },
  "nextQuestion": {
    "questionId": 1002,
    "questionText": "팀 프로젝트 경험을 말씀해주세요",
    "ttsAudioUrl": "/api/tts/question_1002.mp3",
    "generatedAt": "2025-01-20T16:00:35"
  }
}
```

---

### 4.4 TTS 음성 합성

```http
POST /api/tts/synthesize
```

**Request**:
```json
{
  "text": "팀 프로젝트 경험을 말씀해주세요",
  "voice": "ko-KR-Neural2-A",
  "speed": 1.0
}
```

**Response** (200 OK):
```json
{
  "audioUrl": "/api/tts/output_12345.mp3",
  "durationSeconds": 3.5,
  "generatedAt": "2025-01-20T16:00:35"
}
```

---

### 4.5 면접 세션 종료

```http
POST /api/interviews/{sessionId}/complete
```

**Request**:
```json
{
  "sessionId": "interview_789"
}
```

**Response** (200 OK):
```json
{
  "sessionId": "interview_789",
  "status": "completed",
  "completedAt": "2025-01-20T16:03:00",
  "videoRecordingUrl": "/api/videos/interview_789.mp4",
  "totalQuestions": 8,
  "totalDurationSec": 180,
  "message": "면접이 완료되었습니다. 결과를 분석 중입니다..."
}
```

---

## 5. 결과 분석 및 피드백 API (Phase 3)

### 5.1 LLM 기반 결과 분석

```http
POST /api/interviews/{sessionId}/analyze
```

**설명**: 녹화된 면접 데이터를 LLM에 전달하여 종합 분석 리포트를 생성합니다.

**Request**:
```json
{
  "sessionId": "interview_789",
  "includeEmotionAnalysis": true,
  "includeTranscript": true
}
```

**Response** (200 OK):
```json
{
  "sessionId": "interview_789",
  "overallScore": 75.5,
  "analysisReport": {
    "summary": "전반적으로 자신감 있는 답변이었으나, 일부 키워드가 누락되었습니다.",
    "strengths": [
      "명확한 발음",
      "적절한 시선 처리 (65% 카메라 응시)",
      "자연스러운 표정"
    ],
    "weaknesses": [
      "팀워크 관련 키워드 누락",
      "답변 길이 부족 (평균 20초)"
    ],
    "recommendations": [
      "구체적인 사례를 더 추가하세요",
      "STAR 기법을 활용하세요"
    ]
  },
  "emotionSummary": {
    "avgConfidence": 0.75,
    "dominantEmotion": "neutral",
    "emotionDistribution": {
      "happy": 20,
      "neutral": 60,
      "anxious": 15,
      "confused": 5
    }
  }
}
```

---

### 5.2 파형(Waveform) 데이터 조회

```http
GET /api/interviews/{sessionId}/waveform
```

**설명**: 음성 파형 데이터를 JSON 형식으로 반환하여 프론트엔드에서 시각화합니다.

**Response** (200 OK):
```json
{
  "sessionId": "interview_789",
  "durationSeconds": 180,
  "sampleRate": 44100,
  "waveformData": [
    {
      "timeSec": 0.0,
      "amplitude": 0.15
    },
    {
      "timeSec": 0.1,
      "amplitude": 0.25
    }
  ],
  "peakPoints": [
    {
      "timeSec": 15.5,
      "amplitude": 0.95,
      "label": "큰 소리"
    }
  ]
}
```

---

### 5.3 타임라인 피드백 핀 조회

```http
GET /api/interviews/{sessionId}/feedback-pins
```

**설명**: 타임라인에 표시할 피드백 핀 목록을 반환합니다.

**Response** (200 OK):
```json
{
  "sessionId": "interview_789",
  "pins": [
    {
      "pinId": 3001,
      "timelineSec": 45,
      "type": "missing_keyword",
      "severity": "high",
      "title": "핵심 키워드 누락",
      "description": "'팀워크'와 '협업' 키워드가 없습니다.",
      "suggestion": "팀 프로젝트 경험 시 구체적 협업 사례를 포함하세요.",
      "videoTimestamp": 45
    },
    {
      "pinId": 3002,
      "timelineSec": 120,
      "type": "poor_gaze",
      "severity": "medium",
      "title": "시선 처리 개선",
      "description": "카메라를 보지 않고 아래를 보고 있습니다.",
      "suggestion": "면접관과 아이컨택을 유지하세요.",
      "videoTimestamp": 120
    }
  ]
}
```

---

### 5.4 타임라인 핀 클릭 시 비디오 이동

**Frontend 동작**:
```javascript
// 핀 클릭 시
const handlePinClick = (pin) => {
  videoPlayer.currentTime = pin.videoTimestamp;
  videoPlayer.play();
};
```

**Backend**: 별도 API 불필요 (프론트엔드에서 처리)

---

## 6. 데이터베이스 스키마

### lectures 테이블
```sql
CREATE TABLE lectures (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    level INTEGER NOT NULL,
    video_url TEXT NOT NULL,
    thumbnail_url TEXT,
    duration_seconds INTEGER,
    quiz_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### quizzes 테이블
```sql
CREATE TABLE quizzes (
    id BIGSERIAL PRIMARY KEY,
    lecture_id BIGINT REFERENCES lectures(id) ON DELETE CASCADE,
    sequence INTEGER NOT NULL,
    trigger_time_sec INTEGER NOT NULL,
    question_text TEXT NOT NULL,
    expected_answer TEXT NOT NULL,
    acceptable_answers JSONB DEFAULT '[]',
    hint_text TEXT,
    correct_feedback TEXT,
    incorrect_feedback TEXT,
    max_attempts INTEGER DEFAULT 3,
    UNIQUE(lecture_id, sequence)
);
```

### learning_sessions 테이블
```sql
CREATE TABLE learning_sessions (
    id VARCHAR(255) PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    lecture_id BIGINT REFERENCES lectures(id),
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    is_completed BOOLEAN DEFAULT FALSE,
    final_score DECIMAL(5, 2)
);
```

### answers 테이블
```sql
CREATE TABLE answers (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) REFERENCES learning_sessions(id),
    quiz_id BIGINT REFERENCES quizzes(id),
    transcribed_text TEXT,
    audio_url TEXT,
    is_correct BOOLEAN,
    similarity_score DECIMAL(5, 4),
    feedback TEXT,
    attempt_number INTEGER DEFAULT 1,
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### interview_sessions 테이블
```sql
CREATE TABLE interview_sessions (
    id VARCHAR(255) PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    prompt TEXT,
    duration_seconds INTEGER DEFAULT 180,
    video_url TEXT,
    waveform_url TEXT,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    overall_score DECIMAL(5, 2),
    status VARCHAR(50) DEFAULT 'recording'
);
```

### interview_qa_pairs 테이블
```sql
CREATE TABLE interview_qa_pairs (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) REFERENCES interview_sessions(id),
    question_id BIGINT,
    question_text TEXT NOT NULL,
    transcribed_answer TEXT,
    audio_url TEXT,
    tts_audio_url TEXT,
    timeline_sec INTEGER,
    duration_sec INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### emotion_analysis 테이블
```sql
CREATE TABLE emotion_analysis (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) REFERENCES interview_sessions(id),
    timeline_sec INTEGER NOT NULL,
    dominant_emotion VARCHAR(50),
    confidence DECIMAL(5, 4),
    emotion_scores JSONB,
    gaze_direction VARCHAR(50),
    smile_intensity DECIMAL(5, 4),
    analyzed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### feedback_pins 테이블
```sql
CREATE TABLE feedback_pins (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) REFERENCES interview_sessions(id),
    timeline_sec INTEGER NOT NULL,
    type VARCHAR(50) NOT NULL,
    severity VARCHAR(20),
    title VARCHAR(255),
    description TEXT,
    suggestion TEXT,
    video_timestamp INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 7. 구현 체크리스트

### Phase 1: 필수 (MVP)
- [ ] Lecture 목록/상세 조회 API
- [ ] Lecture 업로드 API
- [ ] LLM 퀴즈 자동 생성
- [ ] startLecture API
- [ ] submitAnswer API (음성 인식 + 정답 판정)
- [ ] completeLecture API
- [ ] 비디오 스트리밍 API

### Phase 2: 3단계 면접
- [ ] 면접 세션 시작/종료 API
- [ ] 실시간 비디오 프레임 감정 분석
- [ ] 실시간 음성 답변 STT
- [ ] LLM 실시간 질문 생성
- [ ] TTS 음성 합성
- [ ] WebSocket 실시간 통신

### Phase 3: 결과 분석
- [ ] LLM 결과 분석 리포트
- [ ] 파형 데이터 생성 API
- [ ] 타임라인 피드백 핀 생성
- [ ] 학습 기록 조회 API (1-2단계)
- [ ] 면접 결과 조회 API (3단계)

---

## 8. LLM 통합 포인트

### 8.1 Lecture 퀴즈 자동 생성
```
Input: MP4 비디오 파일
Process:
1. STT → 텍스트 추출
2. LLM Prompt:
   "다음 한국어 학습 비디오 스크립트에서 5개의 퀴즈를 생성하세요.
    각 퀴즈는 타임스탬프, 질문, 예상 답변을 포함해야 합니다."
3. Output: JSON 형식 퀴즈 목록
```

### 8.2 실시간 질문 생성
```
Input: 사용자의 이전 답변 텍스트
Process:
1. LLM Prompt:
   "다음 답변을 분석하고 연관된 심화 질문을 생성하세요: {answer}"
2. Output: 다음 질문 텍스트
```

### 8.3 면접 결과 분석
```
Input: 전체 QA 기록 + 감정 데이터
Process:
1. LLM Prompt:
   "다음 면접 데이터를 분석하고 종합 피드백을 생성하세요..."
2. Output: 분석 리포트 (강점, 약점, 추천사항)
```

---

**작성일**: 2025-01-20
**최종 업데이트**: 2025-01-20
