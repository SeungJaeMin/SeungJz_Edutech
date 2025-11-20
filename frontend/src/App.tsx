import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider } from 'styled-components';
import { GlobalStyle } from './styles/GlobalStyle';
import { theme } from './styles/theme';

// Pages
import WelcomePage from './pages/WelcomePage';
import HomePage from './pages/HomePage';
import LectureListPage from './pages/LectureListPage';
import LectureCreatePage from './pages/LectureCreatePage';
import LearningPage from './pages/LearningPage';
import InterviewPage from './pages/InterviewPage';
import ReviewPage from './pages/ReviewPage';

function App() {
  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <Router>
        <Routes>
          <Route path="/" element={<WelcomePage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/lectures" element={<LectureListPage />} />
          <Route path="/lectures/create" element={<LectureCreatePage />} />
          <Route path="/learning/:id" element={<LearningPage />} />
          <Route path="/interview" element={<InterviewPage />} />
          <Route path="/review" element={<ReviewPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;
