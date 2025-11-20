import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import styled from 'styled-components';

const InterviewPage = () => {
  const navigate = useNavigate();
  const [isRecording, setIsRecording] = useState(false);

  const handleToggleRecording = () => {
    setIsRecording(!isRecording);
  };

  const handleEndInterview = () => {
    navigate('/review');
  };

  return (
    <Container>
      <GradientBackground />

      <AvatarCircle>
        <AvatarText>D</AvatarText>
      </AvatarCircle>

      <DavidTitle>David</DavidTitle>

      <BottomNav>
        <BackButton onClick={() => navigate('/home')}>
          <BackArrow>←</BackArrow>
        </BackButton>

        <RecordButton onClick={handleToggleRecording} recording={isRecording}>
          {isRecording ? '■' : '▶'}
        </RecordButton>

        <SettingsButton onClick={handleEndInterview}>
          <PlusIcon>+</PlusIcon>
        </SettingsButton>
      </BottomNav>
    </Container>
  );
};

const Container = styled.div`
  width: 100%;
  max-width: 393px;
  min-height: 100vh;
  background: white;
  position: relative;
  overflow: hidden;
  margin: 0 auto;

  @media (max-width: 393px) {
    max-width: 100vw;
  }
`;

const GradientBackground = styled.div`
  position: absolute;
  width: 154%;
  height: 70.9%;
  left: -27%;
  top: 51.3%;
  background: radial-gradient(circle, rgba(140, 198, 63, 0.2) 0%, rgba(140, 198, 63, 0) 70%);
  pointer-events: none;
`;

const AvatarCircle = styled.div`
  position: absolute;
  width: clamp(170px, 51.7vw, 203px);
  height: clamp(170px, 51.7vw, 203px);
  left: 50%;
  transform: translateX(-50%);
  top: 23.5%;
  border-radius: 50%;
  background: #d9d9d9;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
`;

const AvatarText = styled.p`
  font-family: 'Inter', sans-serif;
  font-weight: 700;
  font-size: clamp(54px, 16vw, 64px);
  color: black;
  margin: 0;
`;

const DavidTitle = styled.p`
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  top: 6.7%;
  font-family: 'Inter', sans-serif;
  font-weight: 700;
  font-size: clamp(54px, 16vw, 64px);
  color: black;
  margin: 0;
  z-index: 2;
`;

const BottomNav = styled.nav`
  position: absolute;
  bottom: 1.9%;
  left: 50%;
  transform: translateX(-50%);
  width: 92.4%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 2;
`;

const BackButton = styled.button`
  width: clamp(85px, 25.4vw, 100px);
  height: clamp(85px, 25.4vw, 100px);
  border-radius: 50%;
  background: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;

  &:hover {
    background: #f5f5f5;
    transform: scale(1.05);
  }

  &:active {
    transform: scale(0.98);
  }
`;

const BackArrow = styled.div`
  font-size: clamp(30px, 9vw, 36px);
  color: black;
`;

const RecordButton = styled.button<{ recording: boolean }>`
  width: clamp(85px, 25.4vw, 100px);
  height: clamp(85px, 25.4vw, 100px);
  border-radius: 50%;
  background: ${({ recording }) => (recording ? '#ff4444' : '#8cc63f')};
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: clamp(34px, 10vw, 40px);
  color: white;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.2);
  transition: all 0.2s;
  animation: ${({ recording }) => (recording ? 'pulse 1.5s infinite' : 'none')};

  &:hover {
    transform: scale(1.1);
  }

  &:active {
    transform: scale(1.05);
  }

  @keyframes pulse {
    0%,
    100% {
      transform: scale(1);
    }
    50% {
      transform: scale(1.1);
    }
  }
`;

const SettingsButton = styled.button`
  width: clamp(85px, 25.4vw, 100px);
  height: clamp(85px, 25.4vw, 100px);
  border-radius: 50%;
  background: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;

  &:hover {
    background: #f5f5f5;
    transform: scale(1.05);
  }

  &:active {
    transform: scale(0.98);
  }
`;

const PlusIcon = styled.div`
  font-size: clamp(38px, 11vw, 44px);
  color: black;
  font-weight: 300;
`;

export default InterviewPage;
