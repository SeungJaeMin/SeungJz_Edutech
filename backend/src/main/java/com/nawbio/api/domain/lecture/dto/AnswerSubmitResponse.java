package com.nawbio.api.domain.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerSubmitResponse {
    private Long answerId;
    private Boolean isCorrect;
    private String transcribedText;
    private String expectedAnswer;
    private Double similarityScore;
    private String feedback;
    private Integer attemptNumber;
    private Integer nextQuizSequence;
    private Integer nextQuizTriggerSec;
    private Integer remainingAttempts;
    private String hint;
}
