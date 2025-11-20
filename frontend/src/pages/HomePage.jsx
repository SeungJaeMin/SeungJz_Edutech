import { useAuth } from '../hooks/useAuth';
import './HomePage.css';

const HomePage = () => {
  const { user, logout } = useAuth();

  return (
    <div className="home-container">
      <header className="home-header">
        <h1>SeungJz Edutech</h1>
        <div className="user-info">
          {user?.profileImageUrl && (
            <img
              src={user.profileImageUrl}
              alt={user.nickname}
              className="profile-image"
            />
          )}
          <span>{user?.nickname || user?.email}</span>
          <button onClick={logout} className="logout-btn">
            로그아웃
          </button>
        </div>
      </header>

      <main className="home-main">
        <div className="welcome-section">
          <h2>환영합니다! 👋</h2>
          <p>한국어 학습을 시작해보세요.</p>
        </div>

        <div className="level-cards">
          <div className="level-card">
            <div className="level-icon">🎵</div>
            <h3>1단계: K-Pop</h3>
            <p>BTS, 블랙핑크와 함께 배우는 기초 한국어</p>
            <button className="start-btn" disabled>
              준비 중
            </button>
          </div>

          <div className="level-card">
            <div className="level-icon">🎬</div>
            <h3>2단계: 드라마</h3>
            <p>드라마 속 상황별 실전 회화</p>
            <button className="start-btn" disabled>
              준비 중
            </button>
          </div>

          <div className="level-card">
            <div className="level-icon">💼</div>
            <h3>3단계: 면접</h3>
            <p>AI와 함께하는 실시간 면접 연습</p>
            <button className="start-btn" disabled>
              준비 중
            </button>
          </div>
        </div>

        <div className="user-details">
          <h3>내 정보</h3>
          <div className="detail-item">
            <span className="label">이메일:</span>
            <span>{user?.email}</span>
          </div>
          <div className="detail-item">
            <span className="label">로그인 방식:</span>
            <span>{user?.provider}</span>
          </div>
          <div className="detail-item">
            <span className="label">가입일:</span>
            <span>{user?.createdAt ? new Date(user.createdAt).toLocaleDateString('ko-KR') : '-'}</span>
          </div>
        </div>
      </main>
    </div>
  );
};

export default HomePage;
