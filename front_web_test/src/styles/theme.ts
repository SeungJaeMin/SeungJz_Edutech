// 에듀테크 앱 디자인 시스템
// Figma 디자인이 확정되면 여기서 색상/스타일을 일괄 변경 가능

export const colors = {
  // Primary - 학습/교육을 상징하는 블루 계열
  primary: {
    main: '#4F46E5',      // Indigo-600
    light: '#818CF8',     // Indigo-400
    dark: '#3730A3',      // Indigo-800
    contrast: '#FFFFFF',
  },

  // Secondary - 성공/완료를 나타내는 그린 계열
  secondary: {
    main: '#10B981',      // Emerald-500
    light: '#34D399',     // Emerald-400
    dark: '#059669',      // Emerald-600
    contrast: '#FFFFFF',
  },

  // 배경색
  background: {
    primary: '#FFFFFF',
    secondary: '#F9FAFB',  // Gray-50
    tertiary: '#F3F4F6',   // Gray-100
  },

  // 텍스트
  text: {
    primary: '#111827',    // Gray-900
    secondary: '#6B7280',  // Gray-500
    disabled: '#9CA3AF',   // Gray-400
    inverse: '#FFFFFF',
  },

  // 상태 색상
  status: {
    success: '#10B981',    // Green
    warning: '#F59E0B',    // Amber
    error: '#EF4444',      // Red
    info: '#3B82F6',       // Blue
  },

  // 테두리
  border: {
    light: '#E5E7EB',      // Gray-200
    medium: '#D1D5DB',     // Gray-300
    dark: '#9CA3AF',       // Gray-400
  },

  // AI 관련 (특별한 강조가 필요한 경우)
  ai: {
    gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    main: '#8B5CF6',       // Purple-500
    light: '#A78BFA',      // Purple-400
  },
};

export const typography = {
  // 제목
  h1: {
    fontSize: '32px',
    fontWeight: '700',
    lineHeight: '40px',
    letterSpacing: '-0.02em',
  },
  h2: {
    fontSize: '24px',
    fontWeight: '600',
    lineHeight: '32px',
    letterSpacing: '-0.01em',
  },
  h3: {
    fontSize: '20px',
    fontWeight: '600',
    lineHeight: '28px',
  },
  h4: {
    fontSize: '18px',
    fontWeight: '600',
    lineHeight: '26px',
  },

  // 본문
  body: {
    fontSize: '16px',
    fontWeight: '400',
    lineHeight: '24px',
  },
  bodySmall: {
    fontSize: '14px',
    fontWeight: '400',
    lineHeight: '20px',
  },

  // 캡션
  caption: {
    fontSize: '12px',
    fontWeight: '400',
    lineHeight: '16px',
  },

  // 버튼
  button: {
    fontSize: '16px',
    fontWeight: '600',
    lineHeight: '24px',
    letterSpacing: '0.01em',
  },
  buttonSmall: {
    fontSize: '14px',
    fontWeight: '600',
    lineHeight: '20px',
  },
};

export const spacing = {
  xs: '4px',
  sm: '8px',
  md: '16px',
  lg: '24px',
  xl: '32px',
  xxl: '48px',
};

export const borderRadius = {
  none: '0',
  sm: '4px',
  md: '8px',
  lg: '12px',
  xl: '16px',
  full: '9999px',
};

export const shadows = {
  sm: '0 1px 2px 0 rgba(0, 0, 0, 0.05)',
  md: '0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)',
  lg: '0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)',
  xl: '0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)',
};

export const transitions = {
  fast: '150ms ease-in-out',
  normal: '300ms ease-in-out',
  slow: '500ms ease-in-out',
};

// 모바일 반응형 브레이크포인트
export const breakpoints = {
  mobile: '320px',
  tablet: '768px',
  desktop: '1024px',
  wide: '1280px',
};

export const theme = {
  colors,
  typography,
  spacing,
  borderRadius,
  shadows,
  transitions,
  breakpoints,
};

export default theme;
