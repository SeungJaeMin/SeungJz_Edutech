// Figma 디자인: iPhone 16 - 24 (초기 화면)
// 파일: HT_SeungJz (qNH0gG3M2WGNwK1ygMhG5e)
// 노드: 48:469

import React from 'react';

interface InitialPageProps {
  onNavigate?: () => void;
}

export const InitialPage: React.FC<InitialPageProps> = ({ onNavigate }) => {
  return (
    <div
      onClick={onNavigate}
      style={{
        width: '393px',
        height: '852px',
        // Figma 배경색: #F5F9F0
        backgroundColor: '#F5F9F0',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        cursor: 'pointer',
        position: 'relative',
        transition: 'opacity 0.3s ease-out',
      }}
      onMouseEnter={(e) => {
        e.currentTarget.style.opacity = '0.95';
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.opacity = '1';
      }}
    >
      {/* 중앙 이미지 - Figma: 240x240 */}
      <div style={{
        width: '240px',
        height: '240px',
        backgroundColor: '#E8E8E8',
        borderRadius: '10px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: '14px',
        color: '#999',
      }}>
        App Logo
      </div>

      {/* 클릭 힌트 */}
      <div style={{
        position: 'absolute',
        bottom: '40px',
        fontSize: '14px',
        color: '#999',
        fontFamily: 'Inter, sans-serif',
      }}>
        Tap to continue
      </div>
    </div>
  );
};

export default InitialPage;
