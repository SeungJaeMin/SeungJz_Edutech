# Nawbio API 빠른 시작 가이드

## ⚡ 5분 안에 시작하기

### 1단계: PostgreSQL 설치 확인
```bash
psql --version
```
- ❌ 없으면: [PostgreSQL 다운로드](https://www.postgresql.org/download/windows/)
- ✅ 있으면: 다음 단계

### 2단계: 데이터베이스 생성
```bash
psql -U postgres
```
```sql
CREATE DATABASE nawbio;
\q
```

### 3단계: 테이블 생성
```bash
cd nawbio-api
psql -U postgres -d nawbio -f schema.sql
```

### 4단계: 비밀번호 설정
`src/main/resources/application.yml` 파일 열기:
```yaml
spring:
  datasource:
    password: tnwjdtkgkd1
```

### 5단계: 백엔드 실행
```bash
# Windows
gradlew.bat bootRun

# 또는 IntelliJ에서 NawbioApiApplication.java 실행
```

### 6단계: 테스트
브라우저 열기:
```
http://localhost:8080/api/swagger-ui.html
```

---

## 🔧 초기 관리자 계정 생성

```bash
psql -U postgres -d nawbio
```
```sql
INSERT INTO admins (username, password, email, is_active, created_at)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@nawbio.com', true, NOW());
```

로그인 정보:
- Username: `admin`
- Password: `admin123`

---

## 🎯 체크리스트

- [ ] PostgreSQL 설치됨
- [ ] `nawbio` 데이터베이스 생성됨
- [ ] 테이블 생성 완료 (products, news, admins)
- [ ] `application.yml` 비밀번호 설정
- [ ] Java 17 설치 확인 (`java -version`)
- [ ] 백엔드 실행 성공 (`Started NawbioApiApplication...`)
- [ ] Swagger UI 접속 성공
- [ ] 관리자 계정 생성 완료
- [ ] 로그인 테스트 성공

---

## 🚨 문제 해결

### 백엔드가 실행 안됨
```bash
# Java 버전 확인
java -version  # 17 이상 필요

# Gradle wrapper 생성
gradle wrapper
```

### PostgreSQL 접속 안됨
```bash
# Windows 서비스 확인
services.msc

# PostgreSQL 서비스 시작
net start postgresql-x64-15
```

### Port 충돌
`application.yml`에서 포트 변경:
```yaml
server:
  port: 8081  # 8080 대신 다른 포트
```

---

더 자세한 내용은 `SETUP_GUIDE.md` 참고
