# 🐋 Docker 실행 가이드

## 빠른 시작 (Quick Start)

### Windows
```bash
docker-start.bat
```

### Linux/Mac
```bash
chmod +x docker-start.sh
./docker-start.sh
```

## 수동 실행

### 1. 전체 서비스 시작
```bash
docker-compose -f docker-compose.simple.yml up --build -d
```

### 2. 서비스 확인
```bash
docker-compose -f docker-compose.simple.yml ps
```

### 3. 로그 확인
```bash
# 전체 로그
docker-compose -f docker-compose.simple.yml logs -f

# 특정 서비스 로그
docker-compose -f docker-compose.simple.yml logs -f backend
docker-compose -f docker-compose.simple.yml logs -f frontend
docker-compose -f docker-compose.simple.yml logs -f postgres
```

### 4. 서비스 중지
```bash
docker-compose -f docker-compose.simple.yml down
```

### 5. 볼륨까지 삭제 (데이터 초기화)
```bash
docker-compose -f docker-compose.simple.yml down -v
```

## 접속 URL

| 서비스 | URL | 설명 |
|--------|-----|------|
| **Frontend** | http://localhost:3000 | React 웹 애플리케이션 |
| **Backend API** | http://localhost:8080 | Spring Boot REST API |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | API 문서 |
| **H2 Console** | http://localhost:8080/h2-console | H2 데이터베이스 콘솔 |
| **PostgreSQL** | localhost:5432 | PostgreSQL 데이터베이스 |

## 데이터베이스 연결 정보

### H2 Database (기본)
- **URL**: `jdbc:h2:mem:edutech`
- **Username**: `sa`
- **Password**: (없음)
- **Driver**: `org.h2.Driver`

### PostgreSQL (선택사항)
- **Host**: `localhost`
- **Port**: `5432`
- **Database**: `edutech`
- **Username**: `admin`
- **Password**: `admin123`

## 서비스 구성

```
┌─────────────────────────────────────────────┐
│          Frontend (React + Nginx)           │
│            http://localhost:3000            │
└──────────────────┬──────────────────────────┘
                   │ API 요청
                   ↓
┌─────────────────────────────────────────────┐
│       Backend (Spring Boot + H2)            │
│            http://localhost:8080            │
└──────────────────┬──────────────────────────┘
                   │ (Optional)
                   ↓
┌─────────────────────────────────────────────┐
│          PostgreSQL Database                │
│            localhost:5432                   │
└─────────────────────────────────────────────┘
```

## 트러블슈팅

### 포트 충돌
만약 포트가 이미 사용 중이라면:
```bash
# Windows
netstat -ano | findstr :8080
netstat -ano | findstr :3000

# Linux/Mac
lsof -i :8080
lsof -i :3000
```

### Docker 빌드 캐시 삭제
```bash
docker-compose -f docker-compose.simple.yml build --no-cache
```

### 전체 재시작
```bash
docker-compose -f docker-compose.simple.yml down -v
docker-compose -f docker-compose.simple.yml up --build -d
```

### 컨테이너 내부 접속
```bash
# Backend
docker exec -it edutech-backend bash

# Frontend
docker exec -it edutech-frontend sh

# PostgreSQL
docker exec -it edutech-postgres psql -U admin -d edutech
```

## 개발 환경 vs 프로덕션

현재 `docker-compose.simple.yml`은 **개발 환경**용입니다.

프로덕션 환경은 `docker-compose.prod.yml` 사용:
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## 파일 업로드 볼륨

업로드된 파일은 Docker 볼륨에 저장됩니다:
```bash
# 볼륨 확인
docker volume ls | grep edutech

# 볼륨 위치 확인
docker volume inspect seungjz_edutech_backend-uploads
```

## Health Check

모든 서비스는 헬스체크가 설정되어 있습니다:
```bash
# 백엔드 헬스체크
curl http://localhost:8080/actuator/health

# PostgreSQL 헬스체크
docker exec edutech-postgres pg_isready -U admin -d edutech
```

## 다음 단계

1. **Lecture 업로드 테스트**: http://localhost:3000/lectures/create
2. **API 테스트**: http://localhost:8080/swagger-ui.html
3. **데이터베이스 확인**: http://localhost:8080/h2-console

---

**문제 발생 시**:
- GitHub Issues: https://github.com/SeungJaeMin/SeungJz_Edutech/issues
- 로그 확인: `docker-compose -f docker-compose.simple.yml logs -f`
