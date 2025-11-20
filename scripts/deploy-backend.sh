#!/bin/bash

# NawBio 백엔드 배포 스크립트
# 사용법: ./scripts/deploy-backend.sh

set -e  # 에러 발생 시 즉시 종료

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 설정 (여기를 수정하세요)
EC2_HOST="${EC2_HOST:-your-ec2-ip}"
EC2_USER="${EC2_USER:-ec2-user}"
SSH_KEY="${SSH_KEY:-~/.ssh/your-key.pem}"
REMOTE_DIR="/home/ec2-user"
JAR_NAME="nawbio-api.jar"

echo -e "${YELLOW}=== NawBio 백엔드 배포 시작 ===${NC}"

# 1. 환경 확인
if [ ! -f "$SSH_KEY" ]; then
    echo -e "${RED}SSH 키를 찾을 수 없습니다: $SSH_KEY${NC}"
    exit 1
fi

# 2. 백엔드 빌드
echo -e "${YELLOW}[1/5] 백엔드 빌드 중...${NC}"
cd nawbio-api
./gradlew clean bootJar

if [ ! -f "build/libs/nawbio-api-0.0.1-SNAPSHOT.jar" ]; then
    echo -e "${RED}빌드 실패: JAR 파일을 찾을 수 없습니다${NC}"
    exit 1
fi

echo -e "${GREEN}✓ 빌드 완료${NC}"

# 3. 현재 버전 백업
echo -e "${YELLOW}[2/5] EC2에서 현재 버전 백업 중...${NC}"
ssh -i "$SSH_KEY" "$EC2_USER@$EC2_HOST" << 'ENDSSH'
    if [ -f ~/nawbio-api.jar ]; then
        mkdir -p ~/backups
        cp ~/nawbio-api.jar ~/backups/nawbio-api-backup-$(date +%Y%m%d-%H%M%S).jar
        echo "백업 완료"
    fi
ENDSSH

echo -e "${GREEN}✓ 백업 완료${NC}"

# 4. JAR 파일 업로드
echo -e "${YELLOW}[3/5] JAR 파일 업로드 중...${NC}"
scp -i "$SSH_KEY" \
    build/libs/nawbio-api-0.0.1-SNAPSHOT.jar \
    "$EC2_USER@$EC2_HOST:$REMOTE_DIR/nawbio-api-new.jar"

echo -e "${GREEN}✓ 업로드 완료${NC}"

# 5. 서비스 재시작
echo -e "${YELLOW}[4/5] 서비스 재시작 중...${NC}"
ssh -i "$SSH_KEY" "$EC2_USER@$EC2_HOST" << 'ENDSSH'
    # JAR 파일 교체
    mv ~/nawbio-api-new.jar ~/nawbio-api.jar

    # 서비스 재시작
    sudo systemctl restart nawbio-api

    # 재시작 확인
    sleep 3
    if sudo systemctl is-active --quiet nawbio-api; then
        echo "서비스가 정상적으로 시작되었습니다"
    else
        echo "경고: 서비스 시작 실패"
        sudo journalctl -u nawbio-api -n 20
        exit 1
    fi
ENDSSH

echo -e "${GREEN}✓ 서비스 재시작 완료${NC}"

# 6. 헬스체크
echo -e "${YELLOW}[5/5] 헬스체크 중...${NC}"
sleep 5

# API 응답 확인 (포트 8080)
if ssh -i "$SSH_KEY" "$EC2_USER@$EC2_HOST" "curl -s http://localhost:8080/api/health > /dev/null"; then
    echo -e "${GREEN}✓ 백엔드가 정상 작동 중입니다${NC}"
else
    echo -e "${YELLOW}⚠ 헬스체크 실패 - 로그를 확인하세요${NC}"
    ssh -i "$SSH_KEY" "$EC2_USER@$EC2_HOST" "sudo journalctl -u nawbio-api -n 30"
fi

cd ..

echo -e "${GREEN}=== 백엔드 배포 완료 ===${NC}"
echo ""
echo "배포 정보:"
echo "  - JAR: nawbio-api-0.0.1-SNAPSHOT.jar"
echo "  - 서버: $EC2_HOST"
echo "  - 상태: $(ssh -i "$SSH_KEY" "$EC2_USER@$EC2_HOST" "sudo systemctl is-active nawbio-api")"
echo ""
echo "로그 확인: ssh -i $SSH_KEY $EC2_USER@$EC2_HOST 'sudo journalctl -u nawbio-api -f'"
