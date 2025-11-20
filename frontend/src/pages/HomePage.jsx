import { useAuth } from '../hooks/useAuth.jsx';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';

const HomePage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const handleTypeClick = (type) => {
    navigate(`/lectures?type=${type}`);
  };

  const getUserInitial = () => {
    if (user?.nickname) {
      return user.nickname.charAt(0).toUpperCase();
    }
    if (user?.email) {
      return user.email.charAt(0).toUpperCase();
    }
    return 'U';
  };

  return (
    <div className="home-container-new">
      <div className="profile-badge">
        {user?.profileImageUrl ? (
          <img src={user.profileImageUrl} alt="Profile" />
        ) : (
          <div className="profile-initial">{getUserInitial()}</div>
        )}
      </div>

      <div className="welcome-message">
        <h1>{user?.nickname || user?.email?.split('@')[0] || '사용자'}님, 안녕하세요!</h1>
      </div>

      <div className="learning-types">
        <button
          className="type-card"
          onClick={() => handleTypeClick('KPOP')}
        >
          <span className="type-label">M</span>
        </button>
        <button
          className="type-card"
          onClick={() => handleTypeClick('DRAMA')}
        >
          <span className="type-label">D</span>
        </button>
        <button
          className="type-card"
          onClick={() => handleTypeClick('INTERVIEW')}
        >
          <span className="type-label">T</span>
        </button>
      </div>
    </div>
  );
};

export default HomePage;
