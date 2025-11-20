// Figma에서 클릭 가능한 텍스트 컴포넌트
// 인터랙션이 설정된 텍스트 요소를 재현

import React from 'react';

interface ClickableTextProps {
  text: string;
  onClick: () => void;
  fontSize?: string;
  textAlign?: 'left' | 'center' | 'right';
  lineHeight?: string;
}

export const ClickableText: React.FC<ClickableTextProps> = ({
  text,
  onClick,
  fontSize = '128px',
  textAlign = 'left',
  lineHeight = '154.91px',
}) => {
  return (
    <div
      onClick={onClick}
      style={{
        // Figma에서 추출한 텍스트 스타일
        fontFamily: 'Inter, sans-serif',
        fontWeight: 400,
        fontSize: fontSize,
        color: '#000000',
        textAlign: textAlign,
        lineHeight: lineHeight,

        // 클릭 가능하도록 설정
        cursor: 'pointer',
        userSelect: 'none',
        WebkitUserSelect: 'none',
        WebkitTapHighlightColor: 'transparent',

        // 전환 효과 (Figma의 EASE_OUT)
        transition: 'opacity 300ms ease-out, transform 300ms ease-out',

        // 줄바꿈 처리
        whiteSpace: 'pre-line',
      }}
      onMouseEnter={(e) => {
        e.currentTarget.style.opacity = '0.7';
        e.currentTarget.style.transform = 'scale(0.98)';
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.opacity = '1';
        e.currentTarget.style.transform = 'scale(1)';
      }}
      onMouseDown={(e) => {
        e.currentTarget.style.transform = 'scale(0.95)';
      }}
      onMouseUp={(e) => {
        e.currentTarget.style.transform = 'scale(0.98)';
      }}
    >
      {text}
    </div>
  );
};

export default ClickableText;
