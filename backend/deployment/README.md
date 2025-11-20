# NawBio API 배포 가이드

## EC2 초기 설정 (최초 1회만)

### 1. systemd 서비스 파일 설정

```bash
# 템플릿 복사
sudo cp nawbio-api.service.template /etc/systemd/system/nawbio-api.service

# 실제 값으로 수정
sudo vi /etc/systemd/system/nawbio-api.service
```

**수정 필요 항목:**
- `SPRING_DATASOURCE_USERNAME`: PostgreSQL 사용자명 (기본값: nawbio_user)
- `SPRING_DATASOURCE_PASSWORD`: PostgreSQL 비밀번호
- `JWT_SECRET`: `openssl rand -base64 64`로 생성한 값
- `AWS_ACCESS_KEY`: 실제 AWS Access Key
- `AWS_SECRET_KEY`: 실제 AWS Secret Key
- `CORS_ALLOWED_ORIGINS`: 프로덕션 도메인 (또는 `*`)

### 2. 서비스 활성화

```bash
sudo systemctl daemon-reload
sudo systemctl enable nawbio-api
sudo systemctl start nawbio-api
sudo systemctl status nawbio-api
```

---

## 배포 방식

### 자동 배포 (GitHub Actions)

**트리거:**
- `main` 브랜치 또는 `*_EC2Deployment` 브랜치에 push
- `nawbio-api/**` 경로의 파일 변경

**프로세스:**
1. JAR 빌드
2. EC2에 업로드
3. 서비스 재시작

### 수동 배포

```bash
# 로컬에서 빌드
./gradlew clean bootJar

# EC2에 업로드
scp -i ~/.ssh/naw-key.pem \
  build/libs/nawbio-api-0.0.1-SNAPSHOT.jar \
  ec2-user@43.201.247.148:/opt/nawbio/nawbio-api.jar

# EC2에서 재시작
ssh -i ~/.ssh/naw-key.pem ec2-user@43.201.247.148
sudo systemctl restart nawbio-api
```

---

## 주의사항 ⚠️

### DO ✅
- 코드는 **Git에서만** 수정
- 환경변수는 **systemd 파일에서** 수정
- systemd 파일 변경 시 **템플릿도 업데이트**

### DON'T ❌
- EC2에서 코드 직접 수정
- JAR 파일 수동 변경
- application-prod.yml 직접 수정 (환경변수 사용)

---

## 환경변수 변경 시

```bash
# systemd 파일 수정
sudo vi /etc/systemd/system/nawbio-api.service

# 적용
sudo systemctl daemon-reload
sudo systemctl restart nawbio-api
sudo systemctl status nawbio-api
```

---

## 트러블슈팅

### 서비스 로그 확인
```bash
sudo journalctl -u nawbio-api -f
```

### 서비스 상태 확인
```bash
sudo systemctl status nawbio-api
```

### API 헬스체크
```bash
curl https://api.nawbio.com/api/actuator/health
```
