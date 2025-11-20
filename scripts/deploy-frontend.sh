#!/bin/bash

# NawBio 프론트엔드 배포 스크립트
# 사용법: ./scripts/deploy-frontend.sh

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
REMOTE_DIR="/var/www/nawbio-web/build"

echo -e "${YELLOW}=== NawBio 프론트엔드 배포 시작 ===${NC}"

# 1. 환경 확인
if [ ! -f "$SSH_KEY" ]; then
    echo -e "${RED}SSH 키를 찾을 수 없습니다: $SSH_KEY${NC}"
    exit 1
fi

# 2. 프론트엔드 빌드
echo -e "${YELLOW}[1/4] 프론트엔드 빌드 중...${NC}"
cd nawbio-web

# .env.production 확인
if [ ! -f ".env.production" ]; then
    echo -e "${RED}.env.production 파일이 없습니다${NC}"
    echo "다음 내용으로 생성하세요:"
    echo "REACT_APP_API_URL=https://api.nawbio.com"
    exit 1
fi

npm run build

if [ ! -d "build" ]; then
    echo -e "${RED}빌드 실패: build 디렉토리를 찾을 수 없습니다${NC}"
    exit 1
fi

echo -e "${GREEN}✓ 빌드 완료${NC}"

# 3. 현재 버전 백업
echo -e "${YELLOW}[2/4] EC2에서 현재 버전 백업 중...${NC}"
ssh -i "$SSH_KEY" "$EC2_USER@$EC2_HOST" << ENDSSH
    if [ -d $REMOTE_DIR ]; then
        sudo mkdir -p /var/www/nawbio-web/backups
        sudo cp -r $REMOTE_DIR /var/www/nawbio-web/backups/build-backup-\$(date +%Y%m%d-%H%M%S)
        echo "백업 완료"
    fi
ENDSSH

echo -e "${GREEN}✓ 백업 완료${NC}"

# 4. 빌드 파일 업로드
echo -e "${YELLOW}[3/4] 빌드 파일 업로드 중...${NC}"

# 임시 디렉토리에 먼저 업로드
scp -i "$SSH_KEY" -r build/* "$EC2_USER@$EC2_HOST:/tmp/nawbio-web-build/"

# sudo 권한으로 파일 이동
ssh -i "$SSH_KEY" "$EC2_USER@$EC2_HOST" << ENDSSH
    # 기존 파일 삭제
    sudo rm -rf $REMOTE_DIR/*

    # 새 파일 복사
    sudo mv /tmp/nawbio-web-build/* $REMOTE_DIR/

    # 권한 설정
    sudo chown -R nginx:nginx $REMOTE_DIR
    sudo chmod -R 755 $REMOTE_DIR

    echo "파일 배포 완료"
ENDSSH

echo -e "${GREEN}✓ 업로드 완료${NC}"

# 5. Nginx 재시작 (선택)
echo -e "${YELLOW}[4/4] Nginx 설정 확인 중...${NC}"
ssh -i "$SSH_KEY" "$EC2_USER@$EC2_HOST" << 'ENDSSH'
    # Nginx 설정 테스트
    if sudo nginx -t 2>&1 | grep -q "successful"; then
        echo "Nginx 설정 정상"
        # 필요시 reload (graceful restart)
        # sudo systemctl reload nginx
    else
        echo "경고: Nginx 설정 오류"
        sudo nginx -t
    fi
ENDSSH

echo -e "${GREEN}✓ Nginx 확인 완료${NC}"

cd ..

echo -e "${GREEN}=== 프론트엔드 배포 완료 ===${NC}"
echo ""
echo "배포 정보:"
echo "  - 빌드 크기: $(du -sh nawbio-web/build | cut -f1)"
echo "  - 서버: $EC2_HOST"
echo "  - 디렉토리: $REMOTE_DIR"
echo ""
echo "웹사이트 확인: https://nawbio.com"
