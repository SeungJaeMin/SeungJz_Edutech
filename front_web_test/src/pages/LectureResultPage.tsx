// Lecture Result Page
// Displays completion results after finishing a lecture

import React from 'react';

interface LectureResultPageProps {
  lectureTitle?: string;
  correctAnswers?: number;
  totalQuestions?: number;
  completedAt?: string;
  onBackToLectures?: () => void;
}

export const LectureResultPage: React.FC<LectureResultPageProps> = ({
  lectureTitle = 'English Conversation Practice',
  correctAnswers = 8,
  totalQuestions = 10,
  completedAt = new Date().toISOString(),
  onBackToLectures
}) => {
  const percentage = Math.round((correctAnswers / totalQuestions) * 100);
  const isPassed = percentage >= 70;

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
      padding: '40px 20px',
    }}>
      {/* 상단 로고 */}
      <div style={{
        width: '100px',
        height: '72px',
        marginBottom: '30px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
      }}>
        <img
          src="/img/Abocado_Logo.png"
          alt="Abocado Logo"
          style={{
            width: '100%',
            height: '100%',
            objectFit: 'contain',
          }}
        />
      </div>

      {/* 결과 타이틀 */}
      <h1 style={{
        fontFamily: 'Fredoka, Inter, sans-serif',
        fontWeight: 600,
        fontSize: '36px',
        color: isPassed ? '#8CC63F' : '#FF6B6B',
        marginBottom: '20px',
        textAlign: 'center',
      }}>
        {isPassed ? 'Great Job!' : 'Keep Trying!'}
      </h1>

      {/* Lecture 제목 */}
      <p style={{
        fontFamily: 'Fredoka, Inter, sans-serif',
        fontWeight: 400,
        fontSize: '18px',
        color: '#666',
        marginBottom: '40px',
        textAlign: 'center',
      }}>
        {lectureTitle}
      </p>

      {/* 점수 카드 */}
      <div style={{
        width: '300px',
        backgroundColor: '#FFFFFF',
        borderRadius: '20px',
        padding: '40px',
        boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.1)',
        marginBottom: '30px',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
      }}>
        {/* 퍼센티지 원형 */}
        <div style={{
          width: '150px',
          height: '150px',
          borderRadius: '50%',
          border: `10px solid ${isPassed ? '#8CC63F' : '#FF6B6B'}`,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          marginBottom: '30px',
        }}>
          <span style={{
            fontFamily: 'Fredoka, Inter, sans-serif',
            fontWeight: 700,
            fontSize: '48px',
            color: isPassed ? '#8CC63F' : '#FF6B6B',
          }}>
            {percentage}%
          </span>
        </div>

        {/* 정답/총문제 */}
        <p style={{
          fontFamily: 'Fredoka, Inter, sans-serif',
          fontWeight: 600,
          fontSize: '24px',
          color: '#000',
          marginBottom: '10px',
        }}>
          {correctAnswers} / {totalQuestions}
        </p>

        <p style={{
          fontFamily: 'Inter, sans-serif',
          fontWeight: 400,
          fontSize: '14px',
          color: '#999',
        }}>
          Correct Answers
        </p>
      </div>

      {/* 상세 통계 */}
      <div style={{
        width: '300px',
        backgroundColor: '#FFFFFF',
        borderRadius: '15px',
        padding: '20px',
        boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.08)',
        marginBottom: '30px',
      }}>
        <div style={{
          display: 'flex',
          justifyContent: 'space-between',
          marginBottom: '15px',
          paddingBottom: '15px',
          borderBottom: '1px solid #F0F0F0',
        }}>
          <span style={{
            fontFamily: 'Inter, sans-serif',
            fontSize: '14px',
            color: '#666',
          }}>
            Completed
          </span>
          <span style={{
            fontFamily: 'Inter, sans-serif',
            fontSize: '14px',
            fontWeight: 600,
            color: '#000',
          }}>
            {new Date(completedAt).toLocaleDateString()}
          </span>
        </div>

        <div style={{
          display: 'flex',
          justifyContent: 'space-between',
        }}>
          <span style={{
            fontFamily: 'Inter, sans-serif',
            fontSize: '14px',
            color: '#666',
          }}>
            Status
          </span>
          <span style={{
            fontFamily: 'Inter, sans-serif',
            fontSize: '14px',
            fontWeight: 600,
            color: isPassed ? '#8CC63F' : '#FF6B6B',
          }}>
            {isPassed ? 'PASSED' : 'FAILED'}
          </span>
        </div>
      </div>

      {/* 버튼 */}
      <button
        onClick={onBackToLectures}
        style={{
          position: 'absolute',
          bottom: '40px',
          width: '300px',
          height: '60px',
          backgroundColor: '#8CC63F',
          border: 'none',
          borderRadius: '10px',
          cursor: 'pointer',
          boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
          fontFamily: 'Fredoka, Inter, sans-serif',
          fontWeight: 600,
          fontSize: '20px',
          color: '#FFFFFF',
          transition: 'all 0.3s ease-out',
        }}
        onMouseEnter={(e) => {
          e.currentTarget.style.backgroundColor = '#7AB52F';
        }}
        onMouseLeave={(e) => {
          e.currentTarget.style.backgroundColor = '#8CC63F';
        }}
      >
        Back to Lectures
      </button>
    </div>
  );
};

export default LectureResultPage;
