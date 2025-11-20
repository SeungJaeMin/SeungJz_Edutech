import api from './api';

export interface ComponentData {
  type: 'QUESTION' | 'INFO';
  startTime: number;
  question: string;
  expectedAnswer: string;
  keywords: string;
}

export interface LectureData {
  title: string;
  artist: string;
  level: number;
  type: 'MUSIC' | 'DRAMA' | 'TALK';
  duration: number;
  components: ComponentData[];
}

export interface LectureResponse {
  id: number;
  title: string;
  artist: string;
  level: number;
  type: 'MUSIC' | 'DRAMA' | 'TALK';
  videoUrl: string;
  thumbnailUrl: string;
  duration: number;
  components: ComponentResponse[];
  createdAt: string;
  updatedAt: string;
}

export interface ComponentResponse {
  id: number;
  type: 'QUESTION' | 'INFO';
  startTime: number;
  question: string;
  expectedAnswer: string;
  keywords: string;
}

class LectureService {
  /**
   * 새 강의 생성 (파일 업로드 포함)
   */
  async createLecture(
    lectureData: LectureData,
    videoFile: File,
    thumbnailFile?: File
  ): Promise<LectureResponse> {
    const formData = new FormData();

    // JSON 데이터 추가
    formData.append('lecture', JSON.stringify(lectureData));

    // 비디오 파일 추가
    formData.append('videoFile', videoFile);

    // 썸네일 파일 추가 (선택사항)
    if (thumbnailFile) {
      formData.append('thumbnailFile', thumbnailFile);
    }

    const response = await api.post<LectureResponse>('/api/lectures', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    return response.data;
  }

  /**
   * 모든 강의 조회
   */
  async getAllLectures(): Promise<LectureResponse[]> {
    const response = await api.get<LectureResponse[]>('/api/lectures');
    return response.data;
  }

  /**
   * 특정 강의 조회
   */
  async getLecture(id: number): Promise<LectureResponse> {
    const response = await api.get<LectureResponse>(`/api/lectures/${id}`);
    return response.data;
  }

  /**
   * 타입별 강의 조회
   */
  async getLecturesByType(type: 'MUSIC' | 'DRAMA' | 'TALK'): Promise<LectureResponse[]> {
    const response = await api.get<LectureResponse[]>(`/api/lectures/type/${type}`);
    return response.data;
  }

  /**
   * 레벨별 강의 조회
   */
  async getLecturesByLevel(level: number): Promise<LectureResponse[]> {
    const response = await api.get<LectureResponse[]>(`/api/lectures/level/${level}`);
    return response.data;
  }

  /**
   * 강의 수정
   */
  async updateLecture(
    id: number,
    lectureData: LectureData,
    videoFile?: File,
    thumbnailFile?: File
  ): Promise<LectureResponse> {
    const formData = new FormData();

    formData.append('lecture', JSON.stringify(lectureData));

    if (videoFile) {
      formData.append('videoFile', videoFile);
    }

    if (thumbnailFile) {
      formData.append('thumbnailFile', thumbnailFile);
    }

    const response = await api.put<LectureResponse>(`/api/lectures/${id}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    return response.data;
  }

  /**
   * 강의 삭제
   */
  async deleteLecture(id: number): Promise<void> {
    await api.delete(`/api/lectures/${id}`);
  }
}

export default new LectureService();
