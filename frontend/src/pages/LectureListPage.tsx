import { useNavigate, useSearchParams } from 'react-router-dom';
import styled from 'styled-components';

const LectureListPage = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const level = searchParams.get('level') || '1';

  const lectures = [
    { id: 1, title: '슈퍼노바', artist: 'aespa' },
    { id: 2, title: 'APT', artist: '로제 & Bruno Mars' },
    { id: 3, title: '사랑은 늘 도망가', artist: '임영웅' },
  ];

  return (
    <Container>
      <VideoPlaceholder />

      <ContentCard>
        <CardInner>
          <LectureItem onClick={() => navigate(`/learning/${lectures[0].id}`)}>
            <Thumbnail />
            <LectureInfo>
              <Title>제목</Title>
              <Artist>가수</Artist>
            </LectureInfo>
            <Arrow>→</Arrow>
          </LectureItem>
        </CardInner>
      </ContentCard>

      <BottomNav>
        <BackButton onClick={() => navigate('/home')}>
          <BackArrow>←</BackArrow>
        </BackButton>

        <CenterButton />

        <MenuButton>
          <MenuDots>...</MenuDots>
        </MenuButton>
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

const VideoPlaceholder = styled.div`
  position: absolute;
  width: 82.7%;
  height: 71%;
  left: 50%;
  transform: translateX(-50%);
  top: 8.8%;
  background: #e5e5e5;
  border-radius: 12px;
`;

const ContentCard = styled.div`
  position: absolute;
  width: 77.6%;
  height: 68.3%;
  left: 50%;
  transform: translateX(-50%);
  top: 13.7%;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
`;

const CardInner = styled.div`
  position: relative;
  width: 90.8%;
  height: 94.7%;
  left: 4.6%;
  top: 8.4%;
`;

const LectureItem = styled.button`
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 87%;
  height: 11.6%;
  background: white;
  border: none;
  border-radius: 12px;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: clamp(10px, 3vw, 12px);
  padding: clamp(9px, 2.8vw, 11px);
  transition: all 0.2s;

  &:hover {
    transform: translateX(-50%) translateY(-2px);
    box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.15);
  }

  &:active {
    transform: translateX(-50%) translateY(0);
  }
`;

const Thumbnail = styled.div`
  width: clamp(35px, 10.7vw, 42px);
  height: clamp(35px, 10.7vw, 42px);
  border-radius: 50%;
  background: #d9d9d9;
  flex-shrink: 0;
`;

const LectureInfo = styled.div`
  flex: 1;
  text-align: left;
`;

const Title = styled.div`
  font-family: 'Inter', 'Noto Sans KR', sans-serif;
  font-size: clamp(14px, 4vw, 16px);
  font-weight: 600;
  color: black;
  margin-bottom: 4px;
`;

const Artist = styled.div`
  font-family: 'Inter', 'Noto Sans KR', sans-serif;
  font-size: clamp(12px, 3.5vw, 14px);
  color: #666;
`;

const Arrow = styled.div`
  font-size: clamp(18px, 5vw, 20px);
  color: black;
`;

const BottomNav = styled.nav`
  position: absolute;
  bottom: 4.1%;
  left: 50%;
  transform: translateX(-50%);
  width: 91.9%;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const BackButton = styled.button`
  width: clamp(54px, 16.3vw, 64px);
  height: clamp(54px, 16.3vw, 64px);
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
  }

  &:active {
    transform: scale(0.95);
  }
`;

const BackArrow = styled.div`
  font-size: clamp(20px, 6vw, 24px);
  color: black;
`;

const CenterButton = styled.button`
  width: clamp(140px, 42.5vw, 167px);
  height: clamp(54px, 16.3vw, 64px);
  background: white;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;

  &:hover {
    background: #f5f5f5;
  }

  &:active {
    transform: scale(0.98);
  }
`;

const MenuButton = styled.button`
  width: clamp(54px, 16.3vw, 64px);
  height: clamp(54px, 16.3vw, 64px);
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
  }

  &:active {
    transform: scale(0.95);
  }
`;

const MenuDots = styled.div`
  font-size: clamp(20px, 6vw, 24px);
  color: black;
`;

export default LectureListPage;
