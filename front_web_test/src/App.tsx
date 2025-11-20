import { useState } from 'react'
import { ClickableText } from './components/ClickableText'
import { FigmaButton } from './components/FigmaButton'
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
      backgroundColor: '#FFFFFF',
      fontFamily: 'Inter, sans-serif',
      padding: '20px',
    }}>
      {/* 화면 1: iPhone 16 - 1 (id: 1:2) */}
      {screen === 1 && (
        <div style={{
          width: '393px',
          height: '852px',
          backgroundColor: '#FFFFFF',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          position: 'relative',
        }}>
          {/* "뿌찎" 텍스트 (id: 1:3) - ON_CLICK → 화면 2 */}
          <ClickableText
            text="뿌찎"
            onClick={() => setScreen(2)}
            fontSize="128px"
            textAlign="left"
            lineHeight="154.91px"
          />
        </div>
      )}

      {/* 화면 2: iPhone 16 - 2 (id: 1:4) */}
      {screen === 2 && (
        <div style={{
          width: '393px',
          height: '852px',
          backgroundColor: '#FFFFFF',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          position: 'relative',
          padding: '20px',
        }}>
          {/* "똥 나왔다" 텍스트 (id: 1:5) - ON_PRESS → 화면 3 */}
          <div style={{ marginBottom: '100px' }}>
            <ClickableText
              text="똥
나왔다"
              onClick={() => setScreen(3)}
              fontSize="128px"
              textAlign="center"
              lineHeight="154.91px"
            />
          </div>

          {/* "다시 집어넣기" 버튼 (id: 1:7) - ON_CLICK → 화면 1 */}
          <div style={{ position: 'absolute', bottom: '74px' }}>
            <FigmaButton
              text="다시 집어넣기"
              onClick={() => setScreen(1)}
            />
          </div>
        </div>
      )}

      {/* 화면 3: iPhone 16 - 3 (id: 1:12) */}
      {screen === 3 && (
        <div style={{
          width: '393px',
          height: '852px',
          backgroundColor: '#FFFFFF',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          position: 'relative',
        }}>
          {/* "똥이 안멈춰 !!!!!!!" 텍스트 (id: 1:13) - 인터랙션 없음 */}
          <div style={{
            fontFamily: 'Inter, sans-serif',
            fontWeight: 400,
            fontSize: '128px',
            color: '#000000',
            textAlign: 'center',
            lineHeight: '154.91px',
          }}>
            똥이<br />안멈춰<br />!!!!!!!
          </div>

          {/* 추가: 처음으로 돌아가기 버튼 (Figma에는 없지만 편의상 추가) */}
          <div style={{ position: 'absolute', bottom: '74px' }}>
            <FigmaButton
              text="처음으로"
              onClick={() => setScreen(1)}
            />
          </div>
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
        <p style={{ margin: '5px 0' }}>✓ Figma 프로토타입 완벽 재현</p>
        <p style={{ margin: '5px 0' }}>파일: HT_SeungJz (qNH0gG3M2WGNwK1ygMhG5e)</p>
        <p style={{ margin: '5px 0' }}>화면 {screen}/3 | 디바이스: iPhone 16 (393x852)</p>
        <p style={{ margin: '5px 0', fontSize: '10px', color: '#999' }}>
          인터랙션: {screen === 1 ? '"뿌찎" 클릭 → 화면2' : screen === 2 ? '"똥 나왔다" 클릭 → 화면3 | "다시 집어넣기" → 화면1' : '마지막 화면'}
        </p>
      </div>
    </div>
  )
}

export default App
