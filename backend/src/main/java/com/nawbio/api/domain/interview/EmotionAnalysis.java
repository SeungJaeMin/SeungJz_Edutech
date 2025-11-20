package com.nawbio.api.domain.interview;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "emotion_analyses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class EmotionAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private InterviewSession session;

    @Column(name = "timeline_sec", nullable = false)
    private Integer timelineSec;

    @Column(name = "dominant_emotion", length = 50)
    private String dominantEmotion; // happy, neutral, anxious, etc.

    @Column(name = "confidence")
    private Double confidence;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "emotion_scores", columnDefinition = "jsonb")
    private Map<String, Double> emotionScores; // {"happy": 0.2, "neutral": 0.7, ...}

    @Column(name = "gaze_direction", length = 50)
    private String gazeDirection; // camera, away, down

    @Column(name = "smile_intensity")
    private Double smileIntensity;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
