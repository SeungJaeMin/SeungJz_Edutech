# SeungJz Edutech 해커톤 프로젝트

## 프로젝트 개요

**해커톤 시간**: 12시간
**준비 시간**: 2시간
**주제**: 에듀테크 관련 문제를 AI를 이용하여 해결
**세부 주제**: 미정 (해커톤 시작 후 결정)

## 팀 구성

| 역할 | 담당 업무 |
|------|----------|
| 풀스택 개발자 | 모바일 앱 개발, 백엔드 개발, 인프라 구축 |
| 디자이너 | Figma를 이용한 모바일 UI/UX 디자인 |

**개발 포커스**: 모바일 애플리케이션

---

## 시스템 아키텍처

### 3-Tier Architecture

```
[모바일 앱]
    ↓
[서버 - Spring Boot]
    ↓
[데이터베이스 - PostgreSQL/MySQL]
```

### 기술 스택

#### 프론트엔드 (모바일)
- 플랫폼: TBD (React Native / Flutter / Native 등)
- 디자인 연동: Figma MCP

#### 백엔드
- 프레임워크: Spring Boot
- 언어: Java
- AI 모델 통합: Ollama

#### 데이터베이스
- 타입: RDB (PostgreSQL 또는 MySQL)
- 데이터 형식: JSON 지원

#### 인프라
- 클라우드: AWS EC2
- CI/CD: GitHub Actions (예정)

---

## 2시간 내 완료 목표

### 1. 프로젝트 GitHub 연동 ✓
- [x] 저장소 Clone
- [ ] 기본 브랜치 전략 수립
- [ ] 협업 워크플로우 설정

### 2. Figma MCP 연결
**목표**: 디자이너가 작성한 UI 디자인을 자동으로 반영할 수 있는 환경 구축

#### 필요 작업
- [ ] Figma MCP 서버 설치
- [ ] Figma API 토큰 설정
- [ ] Claude Code와 Figma 연동 테스트
- [ ] 디자인 파일 접근 권한 설정

#### 참고 사항
- Figma 파일 URL이 필요함
- 디자이너와 파일 공유 권한 확인 필요

### 3. AI Model 탑재
**목표**: Ollama를 통한 AI 모델 테스트 환경 구축

#### 필요 작업
- [ ] Ollama 설치 (로컬 또는 서버)
- [ ] 적합한 AI 모델 선택 및 다운로드
- [ ] Spring Boot에서 Ollama API 연동 테스트
- [ ] 간단한 AI 기능 프로토타입 구현

#### 후보 모델 (Ollama)
- llama2
- mistral
- codellama
- phi

### 4. 모바일 개발환경 준비
**목표**: 모바일 앱 개발을 즉시 시작할 수 있는 환경 구성

#### 필요 작업
- [ ] 모바일 프레임워크 선택 및 설치
- [ ] 프로젝트 초기 구조 생성
- [ ] 개발 서버와의 연동 테스트
- [ ] 디바이스/에뮬레이터 테스트 환경 확인

---

## CI/CD 파이프라인 (추후 구축)

### AWS EC2 인프라
```
GitHub Repository
    ↓ (Push/PR)
GitHub Actions
    ↓
AWS EC2 Instance
    ├─ Spring Boot Application
    └─ Database (PostgreSQL/MySQL)
```

### 배포 전략
- 개발 브랜치: develop
- 프로덕션 브랜치: main
- 자동 배포: develop → EC2 Dev 환경
- 수동 배포: main → EC2 Production 환경

---

## 브랜치 전략

```
main (프로덕션)
  ↑
develop (개발 통합)
  ↑
feature/* (기능 개발)
```

### 브랜치 명명 규칙
- 기능 개발: `feature/기능명`
- 버그 수정: `fix/버그명`
- 긴급 수정: `hotfix/이슈명`

---

## 개발 워크플로우

1. **기능 개발 시작**
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/기능명
   ```

2. **개발 및 커밋**
   ```bash
   git add .
   git commit -m "feat: 기능 설명"
   ```

3. **개발 브랜치에 병합**
   ```bash
   git checkout develop
   git merge feature/기능명
   git push origin develop
   ```

4. **배포** (추후 자동화 예정)
   - develop → 자동 배포
   - main → 수동 배포 (안정화 후)

---

## 주요 고려사항

### 시간 제약
- 해커톤 시작 전 2시간: 개발 환경 완벽히 구축
- 해커톤 12시간: 순수 개발/디자인 집중

### AI 기능 통합
- Ollama 모델은 로컬 또는 EC2에서 실행
- API 응답 속도 최적화 필요
- 오프라인 동작 가능성 고려

### 모바일 우선 개발
- 디자이너와의 실시간 협업이 핵심
- Figma MCP를 통한 빠른 UI 구현
- 반응형보다는 모바일 최적화에 집중

### 데이터베이스
- JSON 형식 데이터 저장 지원 (PostgreSQL JSONB 또는 MySQL JSON 타입)
- 스키마는 유연하게 설계 (요구사항 변경 대비)

---

## 다음 단계

1. **즉시 수행**: 2시간 내 완료 목표 항목 체크
2. **해커톤 전**: 팀 미팅을 통한 전략 수립
3. **해커톤 시작**: 주제 파악 후 빠른 MVP 개발

---

## 참고 링크

- GitHub Repository: https://github.com/SeungJaeMin/SeungJz_Edutech.git
- Ollama: https://ollama.com/
- AWS EC2: (인스턴스 정보 추후 추가)
- Figma 파일: (디자이너로부터 받을 예정)

---

**작성일**: 2025-11-20
**업데이트**: 프로젝트 진행에 따라 지속적으로 업데이트 예정
