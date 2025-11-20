# Nawbio API

나우바이오 백엔드 API 서버

## 기술 스택

- Java 17
- Spring Boot 3.2.0
- PostgreSQL 15
- JWT Authentication
- Swagger/OpenAPI

## 주요 기능

### 1. 제품 관리 (Products)
- ✅ 제품 목록 조회 (카테고리 필터링)
- ✅ 제품 상세 조회
- ✅ 제품 생성 (관리자)
- ✅ 제품 수정 (관리자)
- ✅ 제품 삭제 (관리자)

### 2. 뉴스/공지사항 (News)
- ✅ 뉴스 목록 조회 (카테고리, 발행 상태 필터링)
- ✅ 뉴스 상세 조회 (조회수 자동 증가)
- ✅ 뉴스 생성 (관리자)
- ✅ 뉴스 수정 (관리자)
- ✅ 뉴스 발행/비발행 토글 (관리자)
- ✅ 뉴스 삭제 (관리자)

### 3. 관리자 인증 (Authentication)
- ✅ 로그인 (JWT 토큰 발급)
- ✅ JWT 기반 인증

### 4. 트래픽 분석 (Analytics)
- ⏳ 향후 구현 예정 (현재 빈 클래스)

## 프로젝트 구조

```
nawbio-api/
├── src/main/java/com/nawbio/api/
│   ├── NawbioApiApplication.java
│   ├── common/
│   │   ├── dto/
│   │   │   └── ApiResponse.java
│   │   └── exception/
│   │       ├── ResourceNotFoundException.java
│   │       └── GlobalExceptionHandler.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   ├── CorsConfig.java
│   │   └── SwaggerConfig.java
│   ├── security/
│   │   ├── JwtUtil.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── CustomUserDetailsService.java
│   └── domain/
│       ├── product/
│       │   ├── Product.java
│       │   ├── ProductRepository.java
│       │   ├── ProductService.java
│       │   ├── ProductController.java
│       │   └── dto/
│       ├── news/
│       │   ├── News.java
│       │   ├── NewsRepository.java
│       │   ├── NewsService.java
│       │   ├── NewsController.java
│       │   └── dto/
│       ├── admin/
│       │   ├── Admin.java
│       │   ├── AdminRepository.java
│       │   ├── AdminService.java
│       │   ├── AuthController.java
│       │   └── dto/
│       └── analytics/
│           └── AnalyticsController.java
└── src/main/resources/
    └── application.yml
```

## 시작하기

### 1. 데이터베이스 설정

PostgreSQL을 설치하고 데이터베이스를 생성합니다:

```sql
CREATE DATABASE nawbio;
```

`schema.sql` 파일을 실행하여 테이블을 생성합니다:

```bash
psql -U postgres -d nawbio -f schema.sql
```

### 2. 애플리케이션 설정

`src/main/resources/application.yml` 파일에서 데이터베이스 연결 정보를 수정합니다:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/nawbio
    username: postgres
    password: your-password
```

### 3. 애플리케이션 실행

```bash
# Gradle 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

또는 IntelliJ IDEA에서 `NawbioApiApplication.java`를 실행합니다.

### 4. 초기 관리자 계정 생성

애플리케이션이 실행되면 `init-admin.sql` 스크립트를 실행하여 초기 관리자 계정을 생성합니다:

```bash
psql -U postgres -d nawbio -f init-admin.sql
```

기본 관리자 계정:
- Username: `admin`
- Password: `admin123`

⚠️ **프로덕션 환경에서는 반드시 비밀번호를 변경하세요!**

## API 문서

애플리케이션 실행 후 Swagger UI에서 API 문서를 확인할 수 있습니다:

```
http://localhost:8080/api/swagger-ui.html
```

## API 엔드포인트

### 인증

- `POST /api/auth/login` - 로그인

### 제품 (Public)

- `GET /api/products` - 제품 목록 조회
- `GET /api/products/{id}` - 제품 상세 조회

### 제품 (Admin - 인증 필요)

- `POST /api/products` - 제품 생성
- `PUT /api/products/{id}` - 제품 수정
- `DELETE /api/products/{id}` - 제품 삭제

### 뉴스 (Public)

- `GET /api/news` - 뉴스 목록 조회
- `GET /api/news/{id}` - 뉴스 상세 조회

### 뉴스 (Admin - 인증 필요)

- `POST /api/news` - 뉴스 생성
- `PUT /api/news/{id}` - 뉴스 수정
- `PATCH /api/news/{id}/publish` - 발행/비발행 토글
- `DELETE /api/news/{id}` - 뉴스 삭제

### 트래픽 분석

- `GET /api/analytics/overview` - 대시보드 요약 (placeholder)
- `GET /api/analytics/realtime` - 실시간 방문자 (placeholder)

## 인증 사용법

### 1. 로그인

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

응답:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "admin",
    "email": "admin@nawbio.com"
  }
}
```

### 2. 인증이 필요한 API 호출

발급받은 토큰을 `Authorization` 헤더에 포함합니다:

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "nameKo": "뉴폴",
    "nameEn": "NewPol",
    "descriptionKo": "천연 항산화제",
    "category": "전체",
    "imageUrl": "https://example.com/image.jpg"
  }'
```

## 개발 환경

### 필수 요구사항

- Java 17 이상
- PostgreSQL 15 이상
- Gradle 7.x 이상

### 권장 IDE

- IntelliJ IDEA
- VS Code (Spring Boot Extension Pack)

## 배포

### Docker를 사용한 배포 (예정)

```bash
docker-compose up -d
```

## 라이선스

© 2024 나우바이오(Nawbio). All rights reserved.
