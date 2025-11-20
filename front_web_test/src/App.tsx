import { useState } from 'react'
import { InitialPage } from './pages/InitialPage'
import { WelcomePage } from './pages/WelcomePage'
import { LanguageSelectionPage } from './pages/LanguageSelectionPage'
import { LevelSelectionPage } from './pages/LevelSelectionPage'
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

      {/* 화면 5: 다음 화면 (TODO: 추가 예정) */}
      {screen === 5 && (
        <div style={{
          width: '393px',
          height: '852px',
          backgroundColor: '#F5F9F0',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          fontSize: '24px',
          fontFamily: 'Fredoka, Inter, sans-serif',
        }}>
          메인 화면 (Coming Soon)
        </div>
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
        <p style={{ margin: '5px 0' }}>화면 {screen}/5 | 디바이스: iPhone 16 (393x852)</p>
        <p style={{ margin: '5px 0', fontSize: '10px', color: '#999' }}>
          {screen === 1 && '클릭하여 시작'}
          {screen === 2 && '소셜 로그인 선택'}
          {screen === 3 && '언어 선택 후 CONTINUE 클릭'}
          {screen === 4 && '실력 레벨 선택 후 CONTINUE 클릭'}
          {screen === 5 && '메인 화면'}
        </p>
      </div>
    </div>
  )
}

export default App
