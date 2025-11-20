package com.nawbio.api.domain.lecture.dto;

import com.nawbio.api.domain.lecture.Quiz;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private Long id;
    private Integer sequence;
    private String question;
    private String correctAnswer;
    private Integer triggerTimeSec;
    private String correctFeedback;
    private String incorrectFeedback;
    private Integer maxAttempts;
    private String hint;

    public static QuizDto fromEntity(Quiz quiz) {
        return QuizDto.builder()
                .id(quiz.getId())
                .sequence(quiz.getSequence())
                .question(quiz.getQuestion())
                .correctAnswer(quiz.getCorrectAnswer())
                .triggerTimeSec(quiz.getTriggerTimeSec())
                .correctFeedback(quiz.getCorrectFeedback())
                .incorrectFeedback(quiz.getIncorrectFeedback())
                .maxAttempts(quiz.getMaxAttempts())
                .hint(quiz.getHint())
                .build();
    }
}
