# 개발 계획 - SeungJz 해커톤 애플리케이션

## 개발 우선순위 원칙

1. **Vertical Slice 방식**: 각 Phase마다 Frontend → Backend → Database를 완성
2. **테스트 가능성**: 각 단계마다 실제로 작동하는 기능 완성
3. **의존성 최소화**: ML 서비스는 가장 마지막에 통합
4. **점진적 확장**: 간단한 기능부터 복잡한 기능 순서로

---

## Phase 1: 인증 및 기본 인프라 (Day 1, 8시간)

### 목표
사용자가 로그인하고 메인 페이지를 볼 수 있다.

### 1.1 Database 설정 (1시간)

**작업**:
- [ ] PostgreSQL 초기화 스크립트 작성
- [ ] `users` 테이블만 생성
- [ ] Docker Compose로 DB 실행 확인

**산출물**:
```sql
-- database/init.sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    provider VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    profile_image_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**검증**:
```bash
docker-compose up postgres
psql -h localhost -U postgres -d edutech
\dt  # 테이블 확인
```

---

### 1.2 Backend - 소셜 로그인 (3시간)

**작업**:
- [ ] Spring Boot 프로젝트 초기화
- [ ] Spring Security + OAuth2 설정
- [ ] Google OAuth 연동
- [ ] JWT 토큰 발급
- [ ] `/api/auth/me` 엔드포인트 구현

**파일 구조**:
```
backend/
├── src/main/java/com/seungjz/edutech/
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   └── JwtConfig.java
│   ├── controller/
│   │   └── AuthController.java
│   ├── service/
│   │   ├── AuthService.java
│   │   └── JwtService.java
│   ├── domain/
│   │   └── User.java
│   └── repository/
│       └── UserRepository.java
└── application.yml
```

**검증**:
```bash
# 1. 서버 실행
./gradlew bootRun

# 2. Google 로그인 테스트
curl http://localhost:8080/api/auth/oauth2/authorization/google

# 3. 로그인 후 토큰으로 사용자 정보 조회
curl -H "Authorization: Bearer {token}" \
     http://localhost:8080/api/auth/me
```

---

### 1.3 Frontend - 로그인 페이지 (3시간)

**작업**:
- [ ] React 프로젝트 초기화 (Vite)
- [ ] 로그인 페이지 UI
- [ ] Google 소셜 로그인 버튼
- [ ] JWT 토큰 저장 (localStorage)
- [ ] 보호된 라우트 설정

**파일 구조**:
```
frontend/
├── src/
│   ├── pages/
│   │   ├── LoginPage.jsx
│   │   └── HomePage.jsx
│   ├── components/
│   │   └── SocialLoginButton.jsx
│   ├── services/
│   │   └── authApi.js
│   ├── hooks/
│   │   └── useAuth.js
│   └── App.jsx
└── package.json
```

**검증**:
```bash
npm run dev
# 브라우저에서 http://localhost:3000
# Google 로그인 클릭 → 로그인 → 홈 화면 이동
```

---

### 1.4 통합 테스트 (1시간)

**시나리오**:
1. Frontend 접속
2. Google 로그인 클릭
3. Google 계정 선택
4. 홈 화면 도착
5. 사용자 정보 표시

**성공 기준**:
- [ ] 로그인 성공 시 JWT 토큰 발급
- [ ] 토큰으로 사용자 정보 조회 가능
- [ ] 로그아웃 후 보호된 페이지 접근 불가

---

## Phase 2: 1-2단계 학습 기능 (Day 1-2, 16시간)

### 목표
사용자가 학습 컨텐츠를 선택하고, 비디오를 보고, 음성으로 답변할 수 있다.

### 2.1 Database - 학습 테이블 (1시간)

**작업**:
- [ ] `lectures` 테이블 생성
- [ ] `components` 테이블 생성
- [ ] `user_progress` 테이블 생성
- [ ] `answer_history` 테이블 생성
- [ ] 샘플 데이터 1개 추가 (seed.sql)

**산출물**:
```sql
-- database/seed.sql
INSERT INTO lectures (title, level, video_url, duration_sec) VALUES
('BTS로 배우는 한국어', 1, 'https://s3.../sample.mp4', 180);

INSERT INTO components (lecture_id, type, sequence, content) VALUES
(1, 'VIDEO_SEGMENT', 0, '{"startTime": 0, "endTime": 30}'),
(1, 'QUESTION', 1, '{"questionText": "방금 들은 인사말은?"}'),
(1, 'VOICE_ANSWER', 2, '{"expectedAnswer": "안녕하세요"}');
```

**검증**:
```sql
SELECT * FROM lectures;
SELECT * FROM components WHERE lecture_id = 1 ORDER BY sequence;
```

---

### 2.2 Backend - 학습 API (4시간)

**작업**:
- [ ] Lecture CRUD API
- [ ] Component 조회 API
- [ ] 학습 시작/진도 저장 API
- [ ] 답변 제출 API (임시로 텍스트 비교만)

**구현 순서**:
1. **Lecture 목록 조회** (`GET /api/lectures`)
2. **Lecture 상세 조회** (`GET /api/lectures/{id}`)
3. **학습 시작** (`POST /api/learning/start`)
4. **답변 제출** (`POST /api/learning/submit-answer`)
   - 초기에는 단순 텍스트 비교 (유사도 70% 이상 정답)
   - STT는 나중에 통합

**검증**:
```bash
# 1. 강의 목록
curl http://localhost:8080/api/lectures

# 2. 강의 상세
curl http://localhost:8080/api/lectures/1

# 3. 학습 시작
curl -X POST http://localhost:8080/api/learning/start \
     -H "Content-Type: application/json" \
     -d '{"lectureId": 1}'

# 4. 답변 제출 (텍스트)
curl -X POST http://localhost:8080/api/learning/submit-answer \
     -F "progressId=1" \
     -F "componentId=3" \
     -F "userAnswer=안녕하세요"
```

---

### 2.3 Frontend - 학습 컨텐츠 목록 (3시간)

**작업**:
- [ ] 학습 컨텐츠 목록 페이지
- [ ] 카드 형태 레이아웃
- [ ] 레벨별 필터
- [ ] 진도율 표시

**검증**:
- [ ] 로그인 후 학습 목록 표시
- [ ] 카드 클릭 시 상세 페이지 이동

---

### 2.4 Frontend - 비디오 학습 페이지 (5시간)

**작업**:
- [ ] Video.js 통합
- [ ] 비디오 재생
- [ ] 특정 시점에 일시정지
- [ ] 질문 오버레이 표시
- [ ] 음성 녹음 버튼 (Web Audio API)
- [ ] 녹음 파일 서버 전송

**주요 컴포넌트**:
```jsx
// VideoPlayer.jsx
- 비디오 재생
- 시간별 이벤트 처리

// QuestionOverlay.jsx
- 질문 표시
- 음성 녹음 버튼

// VoiceRecorder.jsx
- 녹음 시작/중지
- Blob 생성
```

**검증**:
- [ ] 비디오 재생
- [ ] 30초 시점에 자동 일시정지
- [ ] 질문 표시
- [ ] 음성 녹음 및 전송
- [ ] 정답 피드백 표시

---

### 2.5 통합 테스트 (3시간)

**전체 플로우**:
1. 로그인
2. 학습 목록 조회
3. 학습 선택
4. 비디오 시청
5. 질문 응답 (음성)
6. 피드백 확인
7. 다음 질문 진행
8. 학습 완료

**성공 기준**:
- [ ] 전체 플로우 에러 없이 완료
- [ ] 진도가 DB에 저장됨
- [ ] 다시 접속 시 이어서 학습 가능

---

## Phase 3: STT 통합 (Day 2, 4시간)

### 목표
음성 답변을 텍스트로 변환하여 자동 채점

### 3.1 OpenAI Whisper API 통합 (2시간)

**작업**:
- [ ] Backend에 Whisper API 클라이언트 추가
- [ ] 답변 제출 API에 STT 통합
- [ ] 유사도 계산 로직 추가

**구현**:
```java
@Service
public class SpeechService {
    public String transcribe(MultipartFile audio) {
        // OpenAI Whisper API 호출
        return "안녕하세요";
    }

    public double calculateSimilarity(String answer, String expected) {
        // Levenshtein Distance 또는 간단한 문자열 매칭
        return 0.95;
    }
}
```

**검증**:
```bash
curl -X POST http://localhost:8080/api/learning/submit-answer \
     -F "progressId=1" \
     -F "componentId=3" \
     -F "audio=@test.webm"

# Response: {"isCorrect": true, "transcript": "안녕하세요"}
```

---

### 3.2 Frontend 연동 (1시간)

**작업**:
- [ ] 음성 녹음 → Blob → FormData
- [ ] 서버 전송
- [ ] 응답 처리

---

### 3.3 테스트 (1시간)

**시나리오**:
- [ ] 정확한 발음 → 정답
- [ ] 비슷한 발음 → 정답
- [ ] 틀린 답변 → 오답

---

## Phase 4: 3단계 면접 기능 (Day 3, 8시간)

### 목표
실시간 면접 세션 + 질문 생성 (감정 분석 제외)

### 4.1 Database - 면접 테이블 (30분)

**작업**:
- [ ] `interview_sessions` 테이블
- [ ] `interview_qa_pairs` 테이블
- [ ] `interview_feedback` 테이블

---

### 4.2 Backend - 면접 API (3시간)

**작업**:
- [ ] 면접 세션 시작 API
- [ ] GPT로 초기 질문 생성
- [ ] 답변 제출 → STT → GPT로 다음 질문 생성
- [ ] 3분 타이머
- [ ] 세션 종료 및 피드백 생성

**구현**:
```java
@Service
public class InterviewService {
    public InterviewSession startSession(String prompt) {
        // GPT로 초기 질문 생성
        String question = gptClient.generateQuestion(prompt);
        return session;
    }

    public QAPair submitAnswer(Long sessionId, MultipartFile audio) {
        // STT
        String transcript = speechService.transcribe(audio);

        // GPT로 분석 + 다음 질문
        String nextQuestion = gptClient.analyzeAndGenerateNext(transcript);

        return qaPair;
    }
}
```

---

### 4.3 Frontend - 면접 페이지 (3시간)

**작업**:
- [ ] 프롬프트 입력 페이지
- [ ] 면접 화면 (카메라 + 질문 표시)
- [ ] 3분 타이머
- [ ] 음성 녹음
- [ ] 실시간 질문 업데이트

---

### 4.4 통합 테스트 (1.5시간)

**플로우**:
1. 프롬프트 입력
2. 면접 시작
3. 질문 응답
4. 다음 질문 자동 생성
5. 3분 종료
6. 피드백 확인

---

## Phase 5: DeepFace 감정 분석 (Day 3, 4시간)

### 목표
면접 중 감정 분석 추가

### 5.1 ML Service 구축 (2시간)

**작업**:
- [ ] `ml-services/emotion/` 디렉토리 생성
- [ ] Flask + DeepFace 구현
- [ ] Docker 이미지 빌드

---

### 5.2 Backend 연동 (1시간)

**작업**:
- [ ] EmotionAnalysisClient 구현
- [ ] 면접 중 1초마다 프레임 전송
- [ ] DB에 감정 데이터 저장

---

### 5.3 Frontend 연동 (1시간)

**작업**:
- [ ] 비디오 프레임 캡처
- [ ] 서버 전송
- [ ] 실시간 감정 표시 (옵션)

---

## Phase 6: 피드백 고도화 (Day 3, 2시간)

### 목표
타임라인별 피드백 제공

**작업**:
- [ ] 키워드 누락 탐지 (GPT)
- [ ] 감정 타임라인 그래프
- [ ] 시선 처리 점수
- [ ] 개선 제안

---

## 해커톤 최종 체크리스트

### 필수 기능 (MVP)
- [x] Phase 1: 로그인
- [ ] Phase 2: 1-2단계 학습
- [ ] Phase 3: STT 통합
- [ ] Phase 4: 3단계 면접 (감정 분석 제외)

### 추가 기능 (Nice to Have)
- [ ] Phase 5: 감정 분석
- [ ] Phase 6: 고급 피드백
- [ ] Kakao 로그인
- [ ] 학습 복기 기능

---

## 시간 배분 요약

| Phase | 시간 | 누적 시간 | 우선순위 |
|-------|------|----------|---------|
| Phase 1: 인증 | 8시간 | 8시간 | 최고 |
| Phase 2: 학습 | 16시간 | 24시간 | 최고 |
| Phase 3: STT | 4시간 | 28시간 | 높음 |
| Phase 4: 면접 | 8시간 | 36시간 | 높음 |
| Phase 5: 감정 | 4시간 | 40시간 | 중간 |
| Phase 6: 피드백 | 2시간 | 42시간 | 낮음 |

**해커톤 3일 (72시간) 기준**:
- 실제 개발 시간: 42시간
- 여유 시간: 30시간 (디버깅, 테스트, 발표 준비)

---

## 일일 목표

### Day 1 (24시간 중 작업 12시간)
- ✅ Phase 1 완료 (로그인)
- ✅ Phase 2 시작 (학습 컨텐츠 목록까지)

### Day 2 (작업 14시간)
- ✅ Phase 2 완료 (학습 기능 전체)
- ✅ Phase 3 완료 (STT 통합)
- ✅ Phase 4 시작 (면접 세션)

### Day 3 (작업 16시간)
- ✅ Phase 4 완료 (면접 기능)
- ✅ Phase 5 완료 (감정 분석)
- ✅ 통합 테스트 및 발표 준비

---

## 다음 단계

1. **지금 바로**: `database/init.sql` 작성
2. **그 다음**: Spring Boot 프로젝트 초기화
3. **그 다음**: React 프로젝트 초기화
