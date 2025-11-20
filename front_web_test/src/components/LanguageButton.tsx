// Figma에서 추출한 언어 선택 버튼 디자인
// 파일: HT_SeungJz (qNH0gG3M2WGNwK1ygMhG5e)

import React from 'react';

interface LanguageButtonProps {
  language: string;
  flagUrl?: string;
  onClick?: () => void;
  selected?: boolean;
}

export const LanguageButton: React.FC<LanguageButtonProps> = ({
  language,
  flagUrl,
  onClick,
  selected = false
}) => {
  return (
    <button
      onClick={onClick}
      style={{
        // Figma 디자인: Rectangle 39/40/41
        width: '300px',
        height: '50px',
        backgroundColor: selected ? '#8CC63F' : '#FFFFFF',
        border: 'none',
        borderRadius: '10px',
        cursor: 'pointer',
        display: 'flex',
        alignItems: 'center',
        padding: '0 10px',
        gap: '15px',
        transition: 'all 0.3s ease-out',

        // Figma shadow: DROP_SHADOW
        boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
      }}
      onMouseEnter={(e) => {
        if (!selected) {
          e.currentTarget.style.backgroundColor = '#F5F5F5';
        }
      }}
      onMouseLeave={(e) => {
        if (!selected) {
          e.currentTarget.style.backgroundColor = '#FFFFFF';
        }
      }}
    >
      {/* 국기 이미지 */}
      {flagUrl && (
        <div style={{
          width: '45px',
          height: '30px',
          borderRadius: '5px',
          overflow: 'hidden',
          flexShrink: 0,
        }}>
          <img
            src={flagUrl}
            alt={`${language} flag`}
            style={{
              width: '100%',
              height: '100%',
              objectFit: 'cover',
            }}
          />
        </div>
      )}

      {/* 언어 텍스트 */}
      <span style={{
        // Figma 텍스트 스타일: Fredoka Medium 24px
        fontFamily: 'Fredoka, Inter, sans-serif',
        fontWeight: 500,
        fontSize: '24px',
        color: selected ? '#FFFFFF' : '#000000',
        lineHeight: '29.04px',
      }}>
        {language}
      </span>
    </button>
  );
};

export default LanguageButton;
