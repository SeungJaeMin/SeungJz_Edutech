package com.nawbio.api.domain.learning.dto;

import com.nawbio.api.domain.learning.InterviewSession;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SessionResponse {
    private Long sessionId;
    private String userId;
    private Integer stage;
    private String status;
    private Boolean webcamEnabled;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String message;

    public static SessionResponse from(InterviewSession session) {
        return SessionResponse.builder()
                .sessionId(session.getId())
                .userId(session.getUserId())
                .stage(session.getStage())
                .status(session.getStatus().name())
                .webcamEnabled(session.getWebcamEnabled())
                .startedAt(session.getStartedAt())
                .endedAt(session.getEndedAt())
                .build();
    }
}
