import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

interface ComponentInput {
  type: 'QUESTION' | 'INFO';
  startTime: number;
  question: string;
  expectedAnswer: string;
  wrongAnswer: string;
  improvement: string;
  keywords: string;
}

interface LectureInput {
  title: string;
  artist: string;
  level: number;
  type: 'MUSIC' | 'DRAMA' | 'TALK';
  videoUrl: string;
  thumbnailUrl: string;
  duration: number;
  components: ComponentInput[];
}

const LectureCreatePage = () => {
  const navigate = useNavigate();

  const [lecture, setLecture] = useState<LectureInput>({
    title: '',
    artist: '',
    level: 1,
    type: 'MUSIC',
    videoUrl: '',
    thumbnailUrl: '',
    duration: 0,
    components: [
      { type: 'QUESTION', startTime: 0, question: '', expectedAnswer: '', wrongAnswer: '', improvement: '', keywords: '' },
      { type: 'QUESTION', startTime: 0, question: '', expectedAnswer: '', wrongAnswer: '', improvement: '', keywords: '' },
      { type: 'QUESTION', startTime: 0, question: '', expectedAnswer: '', wrongAnswer: '', improvement: '', keywords: '' },
    ],
  });

  const [videoFile, setVideoFile] = useState<File | null>(null);
  const [thumbnailFile, setThumbnailFile] = useState<File | null>(null);
  const [videoPreview, setVideoPreview] = useState<string>('');
  const [thumbnailPreview, setThumbnailPreview] = useState<string>('');

  const handleLectureChange = (field: keyof LectureInput, value: any) => {
    setLecture(prev => ({ ...prev, [field]: value }));
  };

  const handleComponentChange = (index: number, field: keyof ComponentInput, value: any) => {
    const newComponents = [...lecture.components];
    newComponents[index] = { ...newComponents[index], [field]: value };
    setLecture(prev => ({ ...prev, components: newComponents }));
  };

  const handleVideoFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      // 파일 크기 검증 (500MB 제한)
      if (file.size > 500 * 1024 * 1024) {
        alert('비디오 파일은 500MB 이하만 업로드 가능합니다.');
        return;
      }

      // 파일 형식 검증
      const allowedTypes = ['video/mp4', 'video/webm', 'video/ogg', 'video/quicktime'];
      if (!allowedTypes.includes(file.type)) {
        alert('지원하는 비디오 형식: MP4, WebM, OGG, MOV');
        return;
      }

      setVideoFile(file);

      // 비디오 미리보기
      const videoUrl = URL.createObjectURL(file);
      setVideoPreview(videoUrl);

      // 비디오 메타데이터 로드 (duration 자동 설정)
      const video = document.createElement('video');
      video.preload = 'metadata';
      video.onloadedmetadata = () => {
        URL.revokeObjectURL(videoUrl);
        setLecture(prev => ({ ...prev, duration: Math.floor(video.duration) }));
      };
      video.src = videoUrl;
    }
  };

  const handleThumbnailFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      // 파일 크기 검증 (5MB 제한)
      if (file.size > 5 * 1024 * 1024) {
        alert('썸네일 이미지는 5MB 이하만 업로드 가능합니다.');
        return;
      }

      // 파일 형식 검증
      const allowedTypes = ['image/jpeg', 'image/png', 'image/webp', 'image/gif'];
      if (!allowedTypes.includes(file.type)) {
        alert('지원하는 이미지 형식: JPG, PNG, WebP, GIF');
        return;
      }

      setThumbnailFile(file);

      // 썸네일 미리보기
      const reader = new FileReader();
      reader.onloadend = () => {
        setThumbnailPreview(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!videoFile) {
      alert('비디오 파일을 선택해주세요.');
      return;
    }

    // FormData 생성 (파일 업로드용)
    const formData = new FormData();
    formData.append('videoFile', videoFile);
    if (thumbnailFile) {
      formData.append('thumbnailFile', thumbnailFile);
    }

    // JSON 데이터 추가
    formData.append('lecture', JSON.stringify({
      title: lecture.title,
      artist: lecture.artist,
      level: lecture.level,
      type: lecture.type,
      duration: lecture.duration,
      components: lecture.components,
    }));

    // TODO: API 호출
    console.log('Submitting lecture with files:', {
      lecture,
      videoFile,
      thumbnailFile,
    });

    // 임시: 성공 시 목록 페이지로 이동
    alert('강의가 성공적으로 등록되었습니다!');
    navigate('/lectures');
  };

  const handleCancel = () => {
    if (window.confirm('작성 중인 내용이 사라집니다. 취소하시겠습니까?')) {
      navigate('/lectures');
    }
  };

  return (
    <Container>
      <Header>
        <Title>새 강의 등록</Title>
        <Subtitle>강의 정보와 질문을 입력해주세요</Subtitle>
      </Header>

      <Form onSubmit={handleSubmit}>
        {/* 기본 정보 섹션 */}
        <Section>
          <SectionTitle>📚 기본 정보</SectionTitle>

          <FormGroup>
            <Label>강의 제목 *</Label>
            <Input
              type="text"
              value={lecture.title}
              onChange={(e) => handleLectureChange('title', e.target.value)}
              placeholder="예: Jazz 즉흥 연주 입문"
              required
            />
          </FormGroup>

          <FormGroup>
            <Label>아티스트</Label>
            <Input
              type="text"
              value={lecture.artist}
              onChange={(e) => handleLectureChange('artist', e.target.value)}
              placeholder="예: 홍길동"
            />
          </FormGroup>

          <FormRow>
            <FormGroup>
              <Label>레벨 *</Label>
              <Select
                value={lecture.level}
                onChange={(e) => handleLectureChange('level', Number(e.target.value))}
                required
              >
                <option value={1}>Level 1 (초급)</option>
                <option value={2}>Level 2 (중급)</option>
                <option value={3}>Level 3 (고급)</option>
              </Select>
            </FormGroup>

            <FormGroup>
              <Label>타입 *</Label>
              <Select
                value={lecture.type}
                onChange={(e) => handleLectureChange('type', e.target.value as 'MUSIC' | 'DRAMA' | 'TALK')}
                required
              >
                <option value="MUSIC">Music (음악)</option>
                <option value="DRAMA">Drama (연극)</option>
                <option value="TALK">Talk (토크)</option>
              </Select>
            </FormGroup>
          </FormRow>
        </Section>

        {/* 비디오 섹션 */}
        <Section>
          <SectionTitle>🎥 비디오 업로드</SectionTitle>

          <FormGroup>
            <Label>비디오 파일 *</Label>
            <FileInputWrapper>
              <FileInput
                type="file"
                id="videoFile"
                accept="video/mp4,video/webm,video/ogg,video/quicktime"
                onChange={handleVideoFileChange}
                required
              />
              <FileInputLabel htmlFor="videoFile">
                {videoFile ? `✓ ${videoFile.name}` : '📁 비디오 파일 선택'}
              </FileInputLabel>
            </FileInputWrapper>
            <HelpText>
              지원 형식: MP4, WebM, OGG, MOV (최대 500MB)
              {videoFile && ` • 파일 크기: ${(videoFile.size / (1024 * 1024)).toFixed(2)}MB`}
            </HelpText>
            {videoPreview && (
              <VideoPreview controls>
                <source src={videoPreview} type={videoFile?.type} />
              </VideoPreview>
            )}
          </FormGroup>

          <FormGroup>
            <Label>썸네일 이미지</Label>
            <FileInputWrapper>
              <FileInput
                type="file"
                id="thumbnailFile"
                accept="image/jpeg,image/png,image/webp,image/gif"
                onChange={handleThumbnailFileChange}
              />
              <FileInputLabel htmlFor="thumbnailFile">
                {thumbnailFile ? `✓ ${thumbnailFile.name}` : '🖼️ 썸네일 이미지 선택'}
              </FileInputLabel>
            </FileInputWrapper>
            <HelpText>
              지원 형식: JPG, PNG, WebP, GIF (최대 5MB)
              {thumbnailFile && ` • 파일 크기: ${(thumbnailFile.size / (1024 * 1024)).toFixed(2)}MB`}
            </HelpText>
            {thumbnailPreview && (
              <ThumbnailPreview src={thumbnailPreview} alt="썸네일 미리보기" />
            )}
          </FormGroup>

          <FormGroup>
            <Label>비디오 길이 (초)</Label>
            <Input
              type="number"
              value={lecture.duration}
              onChange={(e) => handleLectureChange('duration', Number(e.target.value))}
              placeholder="비디오 파일 선택 시 자동 입력됩니다"
              min="0"
              disabled={!!videoFile}
            />
            <HelpText>
              {videoFile
                ? '비디오 파일에서 자동으로 추출되었습니다'
                : '비디오 파일을 선택하면 자동으로 입력됩니다'}
            </HelpText>
          </FormGroup>
        </Section>

        {/* 질문 섹션 */}
        <Section>
          <SectionTitle>❓ 질문 구성 (3개)</SectionTitle>

          {lecture.components.map((component, index) => (
            <QuestionCard key={index}>
              <QuestionHeader>질문 {index + 1}</QuestionHeader>

              <FormGroup>
                <Label>등장 시간 (초) *</Label>
                <Input
                  type="number"
                  value={component.startTime}
                  onChange={(e) => handleComponentChange(index, 'startTime', Number(e.target.value))}
                  placeholder="60"
                  min="0"
                  max={lecture.duration}
                  required
                />
                <HelpText>이 질문이 나타날 시간을 초 단위로 입력하세요</HelpText>
              </FormGroup>

              <FormGroup>
                <Label>질문 내용 *</Label>
                <Textarea
                  value={component.question}
                  onChange={(e) => handleComponentChange(index, 'question', e.target.value)}
                  placeholder="학습자에게 던질 질문을 입력하세요"
                  rows={3}
                  required
                />
              </FormGroup>

              <FormGroup>
                <Label>정답 (예시 답변) *</Label>
                <Textarea
                  value={component.expectedAnswer}
                  onChange={(e) => handleComponentChange(index, 'expectedAnswer', e.target.value)}
                  placeholder="올바른 답변의 예시를 입력하세요"
                  rows={3}
                  required
                />
              </FormGroup>

              <FormGroup>
                <Label>오답 (예시)</Label>
                <Textarea
                  value={component.wrongAnswer}
                  onChange={(e) => handleComponentChange(index, 'wrongAnswer', e.target.value)}
                  placeholder="잘못된 답변의 예시를 입력하세요 (선택사항)"
                  rows={2}
                />
              </FormGroup>

              <FormGroup>
                <Label>개선사항 / 피드백</Label>
                <Textarea
                  value={component.improvement}
                  onChange={(e) => handleComponentChange(index, 'improvement', e.target.value)}
                  placeholder="학습자에게 줄 개선사항이나 피드백을 입력하세요 (선택사항)"
                  rows={2}
                />
              </FormGroup>

              <FormGroup>
                <Label>키워드 (쉼표로 구분)</Label>
                <Input
                  type="text"
                  value={component.keywords}
                  onChange={(e) => handleComponentChange(index, 'keywords', e.target.value)}
                  placeholder="재즈, 즉흥연주, 스윙"
                />
                <HelpText>관련 키워드를 쉼표(,)로 구분하여 입력하세요</HelpText>
              </FormGroup>
            </QuestionCard>
          ))}
        </Section>

        {/* 버튼 섹션 */}
        <ButtonGroup>
          <CancelButton type="button" onClick={handleCancel}>
            취소
          </CancelButton>
          <SubmitButton type="submit">
            강의 등록
          </SubmitButton>
        </ButtonGroup>
      </Form>
    </Container>
  );
};

export default LectureCreatePage;

// Styled Components
const Container = styled.div`
  max-width: 900px;
  margin: 0 auto;
  padding: ${props => props.theme.spacing.xl};
  background: ${props => props.theme.colors.light};
  min-height: 100vh;
`;

const Header = styled.div`
  margin-bottom: ${props => props.theme.spacing.xl};
`;

const Title = styled.h1`
  font-size: 2rem;
  font-weight: bold;
  color: ${props => props.theme.colors.dark};
  margin-bottom: ${props => props.theme.spacing.sm};
`;

const Subtitle = styled.p`
  font-size: 1rem;
  color: ${props => props.theme.colors.gray[600]};
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: ${props => props.theme.spacing.xl};
`;

const Section = styled.div`
  background: white;
  padding: ${props => props.theme.spacing.xl};
  border-radius: ${props => props.theme.borderRadius.lg};
  box-shadow: ${props => props.theme.shadows.md};
`;

const SectionTitle = styled.h2`
  font-size: 1.5rem;
  font-weight: 600;
  color: ${props => props.theme.colors.dark};
  margin-bottom: ${props => props.theme.spacing.lg};
  padding-bottom: ${props => props.theme.spacing.md};
  border-bottom: 2px solid ${props => props.theme.colors.gray[200]};
`;

const FormGroup = styled.div`
  margin-bottom: ${props => props.theme.spacing.lg};

  &:last-child {
    margin-bottom: 0;
  }
`;

const FormRow = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: ${props => props.theme.spacing.lg};

  @media (max-width: ${props => props.theme.breakpoints.tablet}) {
    grid-template-columns: 1fr;
  }
`;

const Label = styled.label`
  display: block;
  font-size: 0.875rem;
  font-weight: 600;
  color: ${props => props.theme.colors.dark};
  margin-bottom: ${props => props.theme.spacing.sm};
`;

const Input = styled.input`
  width: 100%;
  padding: ${props => props.theme.spacing.md};
  font-size: 1rem;
  border: 1px solid ${props => props.theme.colors.gray[300]};
  border-radius: ${props => props.theme.borderRadius.md};
  transition: all 0.2s;

  &:focus {
    outline: none;
    border-color: ${props => props.theme.colors.primary};
    box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
  }

  &::placeholder {
    color: ${props => props.theme.colors.gray[400]};
  }
`;

const Select = styled.select`
  width: 100%;
  padding: ${props => props.theme.spacing.md};
  font-size: 1rem;
  border: 1px solid ${props => props.theme.colors.gray[300]};
  border-radius: ${props => props.theme.borderRadius.md};
  background: white;
  cursor: pointer;
  transition: all 0.2s;

  &:focus {
    outline: none;
    border-color: ${props => props.theme.colors.primary};
    box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
  }
`;

const Textarea = styled.textarea`
  width: 100%;
  padding: ${props => props.theme.spacing.md};
  font-size: 1rem;
  border: 1px solid ${props => props.theme.colors.gray[300]};
  border-radius: ${props => props.theme.borderRadius.md};
  resize: vertical;
  font-family: inherit;
  transition: all 0.2s;

  &:focus {
    outline: none;
    border-color: ${props => props.theme.colors.primary};
    box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
  }

  &::placeholder {
    color: ${props => props.theme.colors.gray[400]};
  }
`;

const HelpText = styled.span`
  display: block;
  font-size: 0.75rem;
  color: ${props => props.theme.colors.gray[500]};
  margin-top: ${props => props.theme.spacing.xs};
`;

const QuestionCard = styled.div`
  background: ${props => props.theme.colors.gray[50]};
  padding: ${props => props.theme.spacing.lg};
  border-radius: ${props => props.theme.borderRadius.md};
  margin-bottom: ${props => props.theme.spacing.lg};
  border: 1px solid ${props => props.theme.colors.gray[200]};

  &:last-child {
    margin-bottom: 0;
  }
`;

const QuestionHeader = styled.h3`
  font-size: 1.125rem;
  font-weight: 600;
  color: ${props => props.theme.colors.primary};
  margin-bottom: ${props => props.theme.spacing.lg};
  padding-bottom: ${props => props.theme.spacing.sm};
  border-bottom: 1px solid ${props => props.theme.colors.gray[300]};
`;

const ButtonGroup = styled.div`
  display: flex;
  gap: ${props => props.theme.spacing.md};
  justify-content: flex-end;
  padding-top: ${props => props.theme.spacing.lg};
`;

const Button = styled.button`
  padding: ${props => props.theme.spacing.md} ${props => props.theme.spacing.xl};
  font-size: 1rem;
  font-weight: 600;
  border-radius: ${props => props.theme.borderRadius.md};
  cursor: pointer;
  transition: all 0.2s;
  border: none;

  &:hover {
    transform: translateY(-1px);
    box-shadow: ${props => props.theme.shadows.md};
  }

  &:active {
    transform: translateY(0);
  }
`;

const CancelButton = styled(Button)`
  background: white;
  color: ${props => props.theme.colors.gray[700]};
  border: 1px solid ${props => props.theme.colors.gray[300]};

  &:hover {
    background: ${props => props.theme.colors.gray[50]};
  }
`;

const SubmitButton = styled(Button)`
  background: ${props => props.theme.colors.primary};
  color: white;

  &:hover {
    background: ${props => props.theme.colors.secondary};
  }
`;

const FileInputWrapper = styled.div`
  position: relative;
`;

const FileInput = styled.input`
  position: absolute;
  width: 0;
  height: 0;
  opacity: 0;
  overflow: hidden;
`;

const FileInputLabel = styled.label`
  display: inline-block;
  width: 100%;
  padding: ${props => props.theme.spacing.lg};
  font-size: 1rem;
  font-weight: 500;
  text-align: center;
  color: ${props => props.theme.colors.primary};
  background: ${props => props.theme.colors.gray[50]};
  border: 2px dashed ${props => props.theme.colors.gray[300]};
  border-radius: ${props => props.theme.borderRadius.md};
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background: ${props => props.theme.colors.gray[100]};
    border-color: ${props => props.theme.colors.primary};
  }

  &:active {
    transform: scale(0.98);
  }
`;

const VideoPreview = styled.video`
  width: 100%;
  max-height: 400px;
  margin-top: ${props => props.theme.spacing.md};
  border-radius: ${props => props.theme.borderRadius.md};
  background: ${props => props.theme.colors.dark};
`;

const ThumbnailPreview = styled.img`
  width: 100%;
  max-width: 300px;
  height: auto;
  margin-top: ${props => props.theme.spacing.md};
  border-radius: ${props => props.theme.borderRadius.md};
  border: 1px solid ${props => props.theme.colors.gray[200]};
`;
