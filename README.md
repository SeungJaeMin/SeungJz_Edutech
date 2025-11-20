# SeungJz Edutech - 한국어 발화 학습 플랫폼

초개인화 트렌드에 맞춘 **사용자 수준별 한국어 발화 학습 애플리케이션**

## 📋 프로젝트 개요

외국인 학습자를 위한 단계별 한국어 회화 학습 플랫폼으로, K-Pop부터 비즈니스 면접까지 실전 발화 연습을 제공합니다.

### 핵심 기능

- **3단계 학습 시스템**
  - 1단계: K-Pop 콘텐츠 기반 친근한 학습
  - 2단계: 드라마 상황별 발화 학습
  - 3단계: 실시간 면접 시뮬레이션 및 피드백

- **실시간 음성인식 및 발음 교정**
- **AI 기반 문맥 분석 및 키워드 누락 탐지**
- **개인화된 학습 진도 관리**

## 🛠 기술 스택

### Frontend (구현 예정 - Figma MCP)
- React (Mobile-First)
- Web Speech API / 경량 STT 모델
- Video.js (비디오 재생)

### Backend
- **Spring Boot 3.2** + Java 17
- **PostgreSQL** (JPA/Hibernate)
- **JWT Authentication** (소셜 로그인)
- **AWS S3** (비디오 저장소)
- **KoSpeech** (한국어 STT)
- **GPT API** (질문 생성 및 피드백)

### Infrastructure
- **AWS EC2** (배포 서버)
- **Nginx** (Reverse Proxy + SSL)
- **GitHub Actions** (CI/CD 자동화)
- **Docker** (로컬 개발 환경)

## 🚀 빠른 시작

### 필수 요구사항

- Node.js 18+
- Java 17+
- PostgreSQL 14+
- Python 3.8+ (음성인식 모델)

### 로컬 개발 환경

#### 1. 환경변수 설정

```bash
# .env 파일 생성
cp .env.example .env

# 필요한 값 입력
# - DB_PASSWORD
# - JWT_SECRET
# - AWS_ACCESS_KEY, AWS_SECRET_KEY
# - S3_BUCKET_NAME
```

#### 2. 데이터베이스 초기화

```bash
# PostgreSQL 실행
psql -U postgres

# 데이터베이스 생성
CREATE DATABASE edutech;
\q
```

#### 3. 백엔드 실행

```bash
cd backend

# Gradle 빌드 및 실행
./gradlew bootRun
```

API 서버: `http://localhost:8080/api`
Swagger UI: `http://localhost:8080/api/swagger-ui.html`

#### 4. 프론트엔드 (구현 예정)

```bash
cd frontend
npm install
npm run dev
```

## 📁 프로젝트 구조

```
SeungJz_Edutech/
├── backend/                # Spring Boot API
│   ├── src/main/
│   │   ├── java/com/seungjz/edutech/
│   │   │   ├── controller/     # REST API 엔드포인트
│   │   │   ├── service/        # 비즈니스 로직
│   │   │   ├── domain/         # JPA Entity
│   │   │   ├── repository/     # DB 액세스
│   │   │   ├── security/       # JWT 인증
│   │   │   └── config/         # 설정
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-prod.yml
│   ├── build.gradle
│   └── deployment/             # 배포 설정
│
├── frontend/               # React Web App (Figma MCP로 구현 예정)
│   └── (TBD)
│
├── ml-models/              # 음성인식 모델 (KoSpeech)
│   └── (TBD)
│
├── scripts/                # 배포 스크립트
│   ├── deploy.sh
│   ├── deploy-backend.sh
│   └── deploy.config.example
│
├── .github/workflows/      # CI/CD
│   ├── deploy.yml          # 프로덕션 배포
│   ├── deploy-backend.yml  # 백엔드만 배포
│   └── build-test.yml      # CI 테스트
│
├── Docs/                   # 프로젝트 문서
│   ├── API_SPECIFICATION.md
│   ├── ARCHITECTURE.md
│   ├── DATABASE_ERD.md
│   ├── DEVELOPMENT_PLAN.md
│   └── FIGMA_MCP_GUIDE.md
│
├── docker-compose.yml      # Docker 개발 환경
└── README.md
```

## 📖 API 명세서

전체 API 명세는 [API_SPECIFICATION.md](./Docs/API_SPECIFICATION.md) 참조

### 주요 엔드포인트

#### 인증
- `GET /api/auth/oauth2/authorization/google` - Google 로그인
- `GET /api/auth/oauth2/authorization/kakao` - Kakao 로그인
- `GET /api/auth/me` - 현재 사용자 정보

#### 학습 컨텐츠 (1-2단계)
- `GET /api/contents` - 컨텐츠 리스트
- `GET /api/contents/{id}` - 컨텐츠 상세
- `POST /api/progress` - 학습 진행 저장
- `POST /api/answers` - 답변 제출 및 검증

#### 면접 (3단계)
- `POST /api/interview/sessions` - 면접 세션 생성
- `GET /api/interview/sessions/{id}/questions` - 실시간 질문 조회
- `POST /api/interview/sessions/{id}/answers` - 답변 제출
- `GET /api/interview/sessions/{id}/feedback` - AI 피드백

## 🚢 배포

### GitHub Actions CI/CD (권장)

1. **GitHub Secrets 설정**
   ```
   EC2_HOST: EC2 Public IP
   EC2_USERNAME: ec2-user
   EC2_SSH_KEY: SSH Private Key
   ```

2. **배포 실행**
   ```bash
   git push origin main  # 자동 배포 트리거
   ```

자세한 내용: [Continuous Deployment Guide](./Docs/continuous-deployment-guide.md)

### 수동 배포 스크립트

```bash
# 1. 설정 파일 생성
cp scripts/deploy.config.example scripts/deploy.config

# 2. EC2 정보 입력
nano scripts/deploy.config

# 3. 배포 실행
chmod +x scripts/*.sh
./scripts/deploy.sh              # 전체 배포
./scripts/deploy-backend.sh      # 백엔드만
```

## 🧪 테스트

### 백엔드 테스트

```bash
cd backend
./gradlew test
```

### 프론트엔드 테스트 (구현 예정)

```bash
cd frontend
npm test
```

## 📊 주요 사용 시나리오

1. **소셜 로그인** (Google/Kakao)
2. **학습 컨텐츠 선택** (1-2단계)
3. **비디오 학습**
   - 비디오 재생 → 질문 출제 → 음성 답변 → 즉시 피드백
4. **실시간 면접 (3단계)**
   - 프롬프트 입력 → 3분간 5W1H 질문 → AI 피드백
5. **학습 복기** (타임라인별 오답 분석)

## 🔐 보안

- JWT 기반 소셜 로그인 인증
- HTTPS (Let's Encrypt)
- CORS 보호
- SQL Injection 방지 (JPA)
- 환경변수 기반 시크릿 관리

## 📊 모니터링

### 백엔드 로그

```bash
ssh ec2-user@your-ec2-ip
sudo journalctl -u seungjz-edutech-api -f
```

### Nginx 로그

```bash
sudo tail -f /var/log/nginx/access.log
sudo tail -f /var/log/nginx/error.log
```

## 📚 문서

- [아키텍처 설계](./Docs/ARCHITECTURE.md)
- [API 명세서](./Docs/API_SPECIFICATION.md)
- [데이터베이스 ERD](./Docs/DATABASE_ERD.md)
- [개발 계획](./Docs/DEVELOPMENT_PLAN.md)
- [Figma MCP 가이드](./Docs/FIGMA_MCP_GUIDE.md)

## 🔄 개발 워크플로우

```bash
# 1. Feature 브랜치 생성
git checkout -b feature/new-feature

# 2. 개발 및 커밋
git add .
git commit -m "feat: 새로운 기능 추가"

# 3. develop 브랜치에 병합 (CI 테스트)
git checkout develop
git merge feature/new-feature
git push origin develop

# 4. main 브랜치에 병합 (자동 배포)
git checkout main
git merge develop
git push origin main  # GitHub Actions 자동 배포 실행
```

## 📝 버전 관리

```bash
git tag -a v1.0.0 -m "Version 1.0.0 - Initial Release"
git push origin v1.0.0
```

## 🤝 기여자

SeungJz Edutech Team

## 📄 라이선스

MIT License - Copyright © 2025
