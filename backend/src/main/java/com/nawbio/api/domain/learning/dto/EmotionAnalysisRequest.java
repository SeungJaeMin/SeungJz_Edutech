package com.nawbio.api.domain.learning.dto;

import lombok.Data;

@Data
public class EmotionAnalysisRequest {
    private Long sessionId;
    private String imageData; // Base64 encoded image from webcam
    private Long timestamp;
}
