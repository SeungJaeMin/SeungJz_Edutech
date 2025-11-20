import { authApi } from '../services/api';
import './LoginPage.css';

const LoginPage = () => {
  const handleGoogleLogin = () => {
    window.location.href = authApi.getGoogleLoginUrl();
  };

  const handleKakaoLogin = () => {
    window.location.href = authApi.getKakaoLoginUrl();
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h1>SeungJz Edutech</h1>
        <p className="subtitle">한국어 학습 플랫폼</p>

        <div className="login-buttons">
          <button
            className="login-btn google-btn"
            onClick={handleGoogleLogin}
          >
            <span className="btn-icon">G</span>
            Google로 시작하기
          </button>

          <button
            className="login-btn kakao-btn"
            onClick={handleKakaoLogin}
          >
            <span className="btn-icon">K</span>
            Kakao로 시작하기
          </button>
        </div>

        <div className="features">
          <div className="feature-item">
            <span className="feature-icon">🎵</span>
            <span>K-Pop으로 배우는 한국어</span>
          </div>
          <div className="feature-item">
            <span className="feature-icon">🎬</span>
            <span>드라마 속 실전 회화</span>
          </div>
          <div className="feature-item">
            <span className="feature-icon">💼</span>
            <span>AI 면접 시뮬레이션</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
