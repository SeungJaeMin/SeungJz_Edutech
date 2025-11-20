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
        borderRadius: '10px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        overflow: 'hidden',
      }}>
        <img
          src="/img/Abocado_Logo.png"
          alt="Abocado Logo"
          style={{
            width: '100%',
            height: '100%',
            objectFit: 'contain',
          }}
          onError={(e) => {
            e.currentTarget.style.display = 'none';
            e.currentTarget.parentElement!.style.backgroundColor = '#E8E8E8';
            e.currentTarget.parentElement!.innerHTML = '<div style="font-size: 14px; color: #999;">Logo Image</div>';
          }}
        />
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

      {/* 컬러 박스 3개 - 소셜 로그인 */}
      <div style={{
        position: 'absolute',
        bottom: '95px',
        display: 'flex',
        gap: '37px',
        alignItems: 'center',
      }}>
        {/* Google Login */}
        <ColorBox
          color="#FFFFFF"
          icon="/img/google.png"
          onClick={onNavigate}
        />

        {/* Apple Login */}
        <ColorBox
          color="#000000"
          icon="/img/apple.png"
          onClick={onNavigate}
        />

        {/* Facebook Login */}
        <ColorBox
          color="#1877F2"
          icon="/img/facebook.png"
          onClick={onNavigate}
        />
      </div>
    </div>
  );
};

export default WelcomePage;
