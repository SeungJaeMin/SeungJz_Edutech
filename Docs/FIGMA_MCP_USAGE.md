# Figma MCP 실전 사용 가이드

## 설치 완료 확인 ✓

Figma MCP 서버가 성공적으로 설치되었습니다!

```
figma: https://mcp.figma.com/mcp (HTTP) - ⚠ Needs authentication
```

---

## 다음 단계: 인증하기

### 방법 1: Claude Code UI에서 인증 (권장)

1. Claude Code를 재시작하거나 새 채팅 시작
2. 다음 명령어 입력:
   ```
   /mcp
   ```
3. MCP 서버 목록에서 `figma` 선택
4. `Authenticate` 버튼 클릭
5. 브라우저에서 Figma 로그인 및 `Allow Access` 클릭

### 방법 2: Figma Personal Access Token 사용

Figma Personal Access Token을 발급받았다면:
```bash
claude mcp auth figma --token YOUR_FIGMA_TOKEN
```

---

## 실전 사용 예시

### 예시 1: 간단한 버튼 컴포넌트 생성

**디자이너가 Figma에 버튼 디자인 완성 후 URL 공유**

```
Claude에게 요청:
"https://www.figma.com/file/abc123/MobileApp 파일의
'Primary Button' 컴포넌트를 React Native로 변환해줘.
TouchableOpacity를 사용하고, props로 title과 onPress를 받게 해줘."
```

**예상 결과:**
```jsx
// components/PrimaryButton.jsx
import React from 'react';
import { TouchableOpacity, Text, StyleSheet } from 'react-native';

const PrimaryButton = ({ title, onPress }) => {
  return (
    <TouchableOpacity style={styles.button} onPress={onPress}>
      <Text style={styles.text}>{title}</Text>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  button: {
    backgroundColor: '#007AFF',
    paddingVertical: 16,
    paddingHorizontal: 32,
    borderRadius: 8,
    alignItems: 'center',
  },
  text: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '600',
  },
});

export default PrimaryButton;
```

---

### 예시 2: 로그인 화면 전체 생성

**요청:**
```
"Figma 파일 [URL]의 'Login Screen'을 분석해서:
1. React Native 화면 컴포넌트 생성
2. 이메일, 비밀번호 입력 필드 포함
3. 로그인 버튼 포함
4. 폼 검증 로직 추가
5. React Navigation 연동 가능하게"
```

**예상 구조:**
```
screens/
  LoginScreen.jsx
components/
  EmailInput.jsx
  PasswordInput.jsx
  LoginButton.jsx
styles/
  loginStyles.js
utils/
  validation.js
```

---

### 예시 3: 디자인 시스템 추출

**요청:**
```
"Figma 파일 [URL]의 Design System 페이지에서
색상 팔레트와 타이포그래피를 추출해서
React Native의 theme.js 파일로 만들어줘"
```

**예상 결과:**
```javascript
// styles/theme.js
export const colors = {
  primary: '#007AFF',
  secondary: '#5856D6',
  success: '#34C759',
  warning: '#FF9500',
  error: '#FF3B30',
  background: '#F2F2F7',
  text: '#000000',
  textSecondary: '#8E8E93',
  border: '#C6C6C8',
  white: '#FFFFFF',
};

export const typography = {
  h1: {
    fontSize: 32,
    fontWeight: '700',
    lineHeight: 40,
  },
  h2: {
    fontSize: 24,
    fontWeight: '600',
    lineHeight: 32,
  },
  body: {
    fontSize: 16,
    fontWeight: '400',
    lineHeight: 24,
  },
  caption: {
    fontSize: 12,
    fontWeight: '400',
    lineHeight: 16,
  },
};

export const spacing = {
  xs: 4,
  sm: 8,
  md: 16,
  lg: 24,
  xl: 32,
};
```

---

### 예시 4: 반응형 레이아웃

**요청:**
```
"Figma의 'Home Screen'을 React Native로 변환하되,
화면 크기에 따라 2열/3열 그리드로 변경되는 반응형 레이아웃으로 만들어줘"
```

---

## Claude Code에서 Figma 파일 작업하는 실전 팁

### 1. 프로젝트 초기 설정 시

```
"Figma 파일 [URL]을 분석해서 다음을 생성해줘:
1. 전체 프로젝트 구조 파악
2. 필요한 스크린 목록
3. 재사용 가능한 컴포넌트 목록
4. 디자인 시스템 (colors, typography, spacing)
5. 프로젝트 폴더 구조 제안"
```

### 2. 화면 단위 작업 시

```
"지금부터 [화면명] 작업을 시작할게.
Figma의 '[Frame 이름]'을 보고 다음 순서로 작업해줘:
1. 먼저 필요한 하위 컴포넌트들 생성
2. 메인 화면 컴포넌트 조합
3. 네비게이션 연동
4. API 연동 준비 (Mock 데이터 포함)"
```

### 3. 디자인 변경 대응

```
"디자이너가 Figma에서 [컴포넌트명]을 수정했어.
현재 코드와 Figma 디자인을 비교해서 변경사항을 알려주고,
필요한 코드 수정을 제안해줘"
```

---

## 해커톤에서 효율적으로 활용하기

### 시간 절약 전략

#### Phase 1: 준비 (해커톤 시작 전 - 지금!)
```
Claude에게:
"Figma 파일 [URL]에서:
1. 디자인 시스템 추출 → theme.js
2. 공통 컴포넌트 5개 생성 (Button, Input, Card, Header, Footer)
3. 기본 네비게이션 구조 설정"
```
**예상 시간**: 30분

#### Phase 2: 빠른 프로토타입 (해커톤 초반 2시간)
```
Claude에게:
"주요 화면 3개를 Figma에서 가져와서 기본 기능 동작하는 프로토타입 만들어줘:
1. 홈 화면 - 더미 데이터로 표시
2. 상세 화면 - 네비게이션 연동
3. 설정 화면 - 기본 UI만"
```
**예상 시간**: 1-2시간

#### Phase 3: 기능 구현 (해커톤 중반)
```
"이제 실제 기능을 추가할게:
1. [화면명]에 API 연동
2. 상태 관리 추가 (Context API/Redux)
3. 폼 검증 로직
4. 에러 핸들링"
```

#### Phase 4: 완성도 높이기 (해커톤 후반)
```
"UI 개선 작업:
1. 로딩 애니메이션 추가
2. 에러 메시지 표시 개선
3. 터치 피드백 추가
4. 반응형 레이아웃 최적화"
```

---

## 실전 워크플로우 예시

### 상황: 디자이너가 로그인 화면 Figma 완성

1. **디자이너**: Figma 링크 공유
   ```
   "로그인 화면 완성했어요!
   https://www.figma.com/file/xyz789/Login"
   ```

2. **개발자 → Claude**:
   ```
   "위 Figma 링크에서 로그인 화면 컴포넌트 생성해줘.
   - React Native
   - 이메일/비밀번호 입력
   - 로그인 버튼
   - 회원가입 링크
   - 소셜 로그인 버튼 3개 (Google, Apple, Kakao)"
   ```

3. **Claude**: 코드 생성 (5-10분)

4. **개발자**: 코드 검토 및 테스트 (10-15분)

5. **통합 및 연동**: API 연결, 네비게이션 설정 (15-20분)

**총 소요 시간**: 약 30-45분 (수작업 대비 2-3시간 절약)

---

## 자주 사용하는 프롬프트 모음

### 컴포넌트 생성
```
"Figma [URL]의 [컴포넌트명]을 React Native 컴포넌트로 변환해줘"
```

### 화면 전체 생성
```
"Figma [URL]의 [Screen명]을 분석하고 완전한 React Native 화면으로 만들어줘"
```

### 스타일만 추출
```
"Figma [URL]의 [컴포넌트명]에서 스타일 정보만 추출해서 StyleSheet로 만들어줘"
```

### 반응형 대응
```
"이 컴포넌트를 태블릿 크기에서도 잘 보이게 반응형으로 수정해줘"
```

### 애니메이션 추가
```
"Figma의 인터랙션을 참고해서 이 컴포넌트에 애니메이션 추가해줘"
```

---

## 트러블슈팅

### 문제: Figma 파일이 너무 크거나 복잡함
**해결책**:
- 특정 Frame이나 Component만 지정해서 요청
- 화면을 섹션별로 나눠서 작업

### 문제: 디자인과 코드가 정확히 일치하지 않음
**해결책**:
- Figma에서 Auto Layout 사용 권장
- 픽셀 단위보다는 비율/flexbox 개념으로 접근
- Claude에게 "Figma 디자인과 최대한 동일하게" 명시

### 문제: 특정 UI 요소 변환이 어려움
**해결책**:
- 커스텀 컴포넌트가 필요한지 확인
- 서드파티 라이브러리 활용 (react-native-vector-icons 등)
- Claude에게 대안 제시 요청

---

## 체크리스트

### Figma MCP 설정 완료
- [x] MCP 서버 설치
- [ ] Figma 계정 인증
- [ ] 테스트 파일로 연동 확인

### 해커톤 준비
- [ ] 디자이너로부터 Figma 파일 URL 받기
- [ ] Figma 파일 접근 권한 확인
- [ ] 디자인 시스템 추출 테스트
- [ ] 샘플 컴포넌트 1개 생성 테스트

---

## 다음 단계

1. **지금 바로**: `/mcp` 명령어로 Figma 인증 완료
2. **디자이너 연락**: Figma 파일 URL 요청
3. **테스트**: 간단한 컴포넌트 1개 생성해보기
4. **프로젝트 구조**: 디자인 시스템 추출 및 기본 컴포넌트 생성

---

**팁**: 해커톤 시작 전에 최소 1-2개의 화면을 Figma에서 코드로 변환하는 테스트를 꼭 해보세요. 실전에서 시간을 크게 절약할 수 있습니다!
