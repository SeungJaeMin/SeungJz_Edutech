import { useNavigate, useParams } from 'react-router-dom';
import styled from 'styled-components';

const LearningPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  return (
    <Container>
      {/* Landscape video placeholder - rotated view */}
      <VideoContainer>
        <VideoPlaceholder />
        <TopBar>
          <ProfileCircle />
          <BackArrow onClick={() => navigate('/lectures')}>←</BackArrow>
          <SearchBar />
        </TopBar>

        <ContentText>학습</ContentText>

        {/* Navigation controls */}
        <NavigationCircle style={{ left: '166px', top: '260px' }}>
          <Arrow>↑</Arrow>
        </NavigationCircle>

        <CenterCircle />

        <NavigationCircle style={{ left: '226px', top: '455px' }}>
          <Arrow>↓</Arrow>
        </NavigationCircle>

        <NavigationCircle style={{ left: '226px', top: '592px' }}>
          <Arrow>→</Arrow>
        </NavigationCircle>
      </VideoContainer>
    </Container>
  );
};

const Container = styled.div`
  width: 100%;
  max-width: 852px;
  height: 100vh;
  max-height: 393px;
  margin: 0 auto;
  background: black;
  position: relative;
  overflow: hidden;
`;

const VideoContainer = styled.div`
  width: 100%;
  height: 100%;
  position: relative;
`;

const VideoPlaceholder = styled.div`
  position: absolute;
  inset: 0;
  background: #1a1a1a;
`;

const TopBar = styled.div`
  position: absolute;
  top: 27px;
  left: 55px;
  right: 0;
  display: flex;
  align-items: center;
  gap: 12px;
  z-index: 2;
`;

const ProfileCircle = styled.div`
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: #d9d9d9;
`;

const BackArrow = styled.div`
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    transform: scale(1.2);
  }
`;

const SearchBar = styled.div`
  width: 797px;
  height: 26px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 13px;
`;

const ContentText = styled.div`
  position: absolute;
  left: 218px;
  top: 393px;
  font-family: 'Inter', 'Noto Sans KR', sans-serif;
  font-size: 36px;
  font-weight: 400;
  color: white;
  z-index: 1;
`;

const CenterCircle = styled.div`
  position: absolute;
  width: 120px;
  height: 120px;
  left: 256px;
  top: 366px;
  border-radius: 50%;
  background: rgba(140, 198, 63, 0.8);
  z-index: 2;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background: rgba(140, 198, 63, 1);
    transform: scale(1.05);
  }
`;

const NavigationCircle = styled.div`
  position: absolute;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: rgba(200, 200, 200, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background: rgba(200, 200, 200, 0.5);
    transform: scale(1.1);
  }
`;

const Arrow = styled.div`
  font-size: 24px;
  color: white;
`;

export default LearningPage;
