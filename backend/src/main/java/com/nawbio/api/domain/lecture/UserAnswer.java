package com.nawbio.api.domain.lecture;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_answers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private LectureSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "transcribed_text", columnDefinition = "TEXT")
    private String transcribedText; // STT 결과

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "similarity_score")
    private Double similarityScore; // 0.0 ~ 1.0

    @Column(name = "attempt_number")
    @Builder.Default
    private Integer attemptNumber = 1;

    @Column(name = "audio_url", length = 500)
    private String audioUrl; // 사용자 음성 파일 URL

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
