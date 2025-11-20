# Nawbio API 설치 및 실행 가이드

## 1. PostgreSQL 설치

### Windows 환경

1. **PostgreSQL 다운로드**
   - 공식 사이트: https://www.postgresql.org/download/windows/
   - 또는 직접 링크: https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
   - PostgreSQL 15 이상 버전 다운로드

2. **설치 실행**
   ```
   - 다운로드한 설치 파일 실행
   - 설치 경로: 기본값 (C:\Program Files\PostgreSQL\15)
   - Port: 5432 (기본값)
   - Password: 설치 시 입력한 비밀번호 기억하기 (예: postgres)
   - Locale: Korean, Korea
   ```

3. **설치 확인**
   ```bash
   # 명령 프롬프트 또는 PowerShell에서
   psql --version
   ```

   출력 예시:
   ```
   psql (PostgreSQL) 15.x
   ```

### macOS/Linux 환경

```bash
# macOS (Homebrew)
brew install postgresql@15
brew services start postgresql@15

# Ubuntu/Debian
sudo apt update
sudo apt install postgresql postgresql-contrib

# CentOS/RHEL
sudo yum install postgresql-server postgresql-contrib
sudo postgresql-setup initdb
sudo systemctl start postgresql
```

---

## 2. 데이터베이스 생성

### 방법 1: psql 커맨드라인 (추천)

```bash
# PostgreSQL 접속
psql -U postgres

# 데이터베이스 생성
CREATE DATABASE nawbio;

# 생성 확인
\l

# 종료
\q
```

### 방법 2: pgAdmin (GUI)

1. pgAdmin 실행 (PostgreSQL 설치 시 함께 설치됨)
2. 왼쪽 트리에서 `PostgreSQL 15` > `Databases` 우클릭
3. `Create` > `Database...` 클릭
4. Database name: `nawbio` 입력
5. `Save` 클릭

---

## 3. 테이블 생성

### nawbio-api 폴더로 이동

```bash
cd E:\Workspace_Other\Woosung\nawbio-api
```

### 스키마 파일 실행

```bash
# Windows (명령 프롬프트)
psql -U postgres -d nawbio -f schema.sql

# 또는 PowerShell에서
& "C:\Program Files\PostgreSQL\15\bin\psql.exe" -U postgres -d nawbio -f schema.sql
```

비밀번호 입력하면 테이블이 생성됩니다.

### 테이블 생성 확인

```bash
psql -U postgres -d nawbio
```

```sql
-- 생성된 테이블 확인
\dt

-- 테이블 구조 확인
\d products
\d news
\d admins

-- 종료
\q
```

---

## 4. application.yml 설정 확인

`nawbio-api/src/main/resources/application.yml` 파일에서 데이터베이스 연결 정보를 확인/수정합니다:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/nawbio
    username: postgres
    password: tnwjdtkgkd1 
```

---

## 5. Java 17 설치 확인

```bash
java -version
```

출력 예시:
```
java version "17.0.x"
Java(TM) SE Runtime Environment
```

Java 17이 없다면:
- Oracle JDK: https://www.oracle.com/java/technologies/downloads/#java17
- OpenJDK: https://adoptium.net/

---

## 6. 백엔드 실행

### Gradle Wrapper 생성 (처음 한 번만)

```bash
cd nawbio-api

# Gradle이 설치되어 있다면
gradle wrapper

# 또는 IntelliJ에서 자동 생성됨
```

### 애플리케이션 실행

#### 방법 1: Gradle 사용

```bash
# Windows
gradlew.bat bootRun

# macOS/Linux
./gradlew bootRun
```

#### 방법 2: IntelliJ IDEA 사용

1. `nawbio-api` 폴더를 IntelliJ에서 Open
2. `src/main/java/com/nawbio/api/NawbioApiApplication.java` 열기
3. 클래스 옆의 ▶️ 버튼 클릭 또는 `Shift + F10`

### 실행 확인

콘솔에서 다음과 같은 메시지가 보이면 성공:

```
Started NawbioApiApplication in X.XXX seconds
```

---

## 7. API 테스트

### Swagger UI로 테스트 (추천)

브라우저에서 접속:
```
http://localhost:8080/api/swagger-ui.html
```

### 주요 엔드포인트

1. **제품 목록 조회** (인증 불필요)
   ```
   GET http://localhost:8080/api/products
   ```

2. **뉴스 목록 조회** (인증 불필요)
   ```
   GET http://localhost:8080/api/news
   ```

3. **관리자 로그인** (인증 불필요)
   ```
   POST http://localhost:8080/api/auth/login
   Body:
   {
     "username": "admin",
     "password": "admin123"
   }
   ```

---

## 8. 초기 관리자 계정 생성

### 방법 1: SQL 직접 실행 (간단)

```bash
psql -U postgres -d nawbio
```

```sql
-- BCrypt로 암호화된 비밀번호로 관리자 생성
INSERT INTO admins (username, password, email, is_active, created_at)
VALUES (
    'admin',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',  -- 'admin123'
    'admin@nawbio.com',
    true,
    NOW()
);
```

### 방법 2: 애플리케이션에서 생성

`AdminService`를 이용한 계정 생성 기능을 추가하거나, 간단한 테스트 컨트롤러를 만들어서 실행:

```java
// 임시 테스트 코드 (개발용)
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/create-admin")
    public String createAdmin() {
        adminService.createAdmin("admin", "admin123", "admin@nawbio.com");
        return "Admin created successfully";
    }
}
```

---

## 9. 로그인 테스트

### Swagger에서 테스트

1. Swagger UI 접속: http://localhost:8080/api/swagger-ui.html
2. `auth-controller` 섹션 찾기
3. `POST /auth/login` 클릭
4. `Try it out` 클릭
5. Request body 입력:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
6. `Execute` 클릭
7. 응답에서 `token` 값 복사

### curl로 테스트

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

성공 응답:
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

---

## 10. 인증이 필요한 API 테스트

### Swagger에서 인증 설정

1. Swagger UI 상단의 `Authorize` 버튼 클릭
2. Value 필드에 토큰 입력 (형식: `Bearer {token}`)
   ```
   Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
3. `Authorize` 클릭
4. 이제 모든 API를 인증된 상태로 테스트 가능

### curl로 테스트

```bash
# 제품 생성 (관리자 전용)
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "nameKo": "뉴폴",
    "nameEn": "NewPol",
    "descriptionKo": "천연 항산화제",
    "descriptionEn": "Natural Antioxidant",
    "category": "전체",
    "imageUrl": "https://example.com/image.jpg",
    "displayOrder": 1
  }'
```

---

## 트러블슈팅

### 문제 1: PostgreSQL 접속 안됨

```bash
# PostgreSQL 서비스 상태 확인 (Windows)
services.msc 실행 -> "postgresql-x64-15" 서비스 확인

# 서비스 시작 (관리자 권한 필요)
net start postgresql-x64-15
```

### 문제 2: Port 5432 이미 사용 중

```yaml
# application.yml에서 포트 변경
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/nawbio  # 포트 변경
```

### 문제 3: 권한 오류

```bash
# PostgreSQL 사용자 권한 확인
psql -U postgres
GRANT ALL PRIVILEGES ON DATABASE nawbio TO postgres;
```

### 문제 4: Java 버전 오류

```bash
# Java 17 설치 확인
java -version

# JAVA_HOME 환경변수 설정 (Windows)
setx JAVA_HOME "C:\Program Files\Java\jdk-17"
```

---

## 다음 단계

1. ✅ PostgreSQL 설치 및 데이터베이스 생성
2. ✅ 테이블 생성
3. ✅ 백엔드 실행
4. ✅ API 테스트
5. ⏭️ 관리자 CMS 프론트엔드 개발
6. ⏭️ 실제 데이터 마이그레이션
7. ⏭️ 프로덕션 배포

---

## 유용한 명령어 모음

```bash
# PostgreSQL 접속
psql -U postgres -d nawbio

# 모든 테이블 보기
\dt

# 테이블 구조 보기
\d 테이블명

# SQL 파일 실행
\i schema.sql

# 데이터 조회
SELECT * FROM products;
SELECT * FROM news;
SELECT * FROM admins;

# 종료
\q
```

---

**작성일**: 2024-10-17
**작성자**: 나우바이오 개발팀
**버전**: 1.0
