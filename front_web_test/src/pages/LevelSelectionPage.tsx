// Figma 디자인: 실력 선택 화면
// 파일: HT_SeungJz (qNH0gG3M2WGNwK1ygMhG5e)

import React, { useState } from 'react';

interface LevelSelectionPageProps {
  onNavigate?: () => void;
}

interface Level {
  id: string;
  name: string;
  description: string;
}

const levels: Level[] = [
  {
    id: 'beginner',
    name: 'Beginner',
    description: 'Just starting out'
  },
  {
    id: 'intermediate',
    name: 'Intermediate',
    description: 'Some experience'
  },
  {
    id: 'advanced',
    name: 'Advanced',
    description: 'Fluent speaker'
  },
];

export const LevelSelectionPage: React.FC<LevelSelectionPageProps> = ({ onNavigate }) => {
  const [selectedLevel, setSelectedLevel] = useState<string | null>(null);

  const handleContinue = () => {
    if (selectedLevel && onNavigate) {
      console.log('Selected level:', selectedLevel);
      onNavigate();
    }
  };

  return (
    <div style={{
      width: '393px',
      height: '852px',
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
        fontFamily: 'Fredoka, Inter, sans-serif',
        fontWeight: 600,
        fontSize: '30px',
        lineHeight: '36.3px',
        color: '#000000',
        textAlign: 'center',
        marginTop: '27px',
        marginBottom: '41px',
      }}>
        What's your<br />current level?
      </h1>

      {/* 레벨 선택 버튼 리스트 */}
      <div style={{
        display: 'flex',
        flexDirection: 'column',
        gap: '25px',
        alignItems: 'center',
      }}>
        {levels.map((level) => (
          <button
            key={level.id}
            onClick={() => setSelectedLevel(level.id)}
            style={{
              width: '300px',
              height: '80px',
              backgroundColor: selectedLevel === level.id ? '#8CC63F' : '#FFFFFF',
              border: 'none',
              borderRadius: '10px',
              cursor: 'pointer',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              padding: '15px',
              gap: '5px',
              transition: 'all 0.3s ease-out',
              boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
            }}
            onMouseEnter={(e) => {
              if (selectedLevel !== level.id) {
                e.currentTarget.style.backgroundColor = '#F5F5F5';
              }
            }}
            onMouseLeave={(e) => {
              if (selectedLevel !== level.id) {
                e.currentTarget.style.backgroundColor = '#FFFFFF';
              }
            }}
          >
            <span style={{
              fontFamily: 'Fredoka, Inter, sans-serif',
              fontWeight: 600,
              fontSize: '24px',
              color: selectedLevel === level.id ? '#FFFFFF' : '#000000',
            }}>
              {level.name}
            </span>
            <span style={{
              fontFamily: 'Fredoka, Inter, sans-serif',
              fontWeight: 400,
              fontSize: '14px',
              color: selectedLevel === level.id ? '#FFFFFF' : '#666666',
            }}>
              {level.description}
            </span>
          </button>
        ))}
      </div>

      {/* CONTINUE 버튼 */}
      <button
        onClick={handleContinue}
        disabled={!selectedLevel}
        style={{
          position: 'absolute',
          bottom: '75px',
          width: '300px',
          height: '75px',
          backgroundColor: selectedLevel ? '#8CC63F' : '#CCCCCC',
          border: 'none',
          borderRadius: '10px',
          cursor: selectedLevel ? 'pointer' : 'not-allowed',
          boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
          fontFamily: 'Fredoka, Inter, sans-serif',
          fontWeight: 600,
          fontSize: '30px',
          color: '#FFFFFF',
          lineHeight: '36.3px',
          transition: 'all 0.3s ease-out',
        }}
        onMouseEnter={(e) => {
          if (selectedLevel) {
            e.currentTarget.style.backgroundColor = '#7AB52F';
          }
        }}
        onMouseLeave={(e) => {
          if (selectedLevel) {
            e.currentTarget.style.backgroundColor = '#8CC63F';
          }
        }}
      >
        CONTINUE
      </button>
    </div>
  );
};

export default LevelSelectionPage;
