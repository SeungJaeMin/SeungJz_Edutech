// Figma 디자인: iPhone 16 - 26 (언어 선택 화면)
// 파일: HT_SeungJz (qNH0gG3M2WGNwK1ygMhG5e)
// 노드: 48:480

import React, { useState } from 'react';
import { LanguageButton } from '../components/LanguageButton';

interface Language {
  id: string;
  name: string;
  flagUrl?: string;
}

const languages: Language[] = [
  {
    id: 'korean',
    name: 'Korean',
    flagUrl: 'https://flagcdn.com/w320/kr.png'
  },
  {
    id: 'japanese',
    name: 'Japanese',
    flagUrl: 'https://flagcdn.com/w320/jp.png'
  },
  {
    id: 'english',
    name: 'English',
    flagUrl: 'https://flagcdn.com/w320/us.png'
  },
  {
    id: 'chinese',
    name: 'Chinese',
    flagUrl: 'https://flagcdn.com/w320/cn.png'
  },
  {
    id: 'spanish',
    name: 'Spanish',
    flagUrl: 'https://flagcdn.com/w320/es.png'
  },
  {
    id: 'french',
    name: 'French',
    flagUrl: 'https://flagcdn.com/w320/fr.png'
  },
];

export const LanguageSelectionPage: React.FC = () => {
  const [selectedLanguage, setSelectedLanguage] = useState<string | null>(null);

  const handleContinue = () => {
    if (selectedLanguage) {
      console.log('Selected language:', selectedLanguage);
      // TODO: 다음 화면으로 이동
      alert(`Selected: ${selectedLanguage}`);
    }
  };

  return (
    <div style={{
      width: '393px',
      height: '852px',
      // Figma 배경색: #F5F9F0
      backgroundColor: '#F5F9F0',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      position: 'relative',
      overflow: 'hidden',
    }}>
      {/* 상단 로고/이미지 */}
      <div style={{
        marginTop: '35px',
        width: '138px',
        height: '100px',
        backgroundColor: '#E8E8E8',
        borderRadius: '10px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: '12px',
        color: '#999',
      }}>
        Logo
      </div>

      {/* 제목 텍스트 */}
      <h1 style={{
        // Figma 텍스트 스타일: Fredoka SemiBold 30px
        fontFamily: 'Fredoka, Inter, sans-serif',
        fontWeight: 600,
        fontSize: '30px',
        lineHeight: '36.3px',
        color: '#000000',
        textAlign: 'center',
        marginTop: '27px',
        marginBottom: '41px',
      }}>
        Which language<br />are you interested in?
      </h1>

      {/* 언어 선택 버튼 리스트 */}
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        gap: '25px',
        alignItems: 'center',
      }}>
        {languages.map((language) => (
          <LanguageButton
            key={language.id}
            language={language.name}
            flagUrl={language.flagUrl}
            onClick={() => setSelectedLanguage(language.id)}
            selected={selectedLanguage === language.id}
          />
        ))}
      </div>

      {/* CONTINUE 버튼 */}
      <button
        onClick={handleContinue}
        disabled={!selectedLanguage}
        style={{
          // Figma Rectangle 45
          position: 'absolute',
          bottom: '75px',
          width: '300px',
          height: '75px',
          backgroundColor: selectedLanguage ? '#8CC63F' : '#CCCCCC',
          border: 'none',
          borderRadius: '10px',
          cursor: selectedLanguage ? 'pointer' : 'not-allowed',

          // Figma shadow
          boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',

          // 텍스트 스타일: Fredoka SemiBold 30px
          fontFamily: 'Fredoka, Inter, sans-serif',
          fontWeight: 600,
          fontSize: '30px',
          color: '#FFFFFF',
          lineHeight: '36.3px',

          transition: 'all 0.3s ease-out',
        }}
        onMouseEnter={(e) => {
          if (selectedLanguage) {
            e.currentTarget.style.backgroundColor = '#7AB52F';
          }
        }}
        onMouseLeave={(e) => {
          if (selectedLanguage) {
            e.currentTarget.style.backgroundColor = '#8CC63F';
          }
        }}
      >
        CONTINUE
      </button>
    </div>
  );
};

export default LanguageSelectionPage;
