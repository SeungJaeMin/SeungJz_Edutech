# Backend - SeungJz Edutech API

Spring Boot 기반 REST API 서버

## 기술 스택

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: H2 (개발) / PostgreSQL (프로덕션)
- **Build Tool**: Gradle
- **API Documentation**: Swagger/OpenAPI

## 빠른 시작

### 1. IDE에서 실행 (추천)

#### IntelliJ IDEA
1. 프로젝트를 IntelliJ에서 열기
2. `EdutechApplication.java` 파일 찾기
3. 메인 메서드 옆의 실행 버튼 클릭
4. 또는 `Shift + F10` (Windows) / `Ctrl + R` (Mac)

#### Eclipse
1. 프로젝트를 Eclipse에서 열기
2. `EdutechApplication.java` 우클릭
3. Run As → Spring Boot App

### 2. 명령줄에서 실행

```bash
# JAR 파일이 있는 경우
java -jar build/libs/backend-0.0.1-SNAPSHOT.jar

# 또는 Windows 스크립트
run.bat
```

### 3. 빌드 방법

```bash
# Gradle Wrapper 사용
./gradlew clean build     # Linux/Mac
gradlew.bat clean build   # Windows

# 테스트 제외하고 빌드
./gradlew clean build -x test
```

## 서버 접속 정보

실행 후 다음 URL에서 확인 가능:

- **API 서버**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

### H2 Database 설정
- JDBC URL: `jdbc:h2:mem:edutech`
- Username: `sa`
- Password: (비어있음)

## API 엔드포인트

### Lecture API
```
POST   /api/lectures              # 강의 생성 (파일 업로드)
GET    /api/lectures              # 전체 조회
GET    /api/lectures/{id}         # 단건 조회
GET    /api/lectures/type/{type}  # 타입별 조회
GET    /api/lectures/level/{level}# 레벨별 조회
PUT    /api/lectures/{id}         # 수정
DELETE /api/lectures/{id}         # 삭제
```

## 파일 업로드

- **비디오**: 최대 500MB
- **썸네일**: 최대 5MB
- **저장 위치**: `uploads/videos`, `uploads/thumbnails`
- **접근 URL**: `http://localhost:8080/videos/{filename}`

## 설정 파일

`src/main/resources/application.yml`:
- 데이터베이스 설정
- 파일 업로드 크기
- CORS 설정
- 로깅 레벨

## 트러블슈팅

### 포트 충돌 (8080)
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID {PID} /F

# Linux/Mac
lsof -i :8080
kill -9 {PID}
```

### 데이터베이스 연결 실패
- application.yml의 데이터베이스 설정 확인
- PostgreSQL이 실행 중인지 확인
- H2 사용 시 메모리 모드인지 확인

### 빌드 실패
```bash
# 캐시 삭제 후 재빌드
./gradlew clean
./gradlew build --refresh-dependencies
```

## 개발 가이드

### 새 API 추가
1. Entity 작성 (`domain/{domain}/entity`)
2. Repository 작성 (`domain/{domain}/repository`)
3. DTO 작성 (`domain/{domain}/dto`)
4. Service 작성 (`domain/{domain}/service`)
5. Controller 작성 (`domain/{domain}/controller`)

### 로그 확인
- 콘솔에서 실시간 확인
- 로그 레벨: DEBUG (개발), INFO (프로덕션)

## 다음 단계

- [ ] JWT 인증 구현
- [ ] PostgreSQL 연동
- [ ] ML 서비스 연동
- [ ] 테스트 코드 작성
