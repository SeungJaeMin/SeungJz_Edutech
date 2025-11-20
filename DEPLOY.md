# EC2 배포 가이드

## 준비사항

### 1. EC2 인스턴스 요구사항
- **OS**: Ubuntu 22.04 LTS 이상
- **인스턴스 타입**: t3.medium 이상 권장 (2 vCPU, 4GB RAM)
- **스토리지**: 30GB 이상
- **보안 그룹**: 다음 포트 개방
  - 22 (SSH)
  - 8080 (Backend API)
  - 5432 (PostgreSQL - 옵션)
  - 3000 (Frontend - 옵션)

### 2. 필요한 소프트웨어
- Docker
- Docker Compose
- Git

## 배포 절차

### 1단계: EC2 인스턴스 접속
```bash
ssh -i your-key.pem ubuntu@your-ec2-ip
```

### 2단계: Docker 설치
```bash
# Docker 설치
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg lsb-release

sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Docker 권한 설정
sudo usermod -aG docker $USER
newgrp docker

# Docker Compose 설치
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### 3단계: 프로젝트 클론
```bash
cd ~
git clone https://github.com/SeungJaeMin/SeungJz_Edutech.git
cd SeungJz_Edutech
git checkout 1121_SeungJ_api
```

### 4단계: 환경 변수 설정
```bash
# .env 파일 생성
cat > .env << 'EOF'
# Database
DB_PASSWORD=your_secure_password_here

# JWT
JWT_SECRET=your_jwt_secret_key_min_256_bits

# AWS (선택사항)
AWS_ACCESS_KEY=
AWS_SECRET_KEY=
S3_BUCKET_NAME=
AWS_REGION=ap-northeast-2

# Email (선택사항)
SMTP_USERNAME=
SMTP_PASSWORD=

# CORS
CORS_ORIGINS=http://your-frontend-domain.com,http://your-ec2-ip:3000
EOF

# 권한 설정
chmod 600 .env
```

### 5단계: Docker Compose로 배포

#### 옵션 A: Backend + PostgreSQL만 배포
```bash
docker-compose up -d postgres backend
```

#### 옵션 B: 전체 스택 배포 (권장하지 않음 - ML 서비스 제외)
```bash
# ML 서비스 제외하고 backend, postgres만 실행
docker-compose up -d postgres
docker-compose up -d backend
```

### 6단계: 배포 확인
```bash
# 컨테이너 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs -f backend

# PostgreSQL 연결 테스트
docker exec -it seungjz-postgres psql -U postgres -d edutech -c "\dt"

# API 테스트
curl http://localhost:8080/api/swagger-ui.html
```

### 7단계: 데이터베이스 테이블 확인
```bash
# PostgreSQL 컨테이너 접속
docker exec -it seungjz-postgres psql -U postgres -d edutech

# 테이블 목록 확인
\dt

# 종료
\q
```

## API 엔드포인트

### Swagger UI
```
http://your-ec2-ip:8080/api/swagger-ui.html
```

### Health Check
```
http://your-ec2-ip:8080/api/actuator/health
```

### 주요 API
- **Lectures**: `/api/lectures`
- **Interview**: `/api/interviews`
- **Progress**: `/api/progress`
- **Admin**: `/api/init/admin` (초기 관리자 생성)

## 초기 데이터 설정

### 관리자 계정 생성
```bash
curl -X POST http://localhost:8080/api/init/admin
```

기본 관리자 계정:
- Username: `naw181114`
- Password: `naw1023!`

## 트러블슈팅

### Docker 컨테이너가 시작되지 않을 때
```bash
# 로그 확인
docker-compose logs backend
docker-compose logs postgres

# 컨테이너 재시작
docker-compose restart backend
```

### PostgreSQL 연결 실패
```bash
# PostgreSQL 상태 확인
docker-compose exec postgres pg_isready -U postgres

# 네트워크 확인
docker network inspect seungjz-network
```

### 포트 충돌
```bash
# 사용 중인 포트 확인
sudo lsof -i :8080
sudo lsof -i :5432

# 프로세스 종료
sudo kill -9 <PID>
```

## 유지보수

### 로그 모니터링
```bash
# 실시간 로그
docker-compose logs -f backend

# 최근 100줄
docker-compose logs --tail=100 backend
```

### 백업
```bash
# 데이터베이스 백업
docker exec seungjz-postgres pg_dump -U postgres edutech > backup_$(date +%Y%m%d).sql

# 복원
docker exec -i seungjz-postgres psql -U postgres edutech < backup_20250121.sql
```

### 업데이트
```bash
# 최신 코드 가져오기
git pull origin 1121_SeungJ_api

# 재빌드 및 재시작
docker-compose build backend
docker-compose up -d backend
```

### 정리
```bash
# 컨테이너 중지
docker-compose down

# 볼륨 포함 완전 삭제
docker-compose down -v
```

## 성능 최적화

### JVM 메모리 조정
```yaml
# docker-compose.yml의 backend 서비스에 추가
environment:
  - JAVA_OPTS=-Xmx1g -Xms512m
```

### PostgreSQL 튜닝
```bash
# PostgreSQL 설정 조정 (docker-compose.yml)
command: postgres -c 'max_connections=100' -c 'shared_buffers=256MB'
```

## 보안 체크리스트

- [ ] `.env` 파일에 강력한 비밀번호 설정
- [ ] JWT_SECRET 변경 (최소 256비트)
- [ ] DB_PASSWORD 변경
- [ ] PostgreSQL 포트 외부 접근 차단 (5432)
- [ ] HTTPS 설정 (Nginx/ALB 사용)
- [ ] 방화벽 설정 (UFW)
- [ ] SSH 키 기반 인증 사용
- [ ] 정기적인 백업 설정

## 모니터링

### 리소스 사용량 확인
```bash
# Docker 컨테이너 리소스
docker stats

# 시스템 리소스
htop
df -h
```

### 헬스체크
```bash
# Backend health
curl http://localhost:8080/api/actuator/health

# Database health
docker exec seungjz-postgres pg_isready -U postgres
```
