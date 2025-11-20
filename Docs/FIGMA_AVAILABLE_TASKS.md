# Figma MCP로 할 수 있는 작업들

## Figma 파일 정보
**URL**: https://www.figma.com/design/qNH0gG3M2WGNwK1ygMhG5e/HT_SeungJz?node-id=0-1&t=RjAIdSugggeLbIVU-1

**상태**: ✓ Figma MCP 연결 완료

---

## 1. 디자인 분석 및 정보 추출

### 1-1. 전체 프로젝트 구조 파악
```
"Figma 파일 https://www.figma.com/design/qNH0gG3M2WGNwK1ygMhG5e/HT_SeungJz 를 분석해서:
1. 전체 페이지 목록
2. 각 페이지의 프레임/화면 목록
3. 사용된 컴포넌트 목록
4. 프로젝트 전체 구조를 정리해줘"
```

### 1-2. 디자인 시스템 추출
```
"Figma 파일에서 디자인 시스템을 추출해줘:
1. 색상 팔레트 (Primary, Secondary, Background, Text 등)
2. 타이포그래피 (폰트 크기, 굵기, 행간)
3. 간격 시스템 (Spacing/Padding)
4. 그림자, 테두리 등 공통 스타일
→ React Native의 theme.js 파일로 만들어줘"
```

### 1-3. 아이콘 및 이미지 정보
```
"Figma 파일에서 사용된 모든 아이콘과 이미지를 리스트업하고,
어떤 아이콘 라이브러리를 사용하면 좋을지 추천해줘"
```

---

## 2. 코드 생성 (모바일 앱)

### 2-1. 화면 단위 컴포넌트 생성

#### React Native
```
"Figma 파일의 '[화면명]' 프레임을 React Native 컴포넌트로 변환해줘:
- 함수형 컴포넌트 사용
- StyleSheet.create로 스타일 분리
- TypeScript 사용
- Props 타입 정의 포함"
```

#### Flutter
```
"Figma 파일의 '[화면명]' 프레임을 Flutter 위젯으로 변환해줘:
- StatefulWidget 또는 StatelessWidget
- 반응형 레이아웃 적용"
```

### 2-2. 재사용 가능한 컴포넌트 생성
```
"Figma의 '[컴포넌트명]' 컴포넌트를 React Native로 만들어줘:
- Props로 커스터마이징 가능하게
- 다양한 상태 지원 (default, pressed, disabled 등)
- 접근성 고려"
```

예시:
- 버튼 (Primary, Secondary, Text 버튼)
- 입력 필드 (TextInput, 검색창)
- 카드 컴포넌트
- 네비게이션 바
- 탭 바
- 리스트 아이템

### 2-3. 전체 화면 프로토타입
```
"Figma의 모든 주요 화면을 React Native로 변환하고
React Navigation으로 연결해서 동작하는 프로토타입 만들어줘"
```

---

## 3. 레이아웃 및 스타일링

### 3-1. Flexbox 레이아웃 변환
```
"Figma의 Auto Layout을 React Native의 Flexbox로 정확히 변환해줘"
```

### 3-2. 반응형 디자인
```
"이 화면을 다양한 화면 크기에 대응하도록 반응형으로 만들어줘:
- 스마트폰 (작은 화면)
- 태블릿
- 가로/세로 모드 대응"
```

### 3-3. 다크 모드 지원
```
"Figma에 다크 모드 디자인이 있다면 추출하고,
라이트/다크 모드 토글 기능을 추가해줘"
```

---

## 4. 인터랙션 및 애니메이션

### 4-1. 화면 전환 애니메이션
```
"Figma의 프로토타입 인터랙션을 보고
React Native Animated 또는 Reanimated로 구현해줘"
```

### 4-2. 버튼 및 탭 애니메이션
```
"Figma의 버튼 hover/press 상태를 React Native의
터치 피드백으로 구현해줘 (TouchableOpacity 등)"
```

### 4-3. 로딩 및 상태 표시
```
"로딩 스피너, 프로그레스 바, 스켈레톤 UI 등을
Figma 디자인에 맞춰 구현해줘"
```

---

## 5. 특정 UI 패턴 구현

### 5-1. 폼 및 입력
```
"로그인/회원가입 폼을 Figma 디자인에 맞춰 구현하고
다음 기능 추가:
- 유효성 검사
- 에러 메시지 표시
- 자동 완성
- 비밀번호 보기/숨기기"
```

### 5-2. 리스트 및 그리드
```
"Figma의 리스트/그리드 디자인을 FlatList/SectionList로 구현하고:
- 무한 스크롤
- Pull to refresh
- 빈 상태 처리"
```

### 5-3. 모달 및 바텀시트
```
"Figma의 모달/바텀시트 디자인을 구현하고
열기/닫기 애니메이션 추가"
```

### 5-4. 탭 네비게이션
```
"Figma의 탭 바 디자인을 React Navigation의
Bottom Tab Navigator로 구현"
```

---

## 6. 접근성 및 사용성

### 6-1. 접근성 속성 추가
```
"생성된 컴포넌트에 접근성 속성 추가:
- accessibilityLabel
- accessibilityHint
- accessibilityRole
- 스크린 리더 지원"
```

### 6-2. 터치 영역 최적화
```
"모든 터치 가능한 요소의 최소 크기를 44x44pt로 보장"
```

---

## 7. 개발 효율화

### 7-1. Storybook 생성
```
"생성된 컴포넌트들을 Storybook으로 문서화하고
다양한 상태를 스토리로 만들어줘"
```

### 7-2. 스타일 가이드 문서
```
"Figma 디자인 시스템을 기반으로 개발자용 스타일 가이드 문서 작성"
```

### 7-3. 컴포넌트 테스트
```
"생성된 컴포넌트에 대한 Jest/React Native Testing Library 테스트 코드 작성"
```

---

## 8. Figma → 코드 실전 예시

### 예시 1: 로그인 화면 전체 구현
```
"Figma 파일 https://www.figma.com/design/qNH0gG3M2WGNwK1ygMhG5e/HT_SeungJz
에서 로그인 화면을 찾아서 다음을 생성해줘:

1. LoginScreen.tsx - 메인 화면 컴포넌트
2. components/EmailInput.tsx - 이메일 입력 필드
3. components/PasswordInput.tsx - 비밀번호 입력 필드
4. components/LoginButton.tsx - 로그인 버튼
5. styles/loginStyles.ts - 스타일 정의
6. utils/validation.ts - 폼 검증 로직
7. types/login.types.ts - TypeScript 타입 정의

요구사항:
- React Native + TypeScript
- React Hook Form 사용
- 실시간 유효성 검사
- 에러 메시지 표시
- 로딩 상태 처리
- 키보드 자동 숨김"
```

### 예시 2: 홈 화면 리스트
```
"Figma의 홈 화면에 있는 카드 리스트를 구현해줘:

1. HomeScreen.tsx - FlatList로 구현
2. components/CourseCard.tsx - 카드 컴포넌트
3. API 연동 준비 (Mock 데이터 포함)
4. 스켈레톤 로딩 UI
5. 빈 상태 처리
6. Pull to refresh
7. 카드 탭 시 상세 화면 이동"
```

### 예시 3: 네비게이션 전체 설정
```
"Figma의 모든 화면을 분석하고 React Navigation 구조 설계:

1. 어떤 네비게이터를 사용할지 (Stack, Tab, Drawer)
2. 화면 간 흐름도
3. Navigation 설정 파일 생성
4. 타입 안전한 네비게이션 (TypeScript)
5. 화면 전환 애니메이션"
```

---

## 9. AI 기능 통합을 위한 UI

### 9-1. AI 채팅 인터페이스
```
"Figma에 채팅 화면이 있다면 구현하고, 없다면 에듀테크에 맞는
AI 튜터 채팅 인터페이스를 디자인에 맞춰 생성해줘:
- 메시지 말풍선
- 타이핑 인디케이터
- 이미지/파일 첨부
- 음성 입력 버튼"
```

### 9-2. AI 추천 UI
```
"AI 학습 추천 카드를 Figma 디자인 스타일에 맞춰 구현"
```

---

## 10. 해커톤 특화 기능

### 10-1. 빠른 프로토타이핑
```
"해커톤용 MVP를 위해 Figma의 핵심 3개 화면만 빠르게 구현:
1. 온보딩/로그인
2. 메인 기능 화면
3. 프로필/설정

더미 데이터로 동작하게 하고, 나중에 API 연동 가능하게 구조화"
```

### 10-2. 데모용 인터랙션
```
"실제 기능 없이 UI만 동작하는 데모 버전 생성
(버튼 누르면 다음 화면 이동, 더미 데이터 표시 등)"
```

---

## 실제 사용 시 팁

### 효과적인 프롬프트 작성법

1. **구체적으로 요청**
   ```
   ❌ "이 화면 만들어줘"
   ✅ "Figma의 'Login Screen' 프레임을 React Native 컴포넌트로 만들되,
       TypeScript 사용하고 React Hook Form으로 폼 관리해줘"
   ```

2. **기술 스택 명시**
   ```
   "React Native + TypeScript + Expo 환경에서 동작하게"
   ```

3. **우선순위 설정**
   ```
   "일단 UI만 먼저 구현하고, API 연동은 나중에 추가할게"
   ```

4. **단계별 요청**
   ```
   "1단계: 디자인 시스템 추출
    2단계: 공통 컴포넌트 5개 생성
    3단계: 메인 화면 조합"
   ```

---

## 다음 단계

- [ ] Figma 파일 전체 구조 파악
- [ ] 디자인 시스템 추출
- [ ] 공통 컴포넌트 생성
- [ ] 주요 화면 3개 구현
- [ ] 네비게이션 설정
- [ ] API 연동 준비

---

**준비 완료!** 이제 위의 작업들을 Claude Code에게 요청하면 됩니다.
