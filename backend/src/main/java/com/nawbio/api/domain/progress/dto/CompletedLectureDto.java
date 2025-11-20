package com.nawbio.api.domain.progress.dto;

import com.nawbio.api.domain.lecture.LectureSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedLectureDto {
    private Long lectureId;
    private String title;
    private String category;
    private Integer level;
    private LocalDateTime completedAt;
    private Double finalScore;
    private Integer correctAnswers;
    private Integer totalQuizzes;

    public static CompletedLectureDto fromSession(LectureSession session) {
        return CompletedLectureDto.builder()
                .lectureId(session.getLecture().getId())
                .title(session.getLecture().getTitle())
                .category(session.getLecture().getCategory())
                .level(session.getLecture().getLevel())
                .completedAt(session.getCompletedAt())
                .finalScore(session.getFinalScore())
                .correctAnswers(session.getCorrectAnswers())
                .totalQuizzes(session.getTotalQuizzes())
                .build();
    }
}
