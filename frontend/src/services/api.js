import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor - JWT 토큰 추가
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor - 에러 처리
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // 토큰 만료 시 로그아웃
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authApi = {
  getCurrentUser: () => api.get('/api/auth/me'),
  getGoogleLoginUrl: () => `${API_URL}/api/auth/oauth2/authorization/google`,
  getKakaoLoginUrl: () => `${API_URL}/api/auth/oauth2/authorization/kakao`,
};

// Lecture API
export const lectureApi = {
  getLectures: (params) => api.get('/api/lectures', { params }),
  getLectureById: (id) => api.get(`/api/lectures/${id}`),
};

// Learning API
export const learningApi = {
  startLearning: (lectureId) => api.post('/api/learning/start', { lectureId }),
  submitAnswer: (formData) => api.post('/api/learning/submit-answer', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
  getProgress: () => api.get('/api/progress/me'),
};

export default api;
