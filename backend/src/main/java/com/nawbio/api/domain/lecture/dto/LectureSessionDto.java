package com.nawbio.api.domain.lecture.dto;

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
public class LectureSessionDto {
    private String sessionId;
    private Long lectureId;
    private Integer currentQuizSequence;
    private Integer totalQuizzes;
    private Integer correctAnswers;
    private Boolean isCompleted;
    private Double finalScore;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Integer nextQuizTriggerSec;

    public static LectureSessionDto fromEntity(LectureSession session) {
        return LectureSessionDto.builder()
                .sessionId(session.getSessionId())
                .lectureId(session.getLecture().getId())
                .currentQuizSequence(session.getCurrentQuizSequence())
                .totalQuizzes(session.getTotalQuizzes())
                .correctAnswers(session.getCorrectAnswers())
                .isCompleted(session.getIsCompleted())
                .finalScore(session.getFinalScore())
                .startedAt(session.getStartedAt())
                .completedAt(session.getCompletedAt())
                .build();
    }
}
