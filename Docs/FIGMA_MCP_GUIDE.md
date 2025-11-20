# Figma MCP 연동 가이드

## 개요
Figma MCP(Model Context Protocol) 서버를 Claude Code에 연결하여 디자이너가 작성한 Figma 디자인을 AI가 읽고 코드로 변환할 수 있습니다.

---

## 설치 방법

### 방법 1: 공식 Figma MCP 서버 (권장)

#### 1단계: MCP 서버 추가
```bash
claude mcp add --transport http figma https://mcp.figma.com/mcp
```

#### 2단계: 인증
1. Claude Code에서 `/mcp` 명령어 입력
2. MCP 서버 목록에서 `figma` 선택
3. `Authenticate` 선택
4. `Allow Access` 클릭하여 Figma 계정 연결

#### 3단계: 연동 확인
- Claude Code에서 Figma 파일에 접근할 수 있는지 테스트

---

### 방법 2: Figma Desktop App 사용 (로컬 서버)

#### 요구사항
- 최신 버전의 Figma Desktop App 설치

#### MCP 설정 (Claude Code)
```json
{
  "servers": {
    "figma-desktop": {
      "type": "http",
      "url": "http://127.0.0.1:3845/mcp"
    }
  }
}
```

---

### 방법 3: 커뮤니티 MCP 서버 (Framelink Figma MCP)

#### 설치
```bash
npm install -g figma-developer-mcp
```

#### 설정
1. Figma Personal Access Token 발급 필요
2. MCP 설정 파일에 추가:
```json
{
  "mcpServers": {
    "Framelink MCP for Figma": {
      "command": "npx",
      "args": [
        "-y",
        "figma-developer-mcp",
        "--figma-api-key=YOUR_FIGMA_API_KEY",
        "--stdio"
      ]
    }
  }
}
```

---

## Figma Personal Access Token 발급 방법

### 1단계: Figma 설정 접속
1. Figma 웹사이트 로그인: https://www.figma.com/
2. 우측 상단 프로필 아이콘 클릭
3. `Settings` 선택

### 2단계: Access Token 생성
1. 왼쪽 메뉴에서 `Personal access tokens` 선택
2. `Create new token` 버튼 클릭
3. Token 이름 입력 (예: "Edutech Hackathon")
4. 필요한 권한 선택:
   - `File content - Read only` (필수)
   - `File variables - Read only` (선택)
5. `Generate token` 클릭
6. **중요**: 생성된 토큰을 안전한 곳에 복사 저장 (다시 볼 수 없음)

---

## 사용 방법

### 1. Figma 파일 URL 가져오기
```
예시: https://www.figma.com/file/FILE_ID/FILE_NAME
```

### 2. Claude Code에서 Figma 디자인 가져오기

#### 기본 사용법
```
Claude에게 요청:
"Figma 파일 [URL]에서 로그인 화면 디자인을 가져와서 React Native 컴포넌트로 변환해줘"
```

#### 구체적인 예시
```
"https://www.figma.com/file/abc123/MobileApp 파일의
'Login Screen' 프레임을 분석하고 다음을 생성해줘:
1. React Native 컴포넌트
2. 스타일시트
3. 사용된 색상 변수"
```

### 3. MCP 도구 확인
Claude Code에서 사용 가능한 Figma MCP 도구:
- `#get_design_context` - 디자인 컨텍스트 가져오기
- Figma 파일 읽기
- 컴포넌트 정보 추출
- 스타일 정보 가져오기

---

## 실제 워크플로우

### 디자이너 → 개발자 협업 프로세스

1. **디자이너 작업**
   - Figma에서 모바일 UI 디자인 작성
   - 프레임/컴포넌트 명확히 네이밍
   - 개발자에게 Figma 파일 URL 공유

2. **개발자 작업**
   - Claude Code에 Figma URL 제공
   - AI가 디자인 분석 및 코드 생성
   - 생성된 코드 검토 및 수정
   - 실제 앱에 통합

3. **반복 개발**
   - 디자이너가 Figma 업데이트
   - Claude Code가 변경사항 감지
   - 코드 자동 업데이트 또는 제안

---

## 팁 & 베스트 프랙티스

### Figma 파일 구조화
```
프로젝트 파일
├── 📱 Screens
│   ├── Login Screen
│   ├── Home Screen
│   ├── Profile Screen
│   └── Settings Screen
├── 🧩 Components
│   ├── Buttons
│   ├── Input Fields
│   └── Cards
└── 🎨 Design System
    ├── Colors
    ├── Typography
    └── Spacing
```

### 네이밍 컨벤션
- 화면: `[기능명] Screen` (예: Login Screen)
- 컴포넌트: `[타입]/[이름]` (예: Button/Primary)
- 레이어: 명확한 영문명 사용

### Claude Code 사용 팁
1. **구체적으로 요청하기**
   ```
   ❌ "이 화면 코드로 만들어줘"
   ✅ "Login Screen 프레임을 React Native 함수형 컴포넌트로 변환하고,
       StyleSheet.create를 사용해서 스타일 분리해줘"
   ```

2. **컴포넌트별로 나눠서 요청**
   - 한 번에 전체 화면보다는 컴포넌트 단위로 요청
   - 재사용 가능한 컴포넌트 먼저 생성

3. **디자인 시스템 활용**
   - Figma의 Colors, Typography를 먼저 추출
   - 프로젝트 전체에서 재사용

---

## 트러블슈팅

### 문제: MCP 서버 연결 실패
**해결방법**:
1. 인터넷 연결 확인
2. Figma 로그인 상태 확인
3. Claude Code 재시작
4. `claude mcp list`로 설치된 서버 확인

### 문제: Figma 파일 접근 불가
**해결방법**:
1. Figma 파일 공유 권한 확인 (최소 View 권한 필요)
2. Personal Access Token 권한 확인
3. 파일 URL이 올바른지 확인

### 문제: 디자인이 정확하게 변환되지 않음
**해결방법**:
1. Figma 레이어 구조 단순화
2. Auto Layout 적용 (Figma의 Flexbox와 유사)
3. 명확한 네이밍 사용
4. Claude에게 더 구체적인 지시 제공

---

## 다음 단계

- [ ] Figma MCP 서버 설치 완료
- [ ] Figma Personal Access Token 발급
- [ ] 테스트 디자인 파일로 연동 테스트
- [ ] 디자이너로부터 실제 프로젝트 파일 URL 받기
- [ ] 첫 번째 화면 코드 생성 테스트

---

## 참고 링크

- [Figma MCP 공식 문서](https://developers.figma.com/docs/figma-mcp-server/)
- [Figma MCP 블로그](https://www.figma.com/blog/introducing-figmas-dev-mode-mcp-server/)
- [Model Context Protocol](https://modelcontextprotocol.io/)
- [Figma Personal Access Tokens 가이드](https://help.figma.com/hc/en-us/articles/8085703771159-Manage-personal-access-tokens)

---

**작성일**: 2025-11-20
**최종 업데이트**: 2025-11-20
