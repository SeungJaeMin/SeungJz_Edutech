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
public class InterviewCompleteResponse {
    private String sessionId;
    private String status;
    private LocalDateTime completedAt;
    private String videoRecordingUrl;
    private Integer totalQuestions;
    private Integer totalDurationSec;
    private String message;
}
