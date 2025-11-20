import { useState } from 'react'
import { InitialPage } from './pages/InitialPage'
import { WelcomePage } from './pages/WelcomePage'
import { LanguageSelectionPage } from './pages/LanguageSelectionPage'
import { LevelSelectionPage } from './pages/LevelSelectionPage'
import { MainPage } from './pages/MainPage'
import { LectureSelectionPage } from './pages/LectureSelectionPage'
import { LectureResultPage } from './pages/LectureResultPage'
import './App.css'

function App() {
  const [screen, setScreen] = useState(1)

  return (
    <div style={{
      minHeight: '100vh',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor: '#F5F9F0',
      fontFamily: 'Inter, sans-serif',
      padding: '20px',
    }}>
      {/* 화면 1: 초기 화면 (iPhone 16 - 24) */}
      {screen === 1 && (
        <InitialPage onNavigate={() => setScreen(2)} />
      )}

      {/* 화면 2: Welcome 화면 (iPhone 16 - 25) */}
      {screen === 2 && (
        <WelcomePage onNavigate={() => setScreen(3)} />
      )}

      {/* 화면 3: 언어 선택 화면 (iPhone 16 - 26) */}
      {screen === 3 && (
        <LanguageSelectionPage onNavigate={() => setScreen(4)} />
      )}

      {/* 화면 4: 레벨 선택 화면 */}
      {screen === 4 && (
        <LevelSelectionPage onNavigate={() => setScreen(5)} />
      )}

      {/* 화면 5: Main 화면 (Movie/Music/Talk 선택) */}
      {screen === 5 && (
        <MainPage onSelectCategory={(category) => {
          console.log('Selected category:', category);
          setScreen(6);
        }} />
      )}

      {/* 화면 6: 카테고리별 Lecture 선택 화면 (스와이프 카드) */}
      {screen === 6 && (
        <LectureSelectionPage
          onSelectLecture={(lectureId) => {
            console.log('Selected lecture:', lectureId);
            setScreen(7);
          }}
          onBack={() => setScreen(5)}
        />
      )}

      {/* 화면 7: Lecture Playing 화면 (TODO: 추가 예정) */}
      {screen === 7 && (
        <div style={{
          width: '393px',
          height: '852px',
          backgroundColor: '#000000',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          fontSize: '24px',
          fontFamily: 'Fredoka, Inter, sans-serif',
          color: '#FFFFFF',
        }}>
          <p style={{ marginBottom: '20px' }}>Video Playing...</p>
          <button
            onClick={() => setScreen(8)}
            style={{
              backgroundColor: '#8CC63F',
              border: 'none',
              borderRadius: '10px',
              padding: '15px 40px',
              fontSize: '18px',
              color: '#FFFFFF',
              cursor: 'pointer',
              fontFamily: 'Fredoka, Inter, sans-serif',
              fontWeight: 600,
            }}
          >
            Complete Lecture
          </button>
        </div>
      )}

      {/* 화면 8: Lecture Result 화면 */}
      {screen === 8 && (
        <LectureResultPage
          onBackToLectures={() => setScreen(6)}
        />
      )}

      {/* Figma 정보 표시 */}
      <div style={{
        position: 'fixed',
        bottom: '20px',
        fontSize: '12px',
        color: '#666',
        textAlign: 'center',
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        padding: '10px 20px',
        borderRadius: '8px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
      }}>
        <p style={{ margin: '5px 0' }}>✓ Figma 디자인 완벽 재현 (node-id: 30-1419)</p>
        <p style={{ margin: '5px 0' }}>파일: HT_SeungJz (qNH0gG3M2WGNwK1ygMhG5e)</p>
        <p style={{ margin: '5px 0' }}>화면 {screen}/8 | 디바이스: iPhone 16 (393x852)</p>
        <p style={{ margin: '5px 0', fontSize: '10px', color: '#999' }}>
          {screen === 1 && '클릭하여 시작'}
          {screen === 2 && '소셜 로그인 선택'}
          {screen === 3 && '언어 선택 후 CONTINUE 클릭'}
          {screen === 4 && '실력 레벨 선택 후 CONTINUE 클릭'}
          {screen === 5 && 'Movie/Music/Talk 카테고리 선택'}
          {screen === 6 && 'Lecture 카드 스와이프 또는 클릭'}
          {screen === 7 && 'Lecture 재생 중'}
          {screen === 8 && 'Lecture 완료 결과'}
        </p>
      </div>
    </div>
  )
}

export default App
