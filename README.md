# SeungJz 해커톤 애플리케이션

초개인화 트렌드에 맞춘 **사용자 수준별 한국어 발화 학습 애플리케이션**

## 프로젝트 개요

외국인 학습자를 위한 단계별 한국어 회화 학습 플랫폼으로, K-Pop부터 비즈니스 면접까지 실전 발화 연습을 제공합니다.

### 핵심 기능

- **3단계 학습 시스템**
  - 1단계: K-Pop 콘텐츠 기반 친근한 학습
  - 2단계: 드라마 상황별 발화 학습
  - 3단계: 실시간 면접 시뮬레이션 및 피드백

- **실시간 음성인식 및 발음 교정**
- **AI 기반 문맥 분석 및 키워드 누락 탐지**
- **개인화된 학습 진도 관리**

## 기술 스택

### Frontend
- React (Mobile-First)
- Web Speech API / 경량 STT 모델
- Video.js (비디오 재생)

### Backend
- Spring Boot
- PostgreSQL
- KoSpeech (한국어 STT)
- GPT API (질문 생성 및 피드백)

### Infrastructure
- MP4 스트리밍 서버
- AWS S3 (비디오 저장소)

## 시작하기

### 필수 요구사항

- Node.js 18+
- Java 17+
- PostgreSQL 14+
- Python 3.8+ (음성인식 모델)

### 설치 및 실행

```bash
# 저장소 클론
git clone [repository-url]
cd SeungJz_Edutech

# Frontend 설정
cd frontend
npm install
npm run dev

# Backend 설정
cd ../backend
./gradlew bootRun

# Database 초기화
psql -U postgres -f init.sql
```

## 프로젝트 구조

```
SeungJz_Edutech/
├── frontend/          # React 모바일 애플리케이션
├── backend/           # Spring Boot API 서버
├── ml-models/         # 음성인식 모델 (KoSpeech)
├── Docs/              # 프로젝트 문서
│   ├── README.md
│   └── ARCHITECTURE.md
└── database/          # DB 스키마 및 마이그레이션
```

## 주요 사용 시나리오

1. **소셜 로그인** (Google/Kakao)
2. **학습 컨텐츠 리스트 조회**
3. **비디오 기반 학습**
   - 비디오 재생 → 질문 출제 → 음성 답변 → 즉시 피드백
4. **3단계 실시간 면접**
   - 사용자 프롬프트 입력 → 3분간 5W1H 질문 → AI 피드백
5. **학습 복기** (타임라인별 오답 분석)

## 문서

- [아키텍처 설계](./Docs/ARCHITECTURE.md)
- [API 명세서](./Docs/API.md) (TBD)
- [데이터베이스 스키마](./Docs/DATABASE.md) (TBD)

## 라이선스

MIT License

## 기여자

SeungJz Edutech Team
