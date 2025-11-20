// Figma 디자인: Lecture Selection (스와이프 카드)
// 파일: HT_SeungJz (qNH0gG3M2WGNwK1ygMhG5e)
// 노드: 48:1174, 48:1186, 48:1202

import React, { useState, useRef, TouchEvent } from 'react';

interface Lecture {
  id: string;
  title: string;
  artist: string;
  category: string;
  thumbnail?: string;
  duration?: string;
}

interface LectureSelectionPageProps {
  category?: 'music' | 'movie' | 'talk';
  lectures?: Lecture[];
  onSelectLecture?: (lectureId: string) => void;
  onBack?: () => void;
}

const defaultLectures: Lecture[] = [
  {
    id: '1',
    title: 'English Conversation',
    artist: 'John Doe',
    category: 'Music',
    duration: '15:30'
  },
  {
    id: '2',
    title: 'Business English',
    artist: 'Jane Smith',
    category: 'Talk',
    duration: '20:45'
  },
  {
    id: '3',
    title: 'Daily Life Phrases',
    artist: 'Mike Johnson',
    category: 'Drama / Movie',
    duration: '18:00'
  },
  {
    id: '4',
    title: 'Travel English',
    artist: 'Sarah Lee',
    category: 'Music',
    duration: '12:15'
  },
];

export const LectureSelectionPage: React.FC<LectureSelectionPageProps> = ({
  category = 'music',
  lectures = defaultLectures,
  onSelectLecture,
  onBack
}) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [touchStart, setTouchStart] = useState(0);
  const [touchEnd, setTouchEnd] = useState(0);
  const cardRef = useRef<HTMLDivElement>(null);

  const handleTouchStart = (e: TouchEvent) => {
    setTouchStart(e.targetTouches[0].clientX);
  };

  const handleTouchMove = (e: TouchEvent) => {
    setTouchEnd(e.targetTouches[0].clientX);
  };

  const handleTouchEnd = () => {
    if (touchStart - touchEnd > 75) {
      // Swipe left - next card
      if (currentIndex < lectures.length - 1) {
        setCurrentIndex(currentIndex + 1);
      }
    }

    if (touchStart - touchEnd < -75) {
      // Swipe right - previous card
      if (currentIndex > 0) {
        setCurrentIndex(currentIndex - 1);
      }
    }
  };

  const handleNext = () => {
    if (currentIndex < lectures.length - 1) {
      setCurrentIndex(currentIndex + 1);
    }
  };

  const handlePrev = () => {
    if (currentIndex > 0) {
      setCurrentIndex(currentIndex - 1);
    }
  };

  const handleSelectLecture = () => {
    if (onSelectLecture) {
      onSelectLecture(lectures[currentIndex].id);
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
      {/* 헤더 */}
      <div style={{
        width: '100%',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: '20px',
        marginTop: '30px',
      }}>
        {/* 뒤로가기 버튼 */}
        <button
          onClick={onBack}
          style={{
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
          }}
        >
          <svg width="10" height="18" viewBox="0 0 10 18" fill="none">
            <path d="M9 1L1 9L9 17" stroke="black" strokeWidth="5" strokeLinecap="round"/>
          </svg>
        </button>

        {/* 카테고리 표시 */}
        <div style={{
          backgroundColor: '#FFFFFF',
          padding: '10px 30px',
          borderRadius: '32px',
          boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
          fontFamily: 'Fredoka, Inter, sans-serif',
          fontWeight: 600,
          fontSize: '18px',
          color: '#000',
        }}>
          {category.charAt(0).toUpperCase() + category.slice(1)}
        </div>

        <div style={{ width: '50px' }} /> {/* Spacer for alignment */}
      </div>

      {/* 스와이프 카드 영역 */}
      <div
        ref={cardRef}
        onTouchStart={handleTouchStart}
        onTouchMove={handleTouchMove}
        onTouchEnd={handleTouchEnd}
        style={{
          flex: 1,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          position: 'relative',
          width: '100%',
          marginTop: '50px',
        }}
      >
        {/* 배경 카드들 (3D 효과) */}
        {currentIndex < lectures.length - 2 && (
          <div style={{
            position: 'absolute',
            width: '277px',
            height: '551px',
            backgroundColor: '#9D9D9D',
            borderRadius: '53px',
            transform: 'translateX(20px) translateY(30px) scale(0.85)',
            opacity: 0.3,
            zIndex: 1,
          }} />
        )}

        {currentIndex < lectures.length - 1 && (
          <div style={{
            position: 'absolute',
            width: '305px',
            height: '582px',
            backgroundColor: '#B1B1B1',
            borderRadius: '53px',
            transform: 'translateY(10px) scale(0.92)',
            opacity: 0.5,
            zIndex: 2,
          }} />
        )}

        {/* 메인 카드 */}
        <div
          onClick={handleSelectLecture}
          style={{
            position: 'relative',
            width: '333px',
            height: '613px',
            backgroundColor: '#D9D9D9',
            borderRadius: '53px',
            cursor: 'pointer',
            zIndex: 3,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'space-between',
            padding: '40px 30px',
            boxShadow: '0px 8px 20px rgba(0, 0, 0, 0.15)',
            transition: 'transform 0.3s ease-out',
          }}
        >
          {/* 썸네일 영역 */}
          <div style={{
            width: '250px',
            height: '250px',
            backgroundColor: '#FFFFFF',
            borderRadius: '20px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontSize: '48px',
            marginBottom: '30px',
          }}>
            🎵
          </div>

          {/* 강의 정보 */}
          <div style={{
            width: '100%',
            textAlign: 'center',
          }}>
            <h2 style={{
              fontFamily: 'Fredoka, Inter, sans-serif',
              fontWeight: 600,
              fontSize: '28px',
              color: '#000',
              marginBottom: '10px',
            }}>
              {lectures[currentIndex].title}
            </h2>

            <p style={{
              fontFamily: 'Inter, sans-serif',
              fontWeight: 400,
              fontSize: '16px',
              color: '#666',
              marginBottom: '20px',
            }}>
              {lectures[currentIndex].artist}
            </p>

            {lectures[currentIndex].duration && (
              <p style={{
                fontFamily: 'Inter, sans-serif',
                fontWeight: 400,
                fontSize: '14px',
                color: '#999',
              }}>
                Duration: {lectures[currentIndex].duration}
              </p>
            )}
          </div>
        </div>
      </div>

      {/* 인디케이터 */}
      <div style={{
        display: 'flex',
        gap: '10px',
        marginBottom: '30px',
      }}>
        {lectures.map((_, index) => (
          <div
            key={index}
            style={{
              width: currentIndex === index ? '30px' : '10px',
              height: '10px',
              borderRadius: '5px',
              backgroundColor: currentIndex === index ? '#8CC63F' : '#CCCCCC',
              transition: 'all 0.3s ease-out',
              cursor: 'pointer',
            }}
            onClick={() => setCurrentIndex(index)}
          />
        ))}
      </div>

      {/* 네비게이션 버튼 */}
      <div style={{
        display: 'flex',
        gap: '20px',
        marginBottom: '40px',
      }}>
        <button
          onClick={handlePrev}
          disabled={currentIndex === 0}
          style={{
            width: '60px',
            height: '60px',
            borderRadius: '50%',
            backgroundColor: currentIndex === 0 ? '#CCCCCC' : '#FFFFFF',
            border: 'none',
            cursor: currentIndex === 0 ? 'not-allowed' : 'pointer',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.15)',
          }}
        >
          <svg width="12" height="20" viewBox="0 0 12 20" fill="none">
            <path d="M10 2L2 10L10 18" stroke="black" strokeWidth="4" strokeLinecap="round"/>
          </svg>
        </button>

        <button
          onClick={handleNext}
          disabled={currentIndex === lectures.length - 1}
          style={{
            width: '60px',
            height: '60px',
            borderRadius: '50%',
            backgroundColor: currentIndex === lectures.length - 1 ? '#CCCCCC' : '#8CC63F',
            border: 'none',
            cursor: currentIndex === lectures.length - 1 ? 'not-allowed' : 'pointer',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.15)',
          }}
        >
          <svg width="12" height="20" viewBox="0 0 12 20" fill="none">
            <path d="M2 2L10 10L2 18" stroke="white" strokeWidth="4" strokeLinecap="round"/>
          </svg>
        </button>
      </div>
    </div>
  );
};

export default LectureSelectionPage;
