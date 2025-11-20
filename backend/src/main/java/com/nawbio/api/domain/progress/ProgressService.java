package com.nawbio.api.domain.progress;

import com.nawbio.api.domain.lecture.LectureSession;
import com.nawbio.api.domain.lecture.LectureSessionRepository;
import com.nawbio.api.domain.progress.dto.CompletedLectureDto;
import com.nawbio.api.domain.progress.dto.LectureProgressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProgressService {

    private final LectureSessionRepository sessionRepository;

    // 완료한 Lecture 목록 조회
    public LectureProgressResponse getCompletedLectures(Long userId, Integer level) {
        // 완료된 세션 조회
        List<LectureSession> sessions = sessionRepository.findByUserIdOrderByCreatedAtDesc(userId);

        // 레벨 필터링 및 완료된 것만
        List<CompletedLectureDto> completedLectures = sessions.stream()
                .filter(session -> session.getIsCompleted())
                .filter(session -> level == null || session.getLecture().getLevel().equals(level))
                .map(CompletedLectureDto::fromSession)
                .collect(Collectors.toList());

        // 통계 계산
        Integer totalCompleted = completedLectures.size();
        Double averageScore = completedLectures.stream()
                .mapToDouble(CompletedLectureDto::getFinalScore)
                .average()
                .orElse(0.0);

        return LectureProgressResponse.builder()
                .completedLectures(completedLectures)
                .totalCompleted(totalCompleted)
                .averageScore(Math.round(averageScore * 10) / 10.0)
                .build();
    }

    // 전체 학습 통계
    public LectureProgressResponse getAllProgress(Long userId) {
        return getCompletedLectures(userId, null);
    }
}
