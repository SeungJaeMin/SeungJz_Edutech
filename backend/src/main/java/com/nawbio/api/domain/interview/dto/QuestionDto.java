package com.nawbio.api.domain.interview.dto;

import com.nawbio.api.domain.interview.InterviewQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Long questionId;
    private String questionText;
    private String ttsAudioUrl;
    private LocalDateTime generatedAt;

    public static QuestionDto fromEntity(InterviewQuestion question) {
        return QuestionDto.builder()
                .questionId(question.getId())
                .questionText(question.getQuestionText())
                .ttsAudioUrl(question.getTtsAudioUrl())
                .generatedAt(question.getCreatedAt())
                .build();
    }
}
