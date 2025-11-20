# SeungJz Edutech - Frontend

## 프로젝트 개요

해커톤 12시간 내 완성을 목표로 하는 에듀테크 AI 애플리케이션 프론트엔드

## 기술 스택

- **React 19** - UI 라이브러리
- **TypeScript** - 타입 안정성
- **Vite** - 빌드 도구 (초고속 개발 서버)
- **Zustand** - 경량 상태 관리
- **React Router** - 라우팅
- **TanStack Query** - API 상태 관리
- **Axios** - HTTP 클라이언트

## 시작하기

### 개발 서버 시작
```bash
npm run dev
```
서버가 시작되면: **http://localhost:5173**

### 빌드 (프로덕션)
```bash
npm run build
```

## 프로젝트 구조

```
src/
├── components/      # 재사용 가능한 컴포넌트
├── pages/           # 페이지 컴포넌트
├── hooks/           # Custom Hooks
├── store/           # Zustand 스토어
├── api/             # API 호출 함수
├── types/           # TypeScript 타입 정의
├── utils/           # 유틸리티 함수
└── styles/          # 전역 스타일
```

## Figma 연동

Figma MCP가 연결되어 있습니다. Claude Code에게 요청하면 Figma 디자인을 React 컴포넌트로 자동 변환할 수 있습니다.

```
"Figma 파일 https://www.figma.com/design/qNH0gG3M2WGNwK1ygMhG5e/HT_SeungJz
에서 로그인 화면을 React 컴포넌트로 만들어줘"
```

## 다음 단계

- [ ] Figma 디자인 시스템 추출
- [ ] 공통 컴포넌트 생성
- [ ] 주요 페이지 구현
- [ ] API 연동
- [ ] AI 기능 통합
