import { useState } from 'react'
import { InitialPage } from './pages/InitialPage'
import { WelcomePage } from './pages/WelcomePage'
import { LanguageSelectionPage } from './pages/LanguageSelectionPage'
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
        <LanguageSelectionPage />
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
        <p style={{ margin: '5px 0' }}>화면 {screen}/3 | 디바이스: iPhone 16 (393x852)</p>
        <p style={{ margin: '5px 0', fontSize: '10px', color: '#999' }}>
          {screen === 1 && '클릭하여 시작'}
          {screen === 2 && '소셜 로그인 선택'}
          {screen === 3 && '학습할 언어 선택'}
        </p>
      </div>
    </div>
  )
}

export default App
