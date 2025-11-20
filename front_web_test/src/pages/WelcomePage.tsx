// Figma 디자인: iPhone 16 - 25 (Welcome 화면)
// 파일: HT_SeungJz (qNH0gG3M2WGNwK1ygMhG5e)
// 노드: 48:471

import React from 'react';
import { ColorBox } from '../components/ColorBox';

interface WelcomePageProps {
  onNavigate?: () => void;
}

export const WelcomePage: React.FC<WelcomePageProps> = ({ onNavigate }) => {
  return (
    <div style={{
      width: '393px',
      height: '852px',
      // Figma 배경색: #F5F9F0
      backgroundColor: '#F5F9F0',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      position: 'relative',
    }}>
      {/* 상단 로고/이미지 */}
      <div style={{
        position: 'absolute',
        top: '58px',
        width: '240px',
        height: '174px',
        backgroundColor: '#E8E8E8',
        borderRadius: '10px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: '14px',
        color: '#999',
      }}>
        Logo Image
      </div>

      {/* Welcome 텍스트 */}
      <h1 style={{
        // Figma 텍스트 스타일: Fredoka SemiBold 64px
        fontFamily: 'Fredoka, Inter, sans-serif',
        fontWeight: 600,
        fontSize: '64px',
        lineHeight: '77.44px',
        color: '#000000',
        marginBottom: '60px',
      }}>
        welcome!
      </h1>

      {/* 컬러 박스 3개 */}
      <div style={{
        position: 'absolute',
        bottom: '95px',
        display: 'flex',
        gap: '37px',
        alignItems: 'center',
      }}>
        {/* White Box with icon */}
        <ColorBox
          color="#FFFFFF"
          icon="/placeholder-icon-1.png"
          onClick={onNavigate}
        />

        {/* Black Box with icon */}
        <ColorBox
          color="#000000"
          icon="/placeholder-icon-2.png"
          onClick={onNavigate}
        />

        {/* Blue Box with icon - Figma: #1877F2 */}
        <ColorBox
          color="#1877F2"
          icon="/placeholder-icon-3.png"
          onClick={onNavigate}
        />
      </div>
    </div>
  );
};

export default WelcomePage;
