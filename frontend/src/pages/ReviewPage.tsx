import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const ReviewPage = () => {
  const navigate = useNavigate();

  const learningHistory = [
    { date: '11. 21.', items: [{ title: '슈퍼노바' }] },
    { date: '11. 20.', items: [{ title: 'APT' }, { title: '사랑은 늘 도망가' }] },
    { date: '11. 19.', items: [{ title: '비즈니스 미팅' }, { title: '첫 만남 장면' }] },
  ];

  return (
    <Container>
      <HistoryList>
        {learningHistory.map((section, idx) => (
          <Section key={idx}>
            <DateHeader>{section.date}</DateHeader>
            <Divider />
            {section.items.map((item, itemIdx) => (
              <HistoryCard key={itemIdx}>
                <Thumbnail />
                <CardInfo>
                  <CardTitle>제목</CardTitle>
                </CardInfo>
                <Arrow>→</Arrow>
              </HistoryCard>
            ))}
          </Section>
        ))}
      </HistoryList>

      <BottomNav>
        <BackButton onClick={() => navigate('/home')}>
          <BackArrow>←</BackArrow>
        </BackButton>

        <RecordButton>기록</RecordButton>
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
  display: flex;
  flex-direction: column;
  margin: 0 auto;

  @media (max-width: 393px) {
    max-width: 100vw;
  }
`;

const HistoryList = styled.div`
  flex: 1;
  padding: clamp(20px, 7.6vw, 30px);
  overflow-y: auto;
  padding-bottom: 140px;
`;

const Section = styled.div`
  margin-bottom: clamp(32px, 10vw, 40px);
`;

const DateHeader = styled.h2`
  font-family: 'Inter', 'Noto Sans KR', sans-serif;
  font-size: clamp(20px, 6vw, 24px);
  font-weight: 400;
  color: black;
  margin-bottom: 12px;
`;

const Divider = styled.div`
  height: 1px;
  background: #e0e0e0;
  margin-bottom: 12px;
`;

const HistoryCard = styled.button`
  display: flex;
  align-items: center;
  gap: clamp(10px, 3vw, 12px);
  padding: clamp(10px, 3vw, 12px);
  background: white;
  border-radius: 12px;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 12px;
  width: 100%;
  height: clamp(54px, 16.3vw, 64px);
  text-align: left;
  transition: all 0.2s;
  border: none;
  cursor: pointer;

  &:hover {
    transform: translateX(4px);
    box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.15);
  }

  &:active {
    transform: translateX(2px);
  }
`;

const Thumbnail = styled.div`
  width: clamp(35px, 10.7vw, 42px);
  height: clamp(35px, 10.7vw, 42px);
  border-radius: 50%;
  background: #d9d9d9;
  flex-shrink: 0;
`;

const CardInfo = styled.div`
  flex: 1;
`;

const CardTitle = styled.div`
  font-family: 'Inter', 'Noto Sans KR', sans-serif;
  font-size: clamp(20px, 6vw, 24px);
  font-weight: 400;
  color: black;
`;

const Arrow = styled.div`
  font-size: clamp(18px, 5vw, 20px);
  color: black;
`;

const BottomNav = styled.nav`
  position: absolute;
  bottom: 2.9%;
  left: 50%;
  transform: translateX(-50%);
  width: 92.4%;
  display: flex;
  justify-content: space-between;
  align-items: center;
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

const RecordButton = styled.button`
  width: clamp(183px, 55.2vw, 217px);
  height: clamp(85px, 25.4vw, 100px);
  background: white;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-family: 'Inter', 'Noto Sans KR', sans-serif;
  font-size: clamp(20px, 6vw, 24px);
  font-weight: 400;
  color: black;
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;

  &:hover {
    background: #f5f5f5;
    transform: scale(1.02);
  }

  &:active {
    transform: scale(0.98);
  }
`;

export default ReviewPage;
