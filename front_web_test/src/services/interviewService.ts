// Interview API Service
// Handles 3rd stage phone interview with emotion analysis

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://43.203.228.241:8080/api';

export interface SessionStartRequest {
  userId: string;
  stage: number;
}

export interface SessionResponse {
  sessionId: number;
  userId: string;
  stage: number;
  status: string;
  webcamEnabled: boolean;
  startedAt: string;
  endedAt?: string;
  message?: string;
}

export interface EmotionAnalysisRequest {
  sessionId: number;
  imageData: string; // Base64 encoded image
  timestamp: number;
}

export interface EmotionAnalysisResponse {
  sessionId: number;
  dominantEmotion: string;
  confidence: number;
  emotions: Record<string, number>;
  timestamp: number;
  message?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
}

class InterviewService {
  private async fetchAPI<T>(endpoint: string, options?: RequestInit): Promise<T> {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options?.headers,
      },
    });

    if (!response.ok) {
      throw new Error(`API Error: ${response.statusText}`);
    }

    const result: ApiResponse<T> = await response.json();

    if (!result.success) {
      throw new Error(result.message || 'API request failed');
    }

    return result.data;
  }

  async startSession(request: SessionStartRequest): Promise<SessionResponse> {
    return this.fetchAPI<SessionResponse>('/interview/start', {
      method: 'POST',
      body: JSON.stringify(request),
    });
  }

  async analyzeEmotion(request: EmotionAnalysisRequest): Promise<EmotionAnalysisResponse> {
    return this.fetchAPI<EmotionAnalysisResponse>('/interview/analyze', {
      method: 'POST',
      body: JSON.stringify(request),
    });
  }

  async endSession(sessionId: number): Promise<SessionResponse> {
    return this.fetchAPI<SessionResponse>(`/interview/${sessionId}/end`, {
      method: 'POST',
    });
  }

  async getSession(sessionId: number): Promise<SessionResponse> {
    return this.fetchAPI<SessionResponse>(`/interview/${sessionId}`, {
      method: 'GET',
    });
  }

  async getUserSessions(userId: string): Promise<SessionResponse[]> {
    return this.fetchAPI<SessionResponse[]>(`/interview/user/${userId}`, {
      method: 'GET',
    });
  }
}

export const interviewService = new InterviewService();
