import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import './LectureListPage.css';

const LectureListPage = () => {
  const navigate = useNavigate();
  const [lectures, setLectures] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedType, setSelectedType] = useState('KPOP');

  useEffect(() => {
    fetchLectures();
  }, [selectedType]);

  const fetchLectures = async () => {
    try {
      setLoading(true);
      const response = await api.get(`/api/lectures/type/${selectedType}`);
      setLectures(response.data);
    } catch (error) {
      console.error('Failed to fetch lectures:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLectureClick = (lectureId) => {
    navigate(`/lecture/${lectureId}`);
  };

  const getTypeLabel = (type) => {
    switch (type) {
      case 'KPOP':
        return 'M';
      case 'DRAMA':
        return 'D';
      case 'INTERVIEW':
        return 'T';
      default:
        return type;
    }
  };

  return (
    <div className="lecture-list-container">
      <div className="lecture-list-content">
        <div className="lecture-video-placeholder">
          {lectures.length > 0 && lectures[0].thumbnailUrl && (
            <img src={lectures[0].thumbnailUrl} alt="Featured" />
          )}
        </div>

        <div className="lecture-items">
          {loading ? (
            <div className="loading">로딩 중...</div>
          ) : lectures.length === 0 ? (
            <div className="empty">학습 콘텐츠가 없습니다.</div>
          ) : (
            lectures.map((lecture) => (
              <div
                key={lecture.id}
                className="lecture-item"
                onClick={() => handleLectureClick(lecture.id)}
              >
                <div className="lecture-thumbnail">
                  {lecture.thumbnailUrl ? (
                    <img src={lecture.thumbnailUrl} alt={lecture.title} />
                  ) : (
                    <div className="thumbnail-placeholder" />
                  )}
                </div>
                <div className="lecture-info">
                  <h3>{lecture.title}</h3>
                  <p>{lecture.description}</p>
                </div>
                <div className="lecture-arrow">›</div>
              </div>
            ))
          )}
        </div>
      </div>

      <div className="bottom-navigation">
        <button className="nav-btn" onClick={() => navigate(-1)}>
          ‹
        </button>
        <div className="nav-tabs">
          <button
            className={`tab-btn ${selectedType === 'KPOP' ? 'active' : ''}`}
            onClick={() => setSelectedType('KPOP')}
          >
            {getTypeLabel('KPOP')}
          </button>
          <button
            className={`tab-btn ${selectedType === 'DRAMA' ? 'active' : ''}`}
            onClick={() => setSelectedType('DRAMA')}
          >
            {getTypeLabel('DRAMA')}
          </button>
          <button
            className={`tab-btn ${selectedType === 'INTERVIEW' ? 'active' : ''}`}
            onClick={() => setSelectedType('INTERVIEW')}
          >
            {getTypeLabel('INTERVIEW')}
          </button>
        </div>
        <button className="nav-btn">
          <span className="search-icon">🔍</span>
        </button>
        <button className="nav-btn">...</button>
      </div>
    </div>
  );
};

export default LectureListPage;
