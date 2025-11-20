package com.nawbio.api.domain.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewStartResponse {
    private String sessionId;
    private String status;
    private Integer durationSeconds;
    private LocalDateTime startedAt;
    private QuestionDto initialQuestion;
}
