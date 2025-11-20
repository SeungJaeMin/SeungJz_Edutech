# SeungJz 애플리케이션 아키텍처

## 시스템 개요

3-Tier 아키텍처 기반의 모바일 우선 한국어 학습 플랫폼

```
┌─────────────────┐
│   Mobile Web    │  React SPA
│   (Frontend)    │  Web Speech API
└────────┬────────┘
         │ HTTPS/REST
         │
┌────────▼────────┐
│  Spring Boot    │  Business Logic
│   (Backend)     │  Video Streaming
└────────┬────────┘
         │ JDBC
         │
┌────────▼────────┐
│   PostgreSQL    │  Persistent Storage
│   (Database)    │
└─────────────────┘

┌─────────────────┐
│  ML Services    │  KoSpeech STT
│  (Python/Flask) │  DeepFace (Emotion)
│                 │  GPT API
└─────────────────┘
```

## 계층별 상세 설계

### 1. Frontend Layer (React)

#### 주요 책임
- 모바일 최적화 UI/UX
- 비디오 재생 및 동기화
- 1-2단계 클라이언트 측 음성인식
- 학습 진도 시각화

#### 핵심 컴포넌트

```
src/
├── components/
│   ├── auth/
│   │   ├── SocialLogin.jsx        # 소셜 로그인 UI
│   │   └── AuthCallback.jsx       # OAuth 콜백 처리
│   ├── learning/
│   │   ├── LectureList.jsx        # 학습 컨텐츠 목록
│   │   ├── VideoPlayer.jsx        # 비디오 재생기 (Video.js)
│   │   ├── QuestionOverlay.jsx    # 질문 오버레이
│   │   ├── VoiceInput.jsx         # 음성 입력 (Web Speech API)
│   │   └── ResultCard.jsx         # 결과 및 피드백
│   ├── interview/
│   │   ├── PromptInput.jsx        # 3단계 프롬프트 입력
│   │   ├── RealTimeQA.jsx         # 실시간 질문/답변
│   │   ├── VideoRecorder.jsx      # 3단계 비디오 녹화
│   │   ├── EmotionOverlay.jsx     # 실시간 감정 표시
│   │   └── TimelineFeedback.jsx   # 타임라인별 피드백
│   └── review/
│       └── LearningReview.jsx     # 학습 복기
├── services/
│   ├── api.js                     # Axios 기반 API 클라이언트
│   ├── speechRecognition.js       # 음성인식 (서버로 전송)
│   ├── videoSync.js               # 비디오-질문 동기화
│   └── videoCapture.js            # 3단계 비디오 프레임 캡처
├── hooks/
│   ├── useAuth.js                 # 인증 상태 관리
│   ├── useSpeechRecognition.js    # 음성인식 훅
│   └── useLearning.js             # 학습 상태 관리
└── store/
    ├── authSlice.js               # Redux: 사용자 인증
    └── learningSlice.js           # Redux: 학습 진도
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

### 2. Backend Layer (Spring Boot)

#### 주요 책임
- RESTful API 제공
- 비디오 스트리밍 (Adaptive Bitrate)
- Lecture 및 Component 관리
- 3단계 고급 음성인식 처리 (KoSpeech 연동)
- AI 기반 질문 생성 및 피드백

#### 패키지 구조

```
com.seungjz.edutech/
├── config/
│   ├── SecurityConfig.java        # Spring Security + OAuth2
│   ├── WebConfig.java             # CORS, Interceptor
│   └── S3Config.java              # AWS S3 설정
├── controller/
│   ├── AuthController.java        # 로그인/회원가입
│   ├── LectureController.java     # 학습 컨텐츠 CRUD
│   ├── LearningController.java    # 학습 진행 API
│   └── InterviewController.java   # 3단계 실시간 면접
├── service/
│   ├── LectureService.java        # Lecture 비즈니스 로직
│   ├── VideoStreamService.java    # 비디오 스트리밍
│   ├── SpeechService.java         # 음성인식 오케스트레이션
│   ├── FeedbackService.java       # AI 피드백 생성
│   └── ProgressService.java       # 학습 진도 관리
├── repository/
│   ├── UserRepository.java
│   ├── LectureRepository.java
│   ├── ComponentRepository.java
│   └── ProgressRepository.java
├── domain/
│   ├── User.java
│   ├── Lecture.java               # 학습 컨텐츠
│   ├── Component.java             # Lecture 구성요소
│   ├── Progress.java              # 사용자 진도
│   └── Feedback.java              # AI 피드백
└── external/
    ├── KoSpeechClient.java        # Python ML 서비스 연동
    └── OpenAIClient.java          # GPT API 연동
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

#### 구조

```
ml-services/
├── kospeech/
│   ├── app.py                 # Flask API 서버
│   ├── model_loader.py        # KoSpeech 모델 로드
│   └── transcribe.py          # 음성→텍스트 변환
├── feedback/
│   ├── analyzer.py            # 문맥 분석
│   └── keyword_extractor.py   # 키워드 추출
└── requirements.txt
```

#### KoSpeech API 서버

```python
# ml-services/kospeech/app.py
from flask import Flask, request, jsonify
from model_loader import load_kospeech_model

app = Flask(__name__)
model = load_kospeech_model()

@app.route('/transcribe', methods=['POST'])
def transcribe():
    """
    음성 파일을 받아 텍스트로 변환
    """
    audio_file = request.files['audio']

    # KoSpeech 모델로 변환
    transcript = model.transcribe(audio_file)

    return jsonify({
        'transcript': transcript,
        'confidence': 0.95
    })

@app.route('/analyze', methods=['POST'])
def analyze():
    """
    답변 분석: 키워드 누락, 문맥 어색함 탐지
    """
    data = request.json
    question = data['question']
    answer = data['answer']

    # 키워드 추출
    expected_keywords = extract_keywords(question)
    user_keywords = extract_keywords(answer)
    missing_keywords = set(expected_keywords) - set(user_keywords)

    # 문맥 분석 (GPT API 활용)
    context_score = analyze_context(answer)

    return jsonify({
        'missing_keywords': list(missing_keywords),
        'context_score': context_score,
        'is_awkward': context_score < 0.7
    })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
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
