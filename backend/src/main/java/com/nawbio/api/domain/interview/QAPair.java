package com.nawbio.api.domain.interview;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "qa_pairs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class QAPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private InterviewSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private InterviewQuestion question;

    @Column(name = "transcribed_text", columnDefinition = "TEXT")
    private String transcribedText; // STT 결과

    @Column(name = "answer_duration_sec")
    private Integer answerDurationSec;

    @Column(name = "timeline_sec")
    private Integer timelineSec; // 면접 타임라인에서의 시점

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    @Column(name = "has_keywords")
    private Boolean hasKeywords;

    @Column(name = "is_coherent")
    private Boolean isCoherent;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
