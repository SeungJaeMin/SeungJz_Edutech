# Figma 디자인 가져오기 안내

## 현재 상황

Figma MCP가 연결되어 있지만, Figma 파일에 대한 직접 접근이 제한되어 있습니다.

## 해결 방법

### 방법 1: Figma 파일 공개 설정 (권장)

1. Figma에서 파일 열기: https://www.figma.com/design/qNH0gG3M2WGNwK1ygMhG5e/HT_SeungJz
2. 우측 상단 `Share` 버튼 클릭
3. `Anyone with the link` → `can view` 설정
4. 링크 복사

### 방법 2: Figma Personal Access Token 사용

1. Figma 설정에서 Personal Access Token 생성
2. Claude Code에 토큰 설정
3. Figma API를 통해 접근

### 방법 3: 디자이너와 협업

디자이너에게 다음 정보를 요청하세요:

1. **버튼 디자인 정보**
   - 버튼 종류 (Primary, Secondary, Text 등)
   - 각 버튼의 스타일 (색상, 크기, 패딩, 테두리)
   - 버튼 상태 (Normal, Hover, Pressed, Disabled)

2. **색상 팔레트**
   - Primary 색상: #______
   - Secondary 색상: #______
   - Background 색상: #______
   - Text 색상: #______
   - 기타 색상들

3. **타이포그래피**
   - 폰트 패밀리
   - 폰트 크기 (H1, H2, Body, Caption 등)
   - 폰트 굵기

4. **간격 시스템**
   - 기본 간격 (4px, 8px, 16px, 24px 등)

## 임시 대안: 일반적인 모바일 UI 패턴으로 구현

Figma 접근이 안 되는 동안, 일반적인 에듀테크 앱의 디자인 시스템을 기반으로 구현할 수 있습니다.

### 제공할 수 있는 것:

1. **모던한 버튼 컴포넌트**
   - Material Design 스타일
   - iOS Human Interface Guidelines 스타일
   - 또는 커스텀 스타일

2. **기본 색상 시스템**
   - 에듀테크에 적합한 색상 (파란색, 초록색 계열)

3. **반응형 레이아웃**
   - 모바일 우선 디자인

## 다음 단계

어떤 방법을 선택하시겠습니까?

1. **Figma 파일 공개 설정** → 가장 정확한 구현
2. **디자이너에게 스타일 가이드 요청** → 빠른 구현
3. **일반적인 디자인으로 먼저 구현** → 즉시 시작 가능 (나중에 Figma 디자인으로 교체)

---

**추천**: 해커톤 시간이 촉박하므로, **방법 3 (일반적인 디자인으로 먼저 구현)**으로 시작하고
Figma 접근이 가능해지면 나중에 업데이트하는 것을 권장합니다.
