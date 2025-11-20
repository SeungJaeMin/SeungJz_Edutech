#!/bin/bash

# NawBio 전체 배포 스크립트
# 사용법: ./scripts/deploy.sh

set -e  # 에러 발생 시 즉시 종료

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}"
echo "╔════════════════════════════════════════╗"
echo "║   NawBio 전체 배포                     ║"
echo "╚════════════════════════════════════════╝"
echo -e "${NC}"

# 설정 파일 로드 (있는 경우)
if [ -f "scripts/deploy.config" ]; then
    source scripts/deploy.config
fi

# 배포 전 확인
echo -e "${YELLOW}배포를 시작하시겠습니까?${NC}"
echo "  - 백엔드: JAR 빌드 및 배포"
echo "  - 프론트엔드: React 빌드 및 배포"
echo ""
read -p "계속하려면 'yes'를 입력하세요: " confirm

if [ "$confirm" != "yes" ]; then
    echo -e "${RED}배포가 취소되었습니다${NC}"
    exit 0
fi

echo ""

# 1. Git 상태 확인
echo -e "${YELLOW}[1/3] Git 상태 확인${NC}"
if [ -n "$(git status --porcelain)" ]; then
    echo -e "${RED}경고: 커밋되지 않은 변경사항이 있습니다${NC}"
    git status --short
    read -p "계속하시겠습니까? (yes/no): " continue
    if [ "$continue" != "yes" ]; then
        exit 0
    fi
fi

CURRENT_BRANCH=$(git branch --show-current)
CURRENT_COMMIT=$(git rev-parse --short HEAD)

echo -e "${GREEN}✓ 현재 브랜치: $CURRENT_BRANCH${NC}"
echo -e "${GREEN}✓ 커밋: $CURRENT_COMMIT${NC}"
echo ""

# 2. 백엔드 배포
echo -e "${YELLOW}[2/3] 백엔드 배포${NC}"
./scripts/deploy-backend.sh

echo ""

# 3. 프론트엔드 배포
echo -e "${YELLOW}[3/3] 프론트엔드 배포${NC}"
./scripts/deploy-frontend.sh

echo ""

# 배포 완료
echo -e "${GREEN}"
echo "╔════════════════════════════════════════╗"
echo "║   배포 완료!                           ║"
echo "╚════════════════════════════════════════╝"
echo -e "${NC}"

echo "배포 정보:"
echo "  - 브랜치: $CURRENT_BRANCH"
echo "  - 커밋: $CURRENT_COMMIT"
echo "  - 시간: $(date '+%Y-%m-%d %H:%M:%S')"
echo ""
echo "확인 사항:"
echo "  1. 웹사이트: https://nawbio.com"
echo "  2. API: https://api.nawbio.com"
echo "  3. 백엔드 로그: ssh $EC2_USER@$EC2_HOST 'sudo journalctl -u nawbio-api -f'"
echo ""

# 배포 기록 저장
echo "$(date '+%Y-%m-%d %H:%M:%S') | $CURRENT_BRANCH | $CURRENT_COMMIT | SUCCESS" >> scripts/deploy-history.log
