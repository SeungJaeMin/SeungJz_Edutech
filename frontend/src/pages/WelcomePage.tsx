import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const WelcomePage = () => {
  const navigate = useNavigate();

  const handleLogin = (provider: string) => {
    console.log(`Login with ${provider}`);
    navigate('/home');
  };

  return (
    <Container>
      <LogoImage src="https://www.figma.com/api/mcp/asset/e2a72022-0660-466c-81e4-4a7fa4530943" alt="ABOCADO Logo" />

      <WelcomeText>welcome!</WelcomeText>

      <LoginButtonsContainer>
        <GoogleButton onClick={() => handleLogin('Google')}>
          <GoogleIcon src="https://www.figma.com/api/mcp/asset/156c9107-da9a-4b06-bba6-dc4873c7fdfa" alt="Google" />
        </GoogleButton>

        <AppleButton onClick={() => handleLogin('Apple')}>
          <AppleIcon src="https://www.figma.com/api/mcp/asset/030e00ed-4954-4079-b80d-53835cbaa0d2" alt="Apple" />
        </AppleButton>

        <KakaoButton onClick={() => handleLogin('Kakao')} />
      </LoginButtonsContainer>
    </Container>
  );
};

const Container = styled.div`
  width: 100%;
  max-width: 393px;
  min-height: 100vh;
  background: #f5f9f0;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 auto;

  @media (max-width: 393px) {
    max-width: 100vw;
  }
`;

const LogoImage = styled.img`
  width: min(240px, 61%);
  height: auto;
  position: absolute;
  top: 58px;
  left: 50%;
  transform: translateX(-50%);
  object-fit: contain;

  @media (max-width: 393px) {
    top: 8vh;
  }
`;

const WelcomeText = styled.p`
  position: absolute;
  top: 45.5%;
  left: 50%;
  transform: translateX(-50%);
  font-family: 'Fredoka', sans-serif;
  font-weight: 600;
  font-size: clamp(48px, 16vw, 64px);
  color: #8cc63f;
  line-height: normal;
  margin: 0;
  white-space: nowrap;

  @media (max-width: 393px) {
    top: 45%;
  }
`;

const LoginButtonsContainer = styled.div`
  position: absolute;
  top: 73.4%;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: clamp(25px, 9.5vw, 38px);
  align-items: center;

  @media (max-width: 393px) {
    top: 73%;
  }
`;

const GoogleButton = styled.button`
  width: clamp(60px, 19vw, 75px);
  height: clamp(60px, 19vw, 75px);
  background: white;
  border-radius: 10px;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16.7%;
  transition: all 0.2s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0px 6px 6px 0px rgba(0, 0, 0, 0.3);
  }

  &:active {
    transform: translateY(0);
  }
`;

const GoogleIcon = styled.img`
  width: 100%;
  height: 100%;
  object-fit: contain;
`;

const AppleButton = styled.button`
  width: clamp(60px, 19vw, 75px);
  height: clamp(60px, 19vw, 75px);
  background: black;
  border-radius: 10px;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20%;
  transition: all 0.2s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0px 6px 6px 0px rgba(0, 0, 0, 0.3);
  }

  &:active {
    transform: translateY(0);
  }
`;

const AppleIcon = styled.img`
  width: 76%;
  height: 90%;
  object-fit: contain;
`;

const KakaoButton = styled.button`
  width: clamp(60px, 19vw, 75px);
  height: clamp(60px, 19vw, 75px);
  background: white;
  border-radius: 10px;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  border: none;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0px 6px 6px 0px rgba(0, 0, 0, 0.3);
  }

  &:active {
    transform: translateY(0);
  }
`;

export default WelcomePage;
