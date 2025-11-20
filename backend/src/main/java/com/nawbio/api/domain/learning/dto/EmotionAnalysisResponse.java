package com.nawbio.api.domain.learning.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class EmotionAnalysisResponse {
    private Long sessionId;
    private String dominantEmotion;
    private Double confidence;
    private Map<String, Double> emotions; // All emotion scores
    private Long timestamp;
    private String message;
}
