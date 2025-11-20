// Figma에서 추출한 컬러 박스 디자인
// 파일: HT_SeungJz (qNH0gG3M2WGNwK1ygMhG5e)
// 노드: Rectangle 36/37/38 (id: 48:473, 48:474, 48:475)

import React from 'react';

interface ColorBoxProps {
  color: string;
  icon?: string;
  onClick?: () => void;
}

export const ColorBox: React.FC<ColorBoxProps> = ({ color, icon, onClick }) => {
  return (
    <button
      onClick={onClick}
      style={{
        // Figma 디자인: 75x75 정사각형
        width: '75px',
        height: '75px',
        backgroundColor: color,
        border: 'none',
        borderRadius: '10px',
        cursor: 'pointer',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        transition: 'transform 0.3s ease-out, opacity 0.3s ease-out',

        // Figma shadow: DROP_SHADOW
        boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
      }}
      onMouseEnter={(e) => {
        e.currentTarget.style.transform = 'scale(1.05)';
        e.currentTarget.style.opacity = '0.9';
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.transform = 'scale(1)';
        e.currentTarget.style.opacity = '1';
      }}
    >
      {/* 아이콘 이미지 */}
      {icon && (
        <img
          src={icon}
          alt="icon"
          style={{
            width: '50px',
            height: '50px',
            objectFit: 'contain',
          }}
        />
      )}
    </button>
  );
};

export default ColorBox;
