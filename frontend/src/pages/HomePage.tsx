import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const HomePage = () => {
  const navigate = useNavigate();

  return (
    <Container>
      <Greeting>김승재님, 안녕하세요!</Greeting>
      <ProfileButton>P</ProfileButton>

      <ModeButtons>
        <ModeButton onClick={() => navigate('/lectures?level=1')}>
          <ModeLabel>M</ModeLabel>
        </ModeButton>

        <ModeButton onClick={() => navigate('/lectures?level=2')}>
          <ModeLabel>D</ModeLabel>
        </ModeButton>

        <ModeButton onClick={() => navigate('/interview')}>
          <ModeLabel>T</ModeLabel>
        </ModeButton>
      </ModeButtons>
    </Container>
  );
};

const Container = styled.div`
  width: 100%;
  max-width: 393px;
  min-height: 100vh;
  background: white;
  position: relative;
  display: flex;
  flex-direction: column;
  margin: 0 auto;

  @media (max-width: 393px) {
    max-width: 100vw;
  }
`;

const Greeting = styled.h1`
  font-family: 'Inter', 'Noto Sans KR', sans-serif;
  font-size: clamp(28px, 9vw, 36px);
  font-weight: 400;
  color: black;
  margin: 0;
  position: absolute;
  top: 39.2%;
  left: 8.1%;

  @media (max-width: 393px) {
    left: 5%;
    top: 38%;
  }
`;

const ProfileButton = styled.button`
  width: clamp(54px, 16vw, 64px);
  height: clamp(54px, 16vw, 64px);
  border-radius: 50%;
  background: #d9d9d9;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Inter', sans-serif;
  font-size: clamp(28px, 9vw, 36px);
  font-weight: 400;
  color: black;
  position: absolute;
  top: 2.9%;
  right: 6.6%;
  transition: all 0.2s;

  &:hover {
    background: #c0c0c0;
  }

  &:active {
    transform: scale(0.95);
  }

  @media (max-width: 393px) {
    right: 4%;
  }
`;

const ModeButtons = styled.div`
  position: absolute;
  top: 85.2%;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: clamp(12px, 3.8vw, 15px);
  align-items: center;

  @media (max-width: 393px) {
    top: 84%;
  }
`;

const ModeButton = styled.button`
  width: clamp(85px, 25.7vw, 101px);
  height: clamp(79px, 23.5vw, 94px);
  background: #d9d9d9;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;

  &:hover {
    background: #c0c0c0;
    transform: translateY(-2px);
  }

  &:active {
    transform: translateY(0);
  }
`;

const ModeLabel = styled.div`
  font-family: 'Inter', sans-serif;
  font-size: clamp(30px, 9vw, 36px);
  font-weight: 400;
  color: black;
`;

export default HomePage;
