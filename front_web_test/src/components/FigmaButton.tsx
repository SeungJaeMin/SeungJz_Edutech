// Figma에서 추출한 실제 버튼 디자인
// 파일: HT_SeungJz (qNH0gG3M2WGNwK1ygMhG5e)
// 노드: "다시 집어넣기" (id: 1:7)

import React from 'react';

interface FigmaButtonProps {
  text: string;
  onClick: () => void;
}

export const FigmaButton: React.FC<FigmaButtonProps> = ({ text, onClick }) => {
  return (
    <button
      onClick={onClick}
      style={{
        // Figma에서 추출한 정확한 스타일
        fontFamily: 'Inter, sans-serif',
        fontWeight: 400,
        fontSize: '40px',
        color: '#000000',
        textAlign: 'center',
        lineHeight: '48.41px',

        // 버튼 레이아웃
        width: '233px',
        height: '48px',
        padding: '0',
        border: 'none',
        background: 'transparent',
        cursor: 'pointer',

        // 전환 효과
        transition: 'opacity 0.3s ease-out',
      }}
      onMouseEnter={(e) => {
        e.currentTarget.style.opacity = '0.7';
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.opacity = '1';
      }}
    >
      {text}
    </button>
  );
};

// 추가: 모바일 친화적인 버전
export const FigmaButtonMobile: React.FC<FigmaButtonProps> = ({ text, onClick }) => {
  return (
    <button
      onClick={onClick}
      style={{
        // Figma 디자인 유지하되 모바일에 최적화
        fontFamily: 'Inter, sans-serif',
        fontWeight: 400,
        fontSize: '24px', // 모바일용으로 축소
        color: '#000000',
        textAlign: 'center',
        lineHeight: '32px',

        // 반응형 크기
        width: '80%',
        maxWidth: '233px',
        height: 'auto',
        padding: '12px 24px',
        border: 'none',
        background: 'transparent',
        cursor: 'pointer',

        // 터치 최적화
        minHeight: '44px', // iOS 터치 가이드라인
        WebkitTapHighlightColor: 'transparent',

        // 전환 효과
        transition: 'opacity 0.3s ease-out',
      }}
      onMouseEnter={(e) => {
        e.currentTarget.style.opacity = '0.7';
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.opacity = '1';
      }}
    >
      {text}
    </button>
  );
};

export default FigmaButton;
