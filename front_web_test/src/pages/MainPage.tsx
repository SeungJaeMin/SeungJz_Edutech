// Figma 디자인: Main (Movie/Music/Talk 선택 화면)
// 파일: HT_SeungJz (qNH0gG3M2WGNwK1ygMhG5e)
// 노드: 48:1082

import React, { useState } from 'react';

interface MainPageProps {
  onSelectCategory?: (category: 'music' | 'movie' | 'talk') => void;
}

export const MainPage: React.FC<MainPageProps> = ({ onSelectCategory }) => {
  const [hoveredCard, setHoveredCard] = useState<string | null>(null);

  const handleCategoryClick = (category: 'music' | 'movie' | 'talk') => {
    if (onSelectCategory) {
      onSelectCategory(category);
    }
  };

  return (
    <div style={{
      width: '393px',
      height: '852px',
      // Figma 배경색: RGB(0.96, 0.98, 0.94)
      backgroundColor: '#F5F9F0',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      position: 'relative',
      overflow: 'hidden',
    }}>
      {/* 상단 로고 */}
      <div style={{
        marginTop: '97px',
        width: '240px',
        height: '240px',
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
            e.currentTarget.parentElement!.innerHTML = '<div style="font-size: 16px; color: #999;">Logo</div>';
          }}
        />
      </div>

      {/* 프로필 버튼 (우측 상단) */}
      <button
        style={{
          position: 'absolute',
          top: '50px',
          right: '34px',
          width: '50px',
          height: '50px',
          borderRadius: '50%',
          backgroundColor: '#FFFFFF',
          border: 'none',
          cursor: 'pointer',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
          fontSize: '36px',
          fontFamily: 'Inter, sans-serif',
          color: '#000000',
        }}
        onClick={() => console.log('Profile clicked')}
      >
        P
      </button>

      {/* 카테고리 카드 3개 */}
      <div style={{
        marginTop: '67px',
        display: 'flex',
        flexDirection: 'column',
        gap: '25px',
        alignItems: 'center',
      }}>
        {/* Music 카드 */}
        <button
          onClick={() => handleCategoryClick('music')}
          onMouseEnter={() => setHoveredCard('music')}
          onMouseLeave={() => setHoveredCard(null)}
          style={{
            width: '292px',
            height: '126px',
            backgroundColor: hoveredCard === 'music' ? '#8CC63F' : '#FFFFFF',
            border: 'none',
            borderRadius: '10px',
            cursor: 'pointer',
            boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            transition: 'all 0.3s ease-out',
            fontFamily: 'Fredoka, Inter, sans-serif',
            fontWeight: 600,
            fontSize: '36px',
            color: hoveredCard === 'music' ? '#FFFFFF' : '#000000',
            lineHeight: '43.56px',
          }}
        >
          Music
        </button>

        {/* Drama / Movie 카드 */}
        <button
          onClick={() => handleCategoryClick('movie')}
          onMouseEnter={() => setHoveredCard('movie')}
          onMouseLeave={() => setHoveredCard(null)}
          style={{
            width: '292px',
            height: '125px',
            backgroundColor: hoveredCard === 'movie' ? '#8CC63F' : '#FFFFFF',
            border: 'none',
            borderRadius: '10px',
            cursor: 'pointer',
            boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            transition: 'all 0.3s ease-out',
            fontFamily: 'Fredoka, Inter, sans-serif',
            fontWeight: 600,
            fontSize: '36px',
            color: hoveredCard === 'movie' ? '#FFFFFF' : '#000000',
            lineHeight: '43.56px',
            textAlign: 'center',
          }}
        >
          Drama / Movie
        </button>

        {/* Talk 카드 */}
        <button
          onClick={() => handleCategoryClick('talk')}
          onMouseEnter={() => setHoveredCard('talk')}
          onMouseLeave={() => setHoveredCard(null)}
          style={{
            width: '292px',
            height: '125px',
            backgroundColor: hoveredCard === 'talk' ? '#8CC63F' : '#FFFFFF',
            border: 'none',
            borderRadius: '10px',
            cursor: 'pointer',
            boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            transition: 'all 0.3s ease-out',
            fontFamily: 'Fredoka, Inter, sans-serif',
            fontWeight: 600,
            fontSize: '36px',
            color: hoveredCard === 'talk' ? '#FFFFFF' : '#000000',
            lineHeight: '43.56px',
          }}
        >
          Talk
        </button>
      </div>
    </div>
  );
};

export default MainPage;
