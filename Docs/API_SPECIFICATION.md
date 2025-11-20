# API 명세서 - SeungJz 해커톤 애플리케이션

## 개요

- **Base URL**: `http://localhost:8080/api`
- **Authentication**: JWT Bearer Token (소셜 로그인 후 발급)
- **Content-Type**: `application/json`

---

## 개발 우선순위별 API 분류

### Phase 1: 인증 및 기본 구조 (최우선)
- 소셜 로그인
- 사용자 정보 조회

### Phase 2: 1-2단계 학습 (핵심 기능)
- 학습 컨텐츠 조회
- 학습 진행
- 답변 제출 및 검증

### Phase 3: 3단계 면접 (고급 기능)
- 면접 세션 생성
- 실시간 질문/답변
- 감정 분석 및 피드백

---

## 1. 인증 API (Phase 1)

### 1.1 Google 소셜 로그인

```http
GET /api/auth/oauth2/authorization/google
```

**Description**: Google OAuth2 인증 시작

**Response**: Redirect to Google OAuth consent screen

---

### 1.2 Google 로그인 콜백

```http
GET /api/auth/login/oauth2/code/google?code={code}&state={state}
```

**Parameters**:
- `code` (string, required): OAuth2 authorization code
- `state` (string, required): CSRF 방지용 state

**Response** (200 OK):
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "email": "user@gmail.com",
    "nickname": "홍길동",
    "profileImageUrl": "https://...",
    "provider": "GOOGLE",
    "createdAt": "2025-01-20T10:30:00"
  }
}
```

---

### 1.3 Kakao 소셜 로그인

```http
GET /api/auth/oauth2/authorization/kakao
```

**Description**: Kakao OAuth2 인증 시작

---

### 1.4 Kakao 로그인 콜백

```http
GET /api/auth/login/oauth2/code/kakao?code={code}
```

**Response**: Same as Google login callback

---

### 1.5 현재 사용자 정보 조회

```http
GET /api/auth/me
```

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
  "profileImageUrl": "https://...",
  "provider": "GOOGLE",
  "preferredLanguage": "ko",
  "createdAt": "2025-01-20T10:30:00",
  "lastLoginAt": "2025-01-20T15:30:00"
}
```

---

### 1.6 로그아웃

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
  "message": "Successfully logged out"
}
```

---

## 2. 학습 컨텐츠 API (Phase 2)

### 2.1 학습 컨텐츠 목록 조회

```http
GET /api/lectures?level={level}&page={page}&size={size}
```

**Parameters**:
- `level` (integer, optional): 1, 2, 3 (미지정 시 전체)
- `page` (integer, optional, default: 0): 페이지 번호
- `size` (integer, optional, default: 10): 페이지 크기

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "content": [
    {
      "id": 1,
      "title": "BTS 노래로 배우는 기초 한국어",
      "description": "K-Pop을 통해 자연스럽게 한국어를 배워보세요",
      "level": 1,
      "category": "kpop",
      "difficulty": "beginner",
      "thumbnailUrl": "https://s3.../thumbnail.jpg",
      "durationSec": 180,
      "viewCount": 1234,
      "isCompleted": false,
      "userProgress": {
        "completedComponents": 3,
        "totalComponents": 10,
        "score": 75.5
      }
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

### 2.2 학습 컨텐츠 상세 조회

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
  "title": "BTS 노래로 배우는 기초 한국어",
  "description": "K-Pop을 통해 자연스럽게 한국어를 배워보세요",
  "level": 1,
  "category": "kpop",
  "difficulty": "beginner",
  "thumbnailUrl": "https://s3.../thumbnail.jpg",
  "videoUrl": "https://s3.../video.mp4",
  "durationSec": 180,
  "viewCount": 1234,
  "createdAt": "2025-01-15T10:00:00",
  "components": [
    {
      "id": 101,
      "sequence": 0,
      "type": "VIDEO_SEGMENT",
      "triggerTimeSec": null,
      "content": {
        "startTime": 0,
        "endTime": 30,
        "subtitles": "안녕하세요, 여러분!"
      }
    },
    {
      "id": 102,
      "sequence": 1,
      "type": "QUESTION",
      "triggerTimeSec": 30,
      "content": {
        "questionText": "방금 들은 인사말은 무엇인가요?",
        "hintText": "한국에서 가장 많이 쓰는 인사"
      }
    },
    {
      "id": 103,
      "sequence": 2,
      "type": "VOICE_ANSWER",
      "triggerTimeSec": null,
      "content": {
        "expectedAnswer": "안녕하세요",
        "acceptableVariations": ["안녕", "안녕하십니까"],
        "similarityThreshold": 0.8,
        "maxAttempts": 3
      }
    }
  ],
  "userProgress": {
    "completedComponents": [101],
    "currentComponentId": 102,
    "score": 50.0,
    "isCompleted": false,
    "lastAccessedAt": "2025-01-20T14:30:00"
  }
}
```

---

### 2.3 학습 시작

```http
POST /api/learning/start
```

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body**:
```json
{
  "lectureId": 1
}
```

**Response** (200 OK):
```json
{
  "progressId": 123,
  "lectureId": 1,
  "currentComponentId": 101,
  "startedAt": "2025-01-20T15:00:00"
}
```

---

### 2.4 답변 제출

```http
POST /api/learning/submit-answer
```

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: multipart/form-data
```

**Request Body** (Form Data):
```
progressId: 123
componentId: 103
audio: (file) user_audio.webm
```

**Response** (200 OK):
```json
{
  "answerId": 456,
  "isCorrect": true,
  "userAnswerText": "안녕하세요",
  "expectedAnswer": "안녕하세요",
  "similarityScore": 0.95,
  "feedbackText": "완벽합니다! 발음이 정확해요.",
  "attemptCount": 1,
  "nextComponentId": 104
}
```

**Response** (200 OK - 오답):
```json
{
  "answerId": 457,
  "isCorrect": false,
  "userAnswerText": "안녕",
  "expectedAnswer": "안녕하세요",
  "similarityScore": 0.65,
  "feedbackText": "조금 더 정확하게 '안녕하세요'라고 말해보세요. (2/3 시도)",
  "attemptCount": 2,
  "nextComponentId": 103
}
```

---

### 2.5 학습 진도 저장

```http
PUT /api/learning/progress/{progressId}
```

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body**:
```json
{
  "currentComponentId": 105,
  "completedComponents": [101, 102, 103, 104]
}
```

**Response** (200 OK):
```json
{
  "progressId": 123,
  "completedComponents": [101, 102, 103, 104],
  "currentComponentId": 105,
  "score": 85.5,
  "lastAccessedAt": "2025-01-20T15:30:00"
}
```

---

### 2.6 학습 완료

```http
POST /api/learning/complete
```

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body**:
```json
{
  "progressId": 123
}
```

**Response** (200 OK):
```json
{
  "progressId": 123,
  "isCompleted": true,
  "finalScore": 90.0,
  "totalQuestions": 10,
  "correctAnswers": 9,
  "completedAt": "2025-01-20T16:00:00",
  "congratulationsMessage": "축하합니다! 1단계를 완료했습니다."
}
```

---

## 3. 학습 이력 API (Phase 2)

### 3.1 내 학습 진도 조회

```http
GET /api/progress/me
```

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "totalLectures": 20,
  "completedLectures": 5,
  "inProgressLectures": 2,
  "averageScore": 85.5,
  "progress": [
    {
      "progressId": 123,
      "lecture": {
        "id": 1,
        "title": "BTS 노래로 배우는 기초 한국어",
        "level": 1,
        "thumbnailUrl": "https://..."
      },
      "completedComponents": 10,
      "totalComponents": 10,
      "score": 90.0,
      "isCompleted": true,
      "lastAccessedAt": "2025-01-20T16:00:00"
    }
  ]
}
```

---

### 3.2 특정 학습 답변 이력 조회

```http
GET /api/progress/{progressId}/answers
```

**Parameters**:
- `progressId` (integer, required): Progress ID

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "progressId": 123,
  "answers": [
    {
      "answerId": 456,
      "componentId": 103,
      "userAnswerText": "안녕하세요",
      "audioUrl": "https://s3.../answer_456.webm",
      "isCorrect": true,
      "feedbackText": "완벽합니다!",
      "similarityScore": 0.95,
      "attemptCount": 1,
      "answeredAt": "2025-01-20T15:10:00"
    }
  ]
}
```

---

## 4. 3단계 면접 API (Phase 3)

### 4.1 면접 세션 시작

```http
POST /api/interview/start
```

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: application/json
```

**Request Body**:
```json
{
  "promptText": "마케팅 인턴 면접을 준비하고 있습니다. 팀 프로젝트 경험에 대해 이야기하고 싶습니다."
}
```

**Response** (200 OK):
```json
{
  "sessionId": 789,
  "status": "in_progress",
  "durationSec": 180,
  "startedAt": "2025-01-20T16:00:00",
  "firstQuestion": {
    "questionId": 1001,
    "questionText": "자기소개를 해주세요.",
    "questionType": "initial",
    "askedAt": "2025-01-20T16:00:00"
  }
}
```

---

### 4.2 면접 답변 제출 (실시간)

```http
POST /api/interview/{sessionId}/answer
```

**Headers**:
```
Authorization: Bearer {accessToken}
Content-Type: multipart/form-data
```

**Request Body** (Form Data):
```
questionId: 1001
audio: (file) answer_audio.webm
videoFrame: (file) frame_001.jpg
timelineSec: 5
```

**Response** (200 OK):
```json
{
  "qaPairId": 2001,
  "questionId": 1001,
  "transcript": "안녕하세요. 저는 마케팅에 관심이 많은 김철수입니다...",
  "answerDurationSec": 25,
  "emotion": {
    "dominantEmotion": "neutral",
    "confidence": 0.85,
    "gazeDirection": "camera",
    "smileIntensity": 0.3
  },
  "nextQuestion": {
    "questionId": 1002,
    "questionText": "팀 프로젝트에서 어떤 역할을 맡았나요?",
    "questionType": "follow_up",
    "askedAt": "2025-01-20T16:00:30"
  }
}
```

---

### 4.3 면접 세션 종료

```http
POST /api/interview/{sessionId}/complete
```

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "sessionId": 789,
  "status": "completed",
  "completedAt": "2025-01-20T16:03:00",
  "overallScore": 75.5,
  "totalQuestions": 8,
  "videoRecordingUrl": "https://s3.../session_789.mp4",
  "message": "면접 세션이 완료되었습니다. 피드백을 확인하세요."
}
```

---

### 4.4 면접 피드백 조회

```http
GET /api/interview/{sessionId}/feedback
```

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "sessionId": 789,
  "overallScore": 75.5,
  "emotionSummary": {
    "avgConfidence": 0.75,
    "avgSmileIntensity": 0.4,
    "gazeCameraPercent": 65,
    "emotionDistribution": {
      "happy": 20,
      "neutral": 60,
      "anxious": 15,
      "confused": 5
    }
  },
  "feedback": [
    {
      "feedbackId": 3001,
      "type": "missing_keyword",
      "timelineSec": 45,
      "severity": "high",
      "title": "핵심 키워드 누락",
      "description": "'팀워크'와 '협업'이라는 키워드를 언급하지 않았습니다.",
      "suggestion": "팀 프로젝트 경험을 이야기할 때는 구체적인 협업 사례를 포함하세요.",
      "relatedQuestionText": "팀 프로젝트에서 어떤 역할을 맡았나요?"
    },
    {
      "feedbackId": 3002,
      "type": "poor_gaze",
      "timelineSec": 120,
      "severity": "medium",
      "title": "시선 처리 개선 필요",
      "description": "답변 중 35%의 시간 동안 카메라를 보지 않았습니다.",
      "suggestion": "자신감을 보이기 위해 면접관(카메라)과 아이컨택을 유지하세요.",
      "relatedQuestionText": null
    }
  ],
  "timeline": [
    {
      "timeSec": 0,
      "emotion": "neutral",
      "confidence": 0.9,
      "gazeDirection": "camera"
    },
    {
      "timeSec": 30,
      "emotion": "anxious",
      "confidence": 0.7,
      "gazeDirection": "down"
    }
  ]
}
```

---

### 4.5 내 면접 세션 목록

```http
GET /api/interview/sessions
```

**Headers**:
```
Authorization: Bearer {accessToken}
```

**Response** (200 OK):
```json
{
  "sessions": [
    {
      "sessionId": 789,
      "promptText": "마케팅 인턴 면접...",
      "status": "completed",
      "overallScore": 75.5,
      "totalQuestions": 8,
      "feedbackCount": 5,
      "startedAt": "2025-01-20T16:00:00",
      "completedAt": "2025-01-20T16:03:00"
    }
  ]
}
```

---

## 5. 비디오 스트리밍 API (Phase 2)

### 5.1 비디오 스트리밍

```http
GET /api/videos/{lectureId}/stream
```

**Headers**:
```
Authorization: Bearer {accessToken}
Range: bytes=0-1023 (optional)
```

**Response** (206 Partial Content):
```
Content-Type: video/mp4
Content-Range: bytes 0-1023/102400
Accept-Ranges: bytes

(binary video data)
```

---

## 공통 응답 형식

### 성공 응답
```json
{
  "data": { ... },
  "message": "Success",
  "timestamp": "2025-01-20T16:00:00"
}
```

### 에러 응답
```json
{
  "error": {
    "code": "LECTURE_NOT_FOUND",
    "message": "강의를 찾을 수 없습니다.",
    "details": "Lecture ID: 999"
  },
  "timestamp": "2025-01-20T16:00:00"
}
```

### HTTP 상태 코드
- `200 OK`: 성공
- `201 Created`: 리소스 생성 성공
- `400 Bad Request`: 잘못된 요청
- `401 Unauthorized`: 인증 실패
- `403 Forbidden`: 권한 없음
- `404 Not Found`: 리소스 없음
- `500 Internal Server Error`: 서버 오류

---

## 인증 방식

모든 보호된 API는 JWT Bearer Token 인증 필요:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

토큰 만료 시:
```json
{
  "error": {
    "code": "TOKEN_EXPIRED",
    "message": "토큰이 만료되었습니다. 다시 로그인하세요."
  }
}
```

---

## WebSocket API (Phase 3 - 실시간 면접)

### 연결

```
ws://localhost:8080/ws/interview/{sessionId}?token={accessToken}
```

### 메시지 형식

**클라이언트 → 서버 (비디오 프레임 전송)**:
```json
{
  "type": "VIDEO_FRAME",
  "sessionId": 789,
  "timelineSec": 5,
  "frameData": "base64_encoded_image..."
}
```

**서버 → 클라이언트 (실시간 감정 분석)**:
```json
{
  "type": "EMOTION_ANALYSIS",
  "timelineSec": 5,
  "emotion": "neutral",
  "confidence": 0.85,
  "gazeDirection": "camera",
  "smileIntensity": 0.3
}
```

**서버 → 클라이언트 (새 질문)**:
```json
{
  "type": "NEW_QUESTION",
  "questionId": 1002,
  "questionText": "팀 프로젝트에서 어떤 역할을 맡았나요?",
  "questionType": "follow_up"
}
```
