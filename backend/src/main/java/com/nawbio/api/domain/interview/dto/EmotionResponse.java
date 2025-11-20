package com.nawbio.api.domain.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionResponse {
    private Integer timelineSec;
    private EmotionData emotion;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmotionData {
        private String dominantEmotion;
        private Double confidence;
        private Map<String, Double> emotionScores;
        private String gazeDirection;
        private Double smileIntensity;
    }
}
