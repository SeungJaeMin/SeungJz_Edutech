package com.nawbio.api.domain.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerSubmitResponse {
    private Long qaPairId;
    private Long questionId;
    private String transcribedText;
    private Integer answerDurationSec;
    private AnalysisResult analysisResult;
    private QuestionDto nextQuestion;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnalysisResult {
        private Boolean hasKeywords;
        private List<String> missingKeywords;
        private Boolean isCoherent;
        private Double confidence;
    }
}
