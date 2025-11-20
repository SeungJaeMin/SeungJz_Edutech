# SeungJz 애플리케이션 아키텍처

## 시스템 개요

**nawbio-api/nawbio-web 구조를 참조한 3-Tier 아키텍처 기반 한국어 학습 플랫폼**

```
┌─────────────────────────────────────────────────────┐
│               Frontend (React + TypeScript)          │
│   - Mobile-First Responsive Design                  │
│   - React Router v7                                  │
│   - styled-components (CSS-in-JS)                   │
│   - i18next (다국어 지원)                             │
│   - Axios API Client                                 │
└────────────────────┬────────────────────────────────┘
                     │ HTTPS/REST API
                     │ /api/v1/*
┌────────────────────▼────────────────────────────────┐
│          Backend (Spring Boot 3.2)                  │
│   - RESTful API                                     │
│   - JWT Authentication (Header-based)               │
│   - Spring Security                                 │
│   - Spring Data JPA                                 │
│   - Springdoc OpenAPI (Swagger)                     │
│   - Video Streaming (AWS S3 + CloudFront)          │
└────────────────────┬────────────────────────────────┘
                     │ JDBC
┌────────────────────▼────────────────────────────────┐
│              Database (PostgreSQL 14)                │
│   - 사용자 관리 (users)                              │
│   - 학습 컨텐츠 (lectures, components)               │
│   - 학습 진도 (user_progress, answer_history)        │
│   - 면접 세션 (interview_sessions, qa_pairs)         │
│   - 감정 분석 (emotion_analysis)                     │
└─────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────┐
│          ML Services (Python Flask/FastAPI)         │
│   - STT Service: KoSpeech (한국어 특화 STT)         │
│   - Emotion Service: DeepFace (오픈소스)            │
│   - Feedback Service: GPT-4 (질문 생성 및 분석)     │
└─────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────┐
│              Infrastructure (Docker)                 │
│   - docker-compose.yml (로컬 개발)                  │
│   - docker-compose.prod.yml (프로덕션)              │
│   - AWS S3 (비디오 저장소)                          │
│   - CloudFront CDN (비디오 스트리밍)                │
└─────────────────────────────────────────────────────┘
```

## 계층별 상세 설계

### 1. Frontend Layer (React + TypeScript)

#### 주요 책임
- Mobile-First Responsive Design
- JWT 기반 인증 관리
- 비디오 재생 및 동기화
- 음성인식 (Web Speech API / Whisper)
- 실시간 감정 분석 표시
- 학습 진도 시각화

#### nawbio-web 기반 프로젝트 구조

```
frontend/
├── public/
│   ├── index.html
│   ├── favicon.ico
│   └── assets/
│       ├── images/
│       └── videos/
│
├── src/
│   ├── api/                       # API 클라이언트 (nawbio 참조)
│   │   ├── client.ts              # Axios 인스턴스 + Interceptor
│   │   ├── auth.ts                # POST /api/v1/auth/login, /register
│   │   ├── lecture.ts             # GET /api/v1/lectures
│   │   ├── learning.ts            # POST /api/v1/learning/submit
│   │   ├── interview.ts           # POST /api/v1/interview/start
│   │   └── types.ts               # API 타입 정의
│   │
│   ├── components/                # 재사용 컴포넌트
│   │   ├── common/
│   │   │   ├── Header.tsx
│   │   │   ├── Footer.tsx
│   │   │   ├── Navigation.tsx
│   │   │   ├── Loading.tsx
│   │   │   └── ErrorBoundary.tsx
│   │   ├── auth/
│   │   │   ├── LoginForm.tsx
│   │   │   ├── RegisterForm.tsx
│   │   │   └── ProtectedRoute.tsx
│   │   ├── learning/
│   │   │   ├── LectureCard.tsx
│   │   │   ├── VideoPlayer.tsx    # react-player 또는 video.js
│   │   │   ├── QuestionOverlay.tsx
│   │   │   ├── VoiceRecorder.tsx
│   │   │   └── AnswerFeedback.tsx
│   │   └── interview/
│   │       ├── InterviewPrompt.tsx
│   │       ├── InterviewCamera.tsx
│   │       ├── EmotionIndicator.tsx
│   │       └── TimelineFeedback.tsx
│   │
│   ├── pages/                     # 페이지 컴포넌트
│   │   ├── Home.tsx               # 메인 페이지
│   │   ├── Login.tsx
│   │   ├── Register.tsx
│   │   ├── LectureList.tsx        # 학습 컨텐츠 목록
│   │   ├── LearningPage.tsx       # 1-2단계 학습
│   │   ├── InterviewPage.tsx      # 3단계 면접
│   │   ├── ReviewPage.tsx         # 학습 복기
│   │   └── Profile.tsx
│   │
│   ├── hooks/                     # Custom Hooks
│   │   ├── useAuth.ts             # JWT 토큰 관리
│   │   ├── useSpeechRecognition.ts
│   │   ├── useVideoRecorder.ts
│   │   ├── useLearningProgress.ts
│   │   └── useWebSocket.ts        # 3단계 WebSocket
│   │
│   ├── store/                     # Context API 또는 Zustand
│   │   ├── authContext.tsx
│   │   ├── learningContext.tsx
│   │   └── interviewContext.tsx
│   │
│   ├── styles/                    # styled-components
│   │   ├── GlobalStyle.ts
│   │   ├── theme.ts               # 색상, 폰트 테마
│   │   └── mixins.ts              # 재사용 스타일
│   │
│   ├── i18n/                      # 다국어 지원 (i18next)
│   │   ├── index.ts
│   │   └── locales/
│   │       ├── ko.json
│   │       └── en.json
│   │
│   ├── utils/
│   │   ├── localStorage.ts        # 토큰 저장
│   │   ├── videoUtils.ts
│   │   └── speechUtils.ts
│   │
│   ├── types/
│   │   ├── auth.ts
│   │   ├── lecture.ts
│   │   └── interview.ts
│   │
│   ├── App.tsx                    # 메인 App 컴포넌트
│   ├── index.tsx                  # Entry Point
│   └── react-app-env.d.ts
│
├── package.json
├── tsconfig.json
├── .env.development
├── .env.production
└── vite.config.ts                 # 또는 craco.config.js
```

#### 핵심 의존성 (package.json)

```json
{
  "name": "seungjz-edutech-frontend",
  "version": "0.1.0",
  "dependencies": {
    "react": "^18.3.1",
    "react-dom": "^18.3.1",
    "react-router-dom": "^7.9.3",
    "axios": "^1.6.0",
    "styled-components": "^6.1.19",
    "i18next": "^23.15.0",
    "react-i18next": "^14.1.0",
    "framer-motion": "^12.23.22",
    "react-icons": "^4.12.0",
    "zustand": "^4.5.0",
    "typescript": "^4.9.5"
  },
  "devDependencies": {
    "@types/react": "^18.3.24",
    "@types/react-dom": "^18.3.7",
    "@types/styled-components": "^5.1.34",
    "vite": "^5.0.0",
    "@vitejs/plugin-react": "^4.2.0"
  }
}
```

#### API 클라이언트 구현 (nawbio 참조)

```typescript
// src/api/client.ts
import axios, { AxiosInstance } from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1';

export const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor - JWT 토큰 자동 추가
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response Interceptor - 401 에러 시 로그인 페이지 리다이렉트
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

```typescript
// src/api/auth.ts
import { apiClient } from './client';
import { LoginRequest, RegisterRequest, TokenResponse } from './types';

export const authApi = {
  login: async (data: LoginRequest): Promise<TokenResponse> => {
    const response = await apiClient.post('/auth/login', data);
    return response.data;
  },

  register: async (data: RegisterRequest): Promise<TokenResponse> => {
    const response = await apiClient.post('/auth/register', data);
    return response.data;
  },

  logout: async (): Promise<void> => {
    localStorage.removeItem('accessToken');
  },
};
```

```typescript
// src/api/lecture.ts
import { apiClient } from './client';
import { Lecture, Component } from './types';

export const lectureApi = {
  getAll: async (level?: number): Promise<Lecture[]> => {
    const response = await apiClient.get('/lectures', {
      params: { level }
    });
    return response.data;
  },

  getById: async (id: number): Promise<Lecture> => {
    const response = await apiClient.get(`/lectures/${id}`);
    return response.data;
  },

  getComponents: async (lectureId: number): Promise<Component[]> => {
    const response = await apiClient.get(`/lectures/${lectureId}/components`);
    return response.data;
  },
};
```

#### 3단계 비디오 녹화 및 감정 분석

**VideoRecorder 컴포넌트**:
```javascript
// services/videoCapture.js
import { useRef, useState } from 'react';

export const useVideoCapture = () => {
  const [isRecording, setIsRecording] = useState(false);
  const mediaRecorderRef = useRef(null);
  const videoStreamRef = useRef(null);

  const startRecording = async () => {
    // 카메라 + 마이크 접근
    const stream = await navigator.mediaDevices.getUserMedia({
      video: { width: 1280, height: 720, facingMode: 'user' },
      audio: true
    });

    videoStreamRef.current = stream;

    // MediaRecorder로 전체 세션 녹화
    const recorder = new MediaRecorder(stream, {
      mimeType: 'video/webm;codecs=vp9'
    });

    mediaRecorderRef.current = recorder;

    // 프레임 캡처 (1초마다 DeepFace 분석용)
    const canvas = document.createElement('canvas');
    const video = document.createElement('video');
    video.srcObject = stream;

    const captureInterval = setInterval(() => {
      canvas.getContext('2d').drawImage(video, 0, 0);
      canvas.toBlob(blob => {
        // 서버로 프레임 전송 (감정 분석)
        sendFrameForAnalysis(blob);
      }, 'image/jpeg', 0.8);
    }, 1000);  // 1초마다

    recorder.start();
    setIsRecording(true);
  };

  const sendFrameForAnalysis = async (frameBlob) => {
    const formData = new FormData();
    formData.append('frame', frameBlob);

    const response = await fetch('/api/emotion/analyze-frame', {
      method: 'POST',
      body: formData
    });

    const result = await response.json();
    // 실시간 감정 표시 업데이트
    return result;
  };

  return { startRecording, isRecording };
};
```

**장점**:
- 실시간 감정 피드백
- 전체 세션 비디오 저장 (복기용)
- 1초 단위 감정 타임라인

---

### 2. Backend Layer (Spring Boot 3.2)

#### 주요 책임
- RESTful API 제공 (`/api/v1/*`)
- JWT 기반 인증/인가
- 비디오 스트리밍 (AWS S3 + CloudFront)
- Lecture 및 Component 관리
- 학습 진도 추적 및 복기
- ML 서비스 연동 (STT, 감정 분석, GPT 피드백)

#### nawbio-api 기반 패키지 구조

```
com.seungjz.edutech/
├── common/                        # 공통 모듈 (nawbio 참조)
│   ├── config/
│   │   ├── SecurityConfig.java    # Spring Security + JWT
│   │   ├── WebConfig.java         # CORS, Interceptor
│   │   ├── S3Config.java          # AWS S3 설정
│   │   └── SwaggerConfig.java     # Springdoc OpenAPI
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── BusinessException.java
│   │   └── ErrorCode.java
│   ├── dto/
│   │   ├── ApiResponse.java       # 통일된 응답 포맷
│   │   ├── PageResponse.java
│   │   └── ErrorResponse.java
│   └── util/
│       ├── JwtTokenProvider.java
│       └── FileUploadUtil.java
│
├── domain/                        # Domain 기반 설계 (nawbio 참조)
│   ├── auth/
│   │   ├── entity/
│   │   │   └── User.java
│   │   ├── repository/
│   │   │   └── UserRepository.java
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   └── UserService.java
│   │   ├── controller/
│   │   │   └── AuthController.java  # POST /api/v1/auth/register, /login
│   │   └── dto/
│   │       ├── LoginRequest.java
│   │       ├── RegisterRequest.java
│   │       └── TokenResponse.java
│   │
│   ├── lecture/                   # 학습 컨텐츠 (1-2단계)
│   │   ├── entity/
│   │   │   ├── Lecture.java
│   │   │   └── Component.java
│   │   ├── repository/
│   │   │   ├── LectureRepository.java
│   │   │   └── ComponentRepository.java
│   │   ├── service/
│   │   │   ├── LectureService.java
│   │   │   └── VideoStreamService.java
│   │   ├── controller/
│   │   │   └── LectureController.java  # GET /api/v1/lectures
│   │   └── dto/
│   │       ├── LectureResponse.java
│   │       └── ComponentResponse.java
│   │
│   ├── learning/                  # 학습 진행 및 답변
│   │   ├── entity/
│   │   │   ├── UserProgress.java
│   │   │   └── AnswerHistory.java
│   │   ├── repository/
│   │   │   ├── UserProgressRepository.java
│   │   │   └── AnswerHistoryRepository.java
│   │   ├── service/
│   │   │   ├── LearningService.java
│   │   │   └── ProgressService.java
│   │   ├── controller/
│   │   │   └── LearningController.java  # POST /api/v1/learning/submit
│   │   └── dto/
│   │       ├── SubmitAnswerRequest.java
│   │       └── FeedbackResponse.java
│   │
│   ├── interview/                 # 3단계 실시간 면접
│   │   ├── entity/
│   │   │   ├── InterviewSession.java
│   │   │   ├── InterviewQAPair.java
│   │   │   ├── EmotionAnalysis.java
│   │   │   └── InterviewFeedback.java
│   │   ├── repository/
│   │   │   ├── InterviewSessionRepository.java
│   │   │   ├── QAPairRepository.java
│   │   │   ├── EmotionAnalysisRepository.java
│   │   │   └── FeedbackRepository.java
│   │   ├── service/
│   │   │   ├── InterviewService.java
│   │   │   ├── InterviewWebSocketHandler.java
│   │   │   └── FeedbackGenerationService.java
│   │   ├── controller/
│   │   │   └── InterviewController.java  # POST /api/v1/interview/start
│   │   └── dto/
│   │       ├── StartInterviewRequest.java
│   │       ├── InterviewSessionResponse.java
│   │       └── TimelineFeedbackResponse.java
│   │
│   └── ml/                        # ML 서비스 연동
│       ├── client/
│       │   ├── KoSpeechClient.java  # KoSpeech STT 호출
│       │   ├── EmotionClient.java   # DeepFace 호출
│       │   └── GPTClient.java       # OpenAI GPT 호출
│       └── dto/
│           ├── TranscriptionResponse.java
│           └── EmotionAnalysisResult.java
│
└── EdutechApplication.java        # Main Entry Point
```

#### 핵심 설정 (application.yml)

```yaml
spring:
  application:
    name: seungjz-edutech-api

  datasource:
    url: jdbc:postgresql://localhost:5432/edutech
    username: postgres
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB

# JWT 설정
jwt:
  secret: ${JWT_SECRET:your-256-bit-secret-key-change-in-production}
  expiration: 86400000  # 24시간

# CORS 설정
cors:
  allowed-origins: http://localhost:3000,http://localhost:5173
  allowed-methods: GET,POST,PUT,DELETE,PATCH,OPTIONS
  allowed-headers: "*"
  allow-credentials: true

# AWS S3 설정
cloud:
  aws:
    region:
      static: ${AWS_REGION:ap-northeast-2}
    credentials:
      access-key: ${AWS_ACCESS_KEY}
      secret-key: ${AWS_SECRET_KEY}
    s3:
      bucket: ${S3_BUCKET_NAME}

# ML Services URL
ml:
  kospeech:
    url: ${KOSPEECH_SERVICE_URL:http://localhost:5000}
  emotion:
    url: ${EMOTION_SERVICE_URL:http://localhost:5001}
  gpt:
    api-key: ${OPENAI_API_KEY}
    model: gpt-4-turbo

server:
  port: 8080
  servlet:
    context-path: /api/v1

# Swagger
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operations-sorter: method
```

#### 비디오 스트리밍 아키텍처

```java
@RestController
@RequestMapping("/api/videos")
public class VideoStreamController {

    // HLS (HTTP Live Streaming) 지원
    @GetMapping("/{lectureId}/stream")
    public ResponseEntity<Resource> streamVideo(
        @PathVariable Long lectureId,
        @RequestHeader(value = "Range", required = false) String range
    ) {
        // AWS S3에서 비디오 가져오기
        VideoMetadata metadata = videoService.getMetadata(lectureId);

        // Range 요청 처리 (Seek 지원)
        if (range != null) {
            return handleRangeRequest(metadata, range);
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("video/mp4"))
            .body(videoService.getVideoResource(lectureId));
    }
}
```

**비디오 저장 전략**:
- AWS S3 + CloudFront CDN
- 다중 해상도 인코딩 (360p, 720p, 1080p)
- HLS/DASH 프로토콜 지원

#### Lecture Component 시스템

```java
@Entity
public class Lecture {
    @Id @GeneratedValue
    private Long id;

    private String title;
    private Integer level; // 1, 2, 3단계

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL)
    private List<Component> components;
}

@Entity
public class Component {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Lecture lecture;

    @Enumerated(EnumType.STRING)
    private ComponentType type; // VIDEO, QUESTION, ANSWER_CHOICE

    private Integer sequence; // 순서
    private String content; // JSON 형태의 컨텐츠

    // VIDEO: { "videoUrl": "...", "startTime": 0, "endTime": 30 }
    // QUESTION: { "text": "...", "triggerTime": 25 }
    // ANSWER_CHOICE: { "choices": [...], "correctIndex": 0 }
}
```

**Component 실행 흐름**:
1. 클라이언트가 `/api/lectures/{id}/components` 요청
2. 서버가 sequence 순서대로 Component 반환
3. VIDEO → QUESTION → (사용자 답변) → 검증 → 다음 Component

#### 3단계 실시간 면접 처리

```java
@Service
public class InterviewService {

    @Autowired
    private KoSpeechClient koSpeechClient;

    @Autowired
    private OpenAIClient openAIClient;

    // WebSocket 기반 실시간 처리
    public void handleInterviewSession(String sessionId, String userPrompt) {
        // 1. GPT로 초기 질문 생성 (5W1H 기반)
        List<String> questions = openAIClient.generateQuestions(userPrompt);

        // 2. 3분간 질문-답변 반복
        for (int i = 0; i < questions.size(); i++) {
            webSocketService.sendQuestion(sessionId, questions.get(i));

            // 사용자 음성 수신 (WebSocket)
            byte[] audioData = webSocketService.receiveAudio(sessionId);

            // 3. KoSpeech로 음성→텍스트 변환
            String transcript = koSpeechClient.transcribe(audioData);

            // 4. GPT로 답변 분석 및 다음 질문 생성
            AnalysisResult analysis = openAIClient.analyzeAnswer(
                questions.get(i), transcript
            );

            // 키워드 누락, 문맥 어색함 탐지
            if (analysis.hasMissingKeywords() || analysis.isAwkward()) {
                feedbackList.add(createTimedFeedback(analysis));
            }

            // 연계 질문 생성
            questions.add(openAIClient.generateFollowUp(transcript));
        }

        // 5. 최종 피드백 생성 (MP4 타임라인 링크)
        return generateTimelineFeedback(feedbackList);
    }
}
```

---

### 3. Database Layer (PostgreSQL)

#### 스키마 설계

```sql
-- 사용자 관리
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    provider VARCHAR(50) NOT NULL, -- GOOGLE, KAKAO
    provider_id VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    profile_image_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 학습 컨텐츠
CREATE TABLE lectures (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    level INTEGER NOT NULL, -- 1, 2, 3
    thumbnail_url TEXT,
    video_url TEXT, -- S3 URL
    duration INTEGER, -- 초 단위
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Lecture 구성요소
CREATE TABLE components (
    id BIGSERIAL PRIMARY KEY,
    lecture_id BIGINT REFERENCES lectures(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL, -- VIDEO, QUESTION, ANSWER_CHOICE
    sequence INTEGER NOT NULL, -- 순서
    content JSONB NOT NULL, -- 유연한 컨텐츠 저장
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(lecture_id, sequence)
);

-- 학습 진도
CREATE TABLE progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    lecture_id BIGINT REFERENCES lectures(id) ON DELETE CASCADE,
    completed_components JSONB DEFAULT '[]', -- [1, 2, 3]
    score INTEGER, -- 정답률
    last_accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_completed BOOLEAN DEFAULT FALSE,
    UNIQUE(user_id, lecture_id)
);

-- 3단계 면접 세션
CREATE TABLE interview_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    prompt TEXT NOT NULL, -- 사용자 입력 프롬프트
    duration INTEGER DEFAULT 180, -- 3분
    video_recording_url TEXT, -- 녹화 영상 URL
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- AI 피드백
CREATE TABLE feedback (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT REFERENCES interview_sessions(id) ON DELETE CASCADE,
    timeline_sec INTEGER, -- 타임라인 위치 (초)
    feedback_type VARCHAR(50), -- MISSING_KEYWORD, AWKWARD_CONTEXT
    content TEXT, -- 피드백 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX idx_progress_user ON progress(user_id);
CREATE INDEX idx_components_lecture ON components(lecture_id, sequence);
CREATE INDEX idx_feedback_session ON feedback(session_id);
```

---

### 4. ML Services Layer (Python)

음성인식 및 AI 분석을 위한 별도 마이크로서비스

#### 디렉토리 구조

```
ml-services/
├── stt-kospeech/                    # KoSpeech STT 서비스
│   ├── app.py                       # Flask API 서버
│   ├── kospeech_inference.py        # KoSpeech 추론 로직
│   ├── audio_processor.py           # 오디오 전처리
│   ├── requirements.txt
│   ├── Dockerfile
│   └── models/                      # 사전 학습 모델 저장
│       └── conformer_large.pt
│
├── emotion-deepface/                # DeepFace 감정 분석
│   ├── app.py
│   ├── emotion_analyzer.py
│   ├── requirements.txt
│   └── Dockerfile
│
└── docker-compose.ml.yml           # ML 서비스 Docker Compose
```

---

#### KoSpeech STT 서비스 구현

**참조**: https://github.com/sooftware/kospeech

##### 1. requirements.txt

```txt
# ml-services/stt-kospeech/requirements.txt
flask==3.0.0
torch==2.1.0
torchaudio==2.1.0
kospeech==1.0.1
librosa==0.10.1
scipy==1.11.4
numpy==1.24.3
soundfile==0.12.1
gunicorn==21.2.0
```

##### 2. KoSpeech 추론 모듈

```python
# ml-services/stt-kospeech/kospeech_inference.py
import torch
import torchaudio
import librosa
import numpy as np
from typing import Tuple
import os

class KoSpeechInference:
    def __init__(self, model_path: str = 'models/conformer_large.pt'):
        """
        KoSpeech 모델 초기화

        모델 다운로드:
        wget https://github.com/sooftware/kospeech/releases/download/v1.0/conformer_large.pt
        """
        self.device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
        print(f"Using device: {self.device}")

        # KoSpeech 모델 로드
        try:
            # Option 1: 사전 학습된 모델 로드
            from kospeech.models import Model
            self.model = Model.load_model(model_path)
            self.model.to(self.device)
            self.model.eval()

            print(f"KoSpeech model loaded from {model_path}")
        except Exception as e:
            print(f"Error loading model: {e}")
            # Fallback: 기본 모델 사용
            from kospeech.models.conformer import Conformer
            self.model = Conformer(
                num_classes=2000,  # 한국어 어휘 크기
                input_dim=80,
                encoder_dim=512,
                num_encoder_layers=17
            )
            self.model.to(self.device)
            self.model.eval()

        # 한국어 어휘 사전 로드
        self.vocab = self._load_vocabulary()

    def _load_vocabulary(self):
        """한국어 어휘 사전 로드"""
        # KoSpeech 기본 한글 어휘
        vocab_file = 'vocabs/aihub_character_vocabs.csv'
        if os.path.exists(vocab_file):
            import pandas as pd
            df = pd.read_csv(vocab_file)
            return {idx: char for idx, char in enumerate(df['char'])}
        else:
            # 기본 한글 자모 + 특수문자
            return {i: chr(i) for i in range(0x1100, 0x1200)}

    def preprocess_audio(self, audio_path: str, sample_rate: int = 16000) -> torch.Tensor:
        """
        오디오 파일 전처리

        Returns:
            torch.Tensor: (1, time, mel_bins) 형태의 텐서
        """
        # 오디오 로드 (16kHz로 리샘플)
        waveform, sr = librosa.load(audio_path, sr=sample_rate)

        # Mel-spectrogram 추출 (80 bins)
        mel_spec = librosa.feature.melspectrogram(
            y=waveform,
            sr=sample_rate,
            n_mels=80,
            n_fft=512,
            hop_length=160
        )

        # Log-scale
        mel_spec = librosa.power_to_db(mel_spec, ref=np.max)

        # Normalize
        mel_spec = (mel_spec - mel_spec.mean()) / (mel_spec.std() + 1e-8)

        # PyTorch 텐서로 변환 (1, time, mel_bins)
        mel_tensor = torch.FloatTensor(mel_spec).transpose(0, 1).unsqueeze(0)

        return mel_tensor

    @torch.no_grad()
    def transcribe(self, audio_path: str) -> Tuple[str, float]:
        """
        음성을 텍스트로 변환

        Args:
            audio_path: 오디오 파일 경로 (.wav, .mp3 등)

        Returns:
            (transcript, confidence): 변환된 텍스트와 신뢰도
        """
        # 오디오 전처리
        mel_input = self.preprocess_audio(audio_path).to(self.device)

        # 모델 추론
        outputs = self.model(mel_input)

        # Greedy Decoding
        predictions = outputs.argmax(dim=-1).squeeze(0)

        # 토큰 ID → 텍스트 변환
        transcript = self._decode_predictions(predictions)

        # 신뢰도 계산 (softmax 최대값 평균)
        confidence = torch.softmax(outputs, dim=-1).max(dim=-1).values.mean().item()

        return transcript, confidence

    def _decode_predictions(self, predictions: torch.Tensor) -> str:
        """토큰 ID를 텍스트로 디코딩"""
        chars = []
        for token_id in predictions.cpu().numpy():
            if token_id in self.vocab:
                chars.append(self.vocab[token_id])

        # CTC 중복 제거 (예: "ㅎㅎ안안녕" → "안녕")
        text = ''.join(chars)
        text = self._remove_ctc_duplicates(text)

        return text.strip()

    def _remove_ctc_duplicates(self, text: str) -> str:
        """CTC 중복 문자 제거"""
        if not text:
            return text

        result = [text[0]]
        for char in text[1:]:
            if char != result[-1]:
                result.append(char)

        return ''.join(result)

# 전역 인스턴스 (서버 시작 시 한 번만 로드)
kospeech_model = None

def get_kospeech_model():
    global kospeech_model
    if kospeech_model is None:
        kospeech_model = KoSpeechInference()
    return kospeech_model
```

##### 3. Flask API 서버

```python
# ml-services/stt-kospeech/app.py
from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
import os
import tempfile
import time
from kospeech_inference import get_kospeech_model

app = Flask(__name__)
app.config['MAX_CONTENT_LENGTH'] = 50 * 1024 * 1024  # 50MB

# 모델 초기화 (서버 시작 시)
print("Loading KoSpeech model...")
model = get_kospeech_model()
print("Model loaded successfully!")

@app.route('/health', methods=['GET'])
def health():
    """헬스 체크"""
    return jsonify({
        'status': 'healthy',
        'service': 'kospeech-stt',
        'device': str(model.device)
    })

@app.route('/transcribe', methods=['POST'])
def transcribe():
    """
    음성 파일을 받아 텍스트로 변환

    Request:
        - audio: 오디오 파일 (wav, mp3, m4a, webm)

    Response:
        {
            "transcript": "안녕하세요",
            "confidence": 0.95,
            "duration_ms": 1234
        }
    """
    start_time = time.time()

    try:
        # 파일 검증
        if 'audio' not in request.files:
            return jsonify({'error': 'No audio file provided'}), 400

        audio_file = request.files['audio']
        if audio_file.filename == '':
            return jsonify({'error': 'Empty filename'}), 400

        # 임시 파일로 저장
        filename = secure_filename(audio_file.filename)
        temp_dir = tempfile.gettempdir()
        temp_path = os.path.join(temp_dir, f'audio_{int(time.time())}_{filename}')

        audio_file.save(temp_path)

        # KoSpeech 추론
        transcript, confidence = model.transcribe(temp_path)

        # 임시 파일 삭제
        os.remove(temp_path)

        duration_ms = int((time.time() - start_time) * 1000)

        return jsonify({
            'transcript': transcript,
            'confidence': round(confidence, 4),
            'duration_ms': duration_ms
        })

    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/transcribe-batch', methods=['POST'])
def transcribe_batch():
    """
    여러 오디오 파일을 일괄 변환

    Request:
        - audios[]: 오디오 파일 배열

    Response:
        {
            "results": [
                {"transcript": "안녕하세요", "confidence": 0.95},
                {"transcript": "감사합니다", "confidence": 0.92}
            ]
        }
    """
    try:
        if 'audios[]' not in request.files:
            return jsonify({'error': 'No audio files provided'}), 400

        audio_files = request.files.getlist('audios[]')
        results = []

        for audio_file in audio_files:
            filename = secure_filename(audio_file.filename)
            temp_path = os.path.join(tempfile.gettempdir(), f'batch_{int(time.time())}_{filename}')

            audio_file.save(temp_path)
            transcript, confidence = model.transcribe(temp_path)
            os.remove(temp_path)

            results.append({
                'filename': filename,
                'transcript': transcript,
                'confidence': round(confidence, 4)
            })

        return jsonify({'results': results})

    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    # Production: gunicorn app:app -w 4 -b 0.0.0.0:5000
    app.run(host='0.0.0.0', port=5000, debug=False)
```

---

## 주요 기능별 플로우

### 1단계/2단계 학습 플로우

```
[사용자] → [Frontend]
   │
   ├─ GET /api/lectures?level=1 ─→ [Backend]
   │                                    │
   │                                    ├─ Query Lectures
   │                                    └─ Return List
   │
   ├─ GET /api/lectures/{id}/components
   │                                    │
   │                                    └─ Return [VIDEO, QUESTION, ...]
   │
   ├─ 비디오 재생 (Component[0])
   │     ↓ (특정 시점)
   │
   ├─ 질문 표시 (Component[1])
   │     ↓
   │
   ├─ 음성 입력 (Web Speech API - 클라이언트)
   │     ↓
   │
   ├─ POST /api/validate ─→ [Backend]
   │     { userSpeech, correctAnswer }
   │                                    │
   │                                    ├─ 정답 비교
   │                                    └─ Return { isCorrect, feedback }
   │
   └─ 결과 표시
```

### 3단계 실시간 면접 플로우

```
[사용자] → [Frontend]
   │
   ├─ 프롬프트 입력: "마케팅 인턴 면접"
   │     ↓
   │
   ├─ POST /api/interview/start ─→ [Backend]
   │     { prompt: "마케팅 인턴 면접" }
   │                                    │
   │                                    ├─ GPT: 초기 질문 생성
   │                                    │   "자기소개를 해주세요"
   │                                    │
   │                                    └─ WebSocket 세션 생성
   │
   ├─ WebSocket 연결
   │     ↓
   │
   ├─ [3분 루프 시작]
   │   │
   │   ├─ 질문 수신: "자기소개를 해주세요"
   │   │     ↓
   │   │
   │   ├─ 음성 답변 녹음 + 전송
   │   │     ↓
   │   │
   │   ├─ [Backend] → [KoSpeech API]
   │   │                    POST /transcribe
   │   │                    { audio: blob }
   │   │                         ↓
   │   │                    Return "저는 ..."
   │   │
   │   ├─ [Backend] → [GPT API]
   │   │                    분석: 키워드 누락? 문맥 어색?
   │   │                    다음 질문 생성: "마케팅 경험은?"
   │   │
   │   └─ 반복 (3분 종료까지)
   │
   ├─ [3분 종료]
   │     ↓
   │
   └─ GET /api/interview/{sessionId}/feedback
                                    │
                                    └─ Return [
                                        { timeline: 30, type: "MISSING_KEYWORD", ... },
                                        { timeline: 120, type: "AWKWARD_CONTEXT", ... }
                                    ]
```

---

## 비기능적 요구사항

### 성능
- 비디오 스트리밍: 3초 이내 초기 로딩
- 음성인식 응답: 1초 이내 (클라이언트), 3초 이내 (서버)
- API 응답시간: 평균 200ms 이하

### 확장성
- 동시 접속자 1000명 지원
- 비디오 CDN을 통한 글로벌 배포
- 수평 확장 가능한 무상태(stateless) API

### 보안
- OAuth 2.0 소셜 로그인
- JWT 기반 인증
- HTTPS 필수
- 비디오 URL 시간 제한 서명 (Pre-signed URL)

### 가용성
- 99.9% 업타임 목표
- AWS Multi-AZ 배포
- 자동 백업 (일 1회)

---

## 배포 아키텍처

```
┌──────────────────────────────────────────┐
│         CloudFront CDN                   │
│  (정적 파일 + 비디오 스트리밍)              │
└─────────┬────────────────────────────────┘
          │
┌─────────▼────────┐      ┌───────────────┐
│   S3 Bucket      │      │  Route 53     │
│  (React Build)   │      │   (DNS)       │
└──────────────────┘      └───────┬───────┘
                                  │
                          ┌───────▼───────┐
                          │  ALB (Load    │
                          │   Balancer)   │
                          └───────┬───────┘
                                  │
          ┌───────────────────────┼───────────────────┐
          │                       │                   │
┌─────────▼────────┐  ┌──────────▼───────┐  ┌───────▼──────┐
│  EC2 Instance 1  │  │  EC2 Instance 2  │  │  EC2 Auto    │
│  (Spring Boot)   │  │  (Spring Boot)   │  │  Scaling     │
└─────────┬────────┘  └──────────┬───────┘  └──────────────┘
          │                       │
          └───────────┬───────────┘
                      │
          ┌───────────▼───────────┐
          │  RDS PostgreSQL       │
          │  (Multi-AZ)           │
          └───────────────────────┘

┌───────────────────────────────────────────┐
│  Separate EC2 (GPU Instance)              │
│  Python Flask (KoSpeech)                  │
└───────────────────────────────────────────┘
```

---

## Docker 기반 배포 아키텍처

### 전체 Docker Compose 구조

```yaml
version: '3.8'

services:
  # Frontend
  frontend:
    build: ./frontend
    ports:
      - "3000:3000"
    environment:
      - VITE_API_URL=http://localhost:8080
    depends_on:
      - backend

  # Backend
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/edutech
      - ML_STT_SERVICE_URL=http://ml-stt:5000
      - ML_EMOTION_SERVICE_URL=http://ml-emotion:5001
      - OPENAI_API_KEY=${OPENAI_API_KEY}
    depends_on:
      - postgres
      - ml-stt
      - ml-emotion

  # ML Service 1: 음성인식 (KoSpeech)
  ml-stt:
    build:
      context: ./ml-services/stt
      dockerfile: Dockerfile.gpu  # 또는 Dockerfile.cpu
    ports:
      - "5000:5000"
    environment:
      - USE_GPU=true
      - MODEL_TYPE=kospeech  # 또는 whisper-api
      - OPENAI_API_KEY=${OPENAI_API_KEY}
    volumes:
      - ./models/kospeech:/app/models
    deploy:
      resources:
        reservations:
          devices:
            - driver: nvidia
              count: 1
              capabilities: [gpu]

  # ML Service 2: 감정 분석 (DeepFace)
  ml-emotion:
    build:
      context: ./ml-services/emotion
      dockerfile: Dockerfile
    ports:
      - "5001:5001"
    environment:
      - MODEL_BACKEND=opencv  # 'opencv', 'ssd', 'retinaface'
      - DETECTION_BACKEND=opencv
    volumes:
      - ./models/deepface:/root/.deepface
    # CPU로 충분히 빠름 (GPU 불필요)

  # Database
  postgres:
    image: postgres:14
    environment:
      - POSTGRES_DB=edutech
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
    volumes:
      - postgres-data:/var/lib/postgresql/data
      - ./database/init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "5432:5432"

volumes:
  postgres-data:
```

### 로컬 개발 실행

```bash
# .env 파일 생성
echo "OPENAI_API_KEY=sk-..." > .env
echo "USE_GPU=true" >> .env

# Docker Compose 실행 (로컬 RTX 3070 사용)
docker-compose up --build

# 브라우저에서 접속
# http://localhost:3000
```

---

## ML Service 2: DeepFace 감정 분석 서비스

### 아키텍처

```
┌─────────────────────────────────────────┐
│     DeepFace Emotion Service            │
│          (Python Flask)                 │
├─────────────────────────────────────────┤
│                                         │
│  ┌──────────────────────────────────┐  │
│  │  Flask API (Port 5001)           │  │
│  └──────────┬───────────────────────┘  │
│             │                           │
│  ┌──────────▼───────────────────────┐  │
│  │  DeepFace Library                │  │
│  │  - VGG-Face, FaceNet, OpenFace   │  │
│  │  - Emotion Detection (7 classes) │  │
│  │  - Age, Gender, Race             │  │
│  └──────────┬───────────────────────┘  │
│             │                           │
│  ┌──────────▼───────────────────────┐  │
│  │  OpenCV (Face Detection)         │  │
│  │  - Haar Cascade                  │  │
│  │  - DNN (SSD, RetinaFace)         │  │
│  └──────────────────────────────────┘  │
│                                         │
└─────────────────────────────────────────┘
```

### Dockerfile

```dockerfile
# ml-services/emotion/Dockerfile
FROM python:3.10-slim

WORKDIR /app

# 시스템 의존성 설치
RUN apt-get update && apt-get install -y \
    libgl1-mesa-glx \
    libglib2.0-0 \
    libsm6 \
    libxext6 \
    libxrender-dev \
    && rm -rf /var/lib/apt/lists/*

# Python 패키지 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 모델 미리 다운로드 (빌드 시)
RUN python3 -c "from deepface import DeepFace; \
    DeepFace.build_model('Emotion'); \
    print('DeepFace models downloaded')"

COPY . .

EXPOSE 5001

CMD ["python3", "app.py"]
```

### requirements.txt

```txt
# ml-services/emotion/requirements.txt
flask==3.0.0
deepface==0.0.79
opencv-python==4.8.1.78
numpy==1.24.3
pillow==10.1.0
tf-keras==2.15.0
```

### Flask API 구현

```python
# ml-services/emotion/app.py
from flask import Flask, request, jsonify
from deepface import DeepFace
import cv2
import numpy as np
import base64
from datetime import datetime
import os

app = Flask(__name__)

# 환경변수 설정
MODEL_BACKEND = os.getenv('MODEL_BACKEND', 'opencv')
DETECTION_BACKEND = os.getenv('DETECTION_BACKEND', 'opencv')

@app.route('/health', methods=['GET'])
def health():
    return jsonify({'status': 'healthy', 'service': 'emotion-analysis'})

@app.route('/analyze-frame', methods=['POST'])
def analyze_frame():
    """
    단일 프레임 감정 분석

    Request:
        - frame: 이미지 파일 (jpeg/png)

    Response:
        {
            "emotion": "happy",
            "emotion_scores": {
                "angry": 0.01,
                "disgust": 0.0,
                "fear": 0.02,
                "happy": 0.85,
                "sad": 0.03,
                "surprise": 0.05,
                "neutral": 0.04
            },
            "dominant_emotion": "happy",
            "face_confidence": 0.95,
            "gaze_direction": "camera",
            "smile_intensity": 0.8,
            "face_region": {"x": 100, "y": 50, "w": 200, "h": 200},
            "timestamp": "2025-01-20T10:30:45"
        }
    """
    try:
        # 이미지 파일 읽기
        if 'frame' not in request.files:
            return jsonify({'error': 'No frame provided'}), 400

        file = request.files['frame']
        img_array = np.frombuffer(file.read(), np.uint8)
        img = cv2.imdecode(img_array, cv2.IMREAD_COLOR)

        if img is None:
            return jsonify({'error': 'Invalid image'}), 400

        # DeepFace로 감정 분석
        result = DeepFace.analyze(
            img_path=img,
            actions=['emotion', 'age', 'gender'],
            enforce_detection=False,  # 얼굴 없어도 에러 안남
            detector_backend=DETECTION_BACKEND
        )

        # 결과가 리스트로 반환됨 (여러 얼굴 가능)
        if isinstance(result, list):
            result = result[0]  # 첫 번째 얼굴만 사용

        # 시선 방향 분석 (간단한 휴리스틱)
        face_region = result.get('region', {})
        gaze = analyze_gaze(img, face_region)

        # 미소 강도 계산
        smile_intensity = result['emotion'].get('happy', 0) / 100.0

        return jsonify({
            'emotion': result['dominant_emotion'],
            'emotion_scores': result['emotion'],
            'dominant_emotion': result['dominant_emotion'],
            'face_confidence': result.get('face_confidence', 1.0),
            'gaze_direction': gaze,
            'smile_intensity': smile_intensity,
            'face_region': face_region,
            'age': result.get('age'),
            'gender': result.get('dominant_gender'),
            'timestamp': datetime.now().isoformat()
        })

    except Exception as e:
        return jsonify({'error': str(e)}), 500


def analyze_gaze(img, face_region):
    """
    시선 방향 분석 (간단한 구현)

    실제로는 eye landmarks 필요하지만,
    해커톤용으로는 얼굴 위치 기반 휴리스틱 사용
    """
    if not face_region:
        return 'unknown'

    img_center_x = img.shape[1] / 2
    face_center_x = face_region['x'] + face_region['w'] / 2

    # 얼굴이 이미지 중앙에 있으면 'camera'
    if abs(face_center_x - img_center_x) < 100:
        return 'camera'
    elif face_center_x < img_center_x:
        return 'left'
    else:
        return 'right'


@app.route('/analyze-video', methods=['POST'])
def analyze_video():
    """
    비디오 파일 전체 분석 (복기용)

    Request:
        - video: 비디오 파일 (.mp4, .webm)
        - interval: 분석 간격 (초, 기본 1초)

    Response:
        {
            "timeline": [
                {"time": 0, "emotion": "neutral", "confidence": 0.8},
                {"time": 1, "emotion": "happy", "confidence": 0.9},
                ...
            ],
            "summary": {
                "total_frames": 180,
                "avg_confidence": 0.85,
                "emotion_distribution": {
                    "happy": 60,
                    "neutral": 100,
                    "anxious": 20
                }
            }
        }
    """
    try:
        if 'video' not in request.files:
            return jsonify({'error': 'No video provided'}), 400

        video_file = request.files['video']
        interval = int(request.form.get('interval', 1))  # 기본 1초

        # 임시 파일 저장
        temp_path = f'/tmp/video_{datetime.now().timestamp()}.mp4'
        video_file.save(temp_path)

        # 비디오 분석
        timeline = []
        emotion_counts = {}

        cap = cv2.VideoCapture(temp_path)
        fps = cap.get(cv2.CAP_PROP_FPS)
        frame_interval = int(fps * interval)

        frame_idx = 0
        while cap.isOpened():
            ret, frame = cap.read()
            if not ret:
                break

            # interval마다 분석
            if frame_idx % frame_interval == 0:
                try:
                    result = DeepFace.analyze(
                        img_path=frame,
                        actions=['emotion'],
                        enforce_detection=False,
                        detector_backend=DETECTION_BACKEND
                    )

                    if isinstance(result, list):
                        result = result[0]

                    emotion = result['dominant_emotion']
                    time_sec = frame_idx / fps

                    timeline.append({
                        'time': round(time_sec, 1),
                        'emotion': emotion,
                        'confidence': max(result['emotion'].values()) / 100.0,
                        'scores': result['emotion']
                    })

                    emotion_counts[emotion] = emotion_counts.get(emotion, 0) + 1

                except:
                    pass  # 얼굴 감지 실패 시 스킵

            frame_idx += 1

        cap.release()
        os.remove(temp_path)

        # 요약 통계
        total_frames = len(timeline)
        avg_confidence = sum(t['confidence'] for t in timeline) / total_frames if total_frames > 0 else 0

        return jsonify({
            'timeline': timeline,
            'summary': {
                'total_frames': total_frames,
                'avg_confidence': round(avg_confidence, 2),
                'emotion_distribution': emotion_counts
            }
        })

    except Exception as e:
        return jsonify({'error': str(e)}), 500


@app.route('/models/info', methods=['GET'])
def models_info():
    """사용 가능한 모델 정보"""
    return jsonify({
        'detector_backend': DETECTION_BACKEND,
        'model_backend': MODEL_BACKEND,
        'available_emotions': [
            'angry', 'disgust', 'fear', 'happy',
            'sad', 'surprise', 'neutral'
        ],
        'available_detectors': [
            'opencv', 'ssd', 'dlib', 'mtcnn',
            'retinaface', 'mediapipe'
        ]
    })


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001, debug=False)
```

### Spring Boot 연동

```java
// backend/src/main/java/com/seungjz/edutech/external/EmotionAnalysisClient.java
@Service
public class EmotionAnalysisClient {

    @Value("${ml.emotion.service.url}")
    private String emotionServiceUrl;

    private final RestTemplate restTemplate;

    public EmotionAnalysisClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EmotionAnalysisResult analyzeFrame(byte[] frameData) {
        String url = emotionServiceUrl + "/analyze-frame";

        // Multipart 요청 생성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("frame", new ByteArrayResource(frameData) {
            @Override
            public String getFilename() {
                return "frame.jpg";
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
            new HttpEntity<>(body, headers);

        ResponseEntity<EmotionAnalysisResult> response =
            restTemplate.postForEntity(url, requestEntity, EmotionAnalysisResult.class);

        return response.getBody();
    }

    @Data
    public static class EmotionAnalysisResult {
        private String emotion;
        private Map<String, Double> emotionScores;
        private String dominantEmotion;
        private Double faceConfidence;
        private String gazeDirection;
        private Double smileIntensity;
        private Map<String, Integer> faceRegion;
        private String timestamp;
    }
}
```

---

## 감정 분석 API 비교

| API | 비용 | 정확도 | 응답속도 | 프라이버시 | 해커톤 적합도 |
|-----|------|--------|----------|------------|--------------|
| **DeepFace (오픈소스)** | ✅ 무료 | ⭐⭐⭐⭐ (85-90%) | 빠름 (0.2초/프레임) | ✅ 로컬 처리 | 🏆 최고 |
| Azure Face API | 30,000건/월 무료 | ⭐⭐⭐⭐⭐ (95%+) | 중간 (0.5초) | ❌ 클라우드 전송 | ⭐⭐⭐⭐ |
| Google Cloud Vision | 1,000건/월 무료 | ⭐⭐⭐⭐⭐ (95%+) | 중간 (0.6초) | ❌ 클라우드 전송 | ⭐⭐⭐ |
| AWS Rekognition | 5,000건/월 (12개월) | ⭐⭐⭐⭐⭐ (95%+) | 중간 (0.5초) | ❌ 클라우드 전송 | ⭐⭐⭐ |
| Face++ | 1,000건/일 무료 | ⭐⭐⭐⭐ (90%) | 빠름 (0.3초) | ❌ 클라우드 전송 | ⭐⭐⭐⭐ |

### DeepFace 선택 이유

1. **완전 무료** - API 호출 제한 없음
2. **Docker 통합** - 로컬 GPU 전략과 완벽 매칭
3. **프라이버시** - 비디오가 외부로 나가지 않음 (GDPR 준수)
4. **MIT 라이센스** - 상업적 사용 가능
5. **빠른 응답** - 네트워크 지연 없음
6. **오프라인 동작** - 인터넷 없어도 작동

### DeepFace 감정 클래스

```python
# 7가지 기본 감정
emotions = [
    'angry',     # 화남
    'disgust',   # 혐오
    'fear',      # 두려움
    'happy',     # 행복
    'sad',       # 슬픔
    'surprise',  # 놀람
    'neutral'    # 중립
]
```

---

## 로컬 → 클라우드 마이그레이션 가이드

### 1단계: 로컬 개발 (Day 1-2)

```bash
# RTX 3070 GPU 사용
docker-compose up

# 장점:
# - 비용: 전기세 ~3,780원
# - 학습 곡선: 없음
# - 개발 속도: 최고
```

### 2단계: 클라우드 배포 (Day 3 - 데모)

#### 옵션 A: Whisper API 전환 (GPU 불필요)

```bash
# .env 수정
USE_GPU=false
MODEL_TYPE=whisper-api

# Render.com/Railway 무료 배포
docker-compose -f docker-compose.prod.yml up
```

#### 옵션 B: Azure Container Instances

```bash
# 이미지 빌드 및 푸시
docker build -t yourrepo/ml-emotion:latest ./ml-services/emotion
docker push yourrepo/ml-emotion:latest

# Azure 배포
az container create \
  --resource-group edutech \
  --name emotion-service \
  --image yourrepo/ml-emotion:latest \
  --cpu 2 --memory 4 \
  --ports 5001

# STT는 GPU 필요 시
az container create \
  --resource-group edutech \
  --name stt-service \
  --image yourrepo/ml-stt:latest \
  --gpu-count 1 \
  --gpu-sku K80
```

### 3단계: Kubernetes (프로덕션)

```yaml
# k8s/emotion-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: emotion-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: emotion-service
  template:
    metadata:
      labels:
        app: emotion-service
    spec:
      containers:
      - name: emotion
        image: yourrepo/ml-emotion:latest
        ports:
        - containerPort: 5001
        resources:
          requests:
            memory: "2Gi"
            cpu: "1000m"
          limits:
            memory: "4Gi"
            cpu: "2000m"
```

---

## 개발 로드맵

### Phase 1: MVP (4주)
- [ ] 소셜 로그인 구현
- [ ] 1단계 학습 컨텐츠 관리
- [ ] 비디오 재생 + 클라이언트 음성인식
- [ ] 기본 정답 검증

### Phase 2: 고급 기능 (4주)
- [ ] 2단계 드라마 컨텐츠 추가
- [ ] KoSpeech 통합
- [ ] 학습 진도 및 복기 기능

### Phase 3: 실시간 면접 (4주)
- [ ] 3단계 WebSocket 구현
- [ ] GPT 기반 질문 생성
- [ ] 타임라인 피드백 시스템

### Phase 4: 최적화 (2주)
- [ ] 성능 튜닝
- [ ] CDN 설정
- [ ] 모니터링 및 로깅

---

## 기술적 도전과제 및 해결 방안

### 1. 비디오 스트리밍 최적화
**문제**: 대용량 MP4 파일 전송 시 로딩 시간 증가

**해결**:
- HLS/DASH 프로토콜 적용 (청크 단위 스트리밍)
- AWS CloudFront CDN 활용
- Adaptive Bitrate Streaming (네트워크 상황 따라 화질 조정)

### 2. 음성인식 정확도
**문제**: 외국인 발음, 배경 소음 처리

**해결**:
- KoSpeech 모델 + 발음 보정 후처리
- 사용자별 음성 프로필 학습 (점진적 개선)
- 소음 제거 전처리 (WebRTC Audio Processing)

### 3. 실시간 면접 동시성
**문제**: 다수 사용자 동시 면접 시 서버 부하

**해결**:
- WebSocket 풀링 (Netty 기반)
- Redis Pub/Sub으로 세션 관리
- 비동기 음성인식 처리 (Queue 기반)

### 4. 클라이언트 vs 서버 음성인식 결정
**최종 권장 사항**:

| 단계 | 처리 위치 | 이유 |
|------|----------|------|
| 1-2단계 | 클라이언트 (Web Speech API) | 즉각 피드백, 간단한 정답 비교 |
| 3단계 | 서버 (KoSpeech) | 복잡한 문맥 분석, 지속적 모델 개선 |

---

## 참고 자료

### 음성인식
- [KoSpeech GitHub](https://github.com/sooftware/kospeech)
- [한국어 STT 구현 가이드](https://velog.io/@letgodchan0/%EC%9D%8C%EC%84%B1%EC%9D%B8%EC%8B%9D-%ED%95%9C%EA%B5%AD%EC%96%B4-STT-5)
- [OpenAI Whisper API](https://platform.openai.com/docs/guides/speech-to-text)

### 감정 분석
- [DeepFace GitHub](https://github.com/serengil/deepface)
- [DeepFace 사용 가이드](https://viso.ai/computer-vision/deepface/)
- [Facial Expression Recognition (FER)](https://github.com/topics/face-emotion-recognition)

### 비디오 처리
- [MediaRecorder API MDN](https://developer.mozilla.org/en-US/docs/Web/API/MediaRecorder)
- [getUserMedia API](https://developer.mozilla.org/en-US/docs/Web/API/MediaDevices/getUserMedia)
- [HLS Video Streaming Guide](https://docs.aws.amazon.com/ko_kr/mediaconvert/latest/ug/what-is.html)

### Docker & 배포
- [Docker Compose GPU Support](https://docs.docker.com/compose/gpu-support/)
- [Azure Container Instances](https://learn.microsoft.com/azure/container-instances/)
- [Kubernetes GPU Scheduling](https://kubernetes.io/docs/tasks/manage-gpus/scheduling-gpus/)
