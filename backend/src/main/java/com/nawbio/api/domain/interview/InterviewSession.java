package com.nawbio.api.domain.interview;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class InterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, unique = true, length = 100)
    private String sessionId;

    @Column(name = "user_id")
    private Long userId; // 추후 User 엔티티 연결

    @Column(name = "prompt", columnDefinition = "TEXT")
    private String prompt; // "마케팅 인턴 면접 준비"

    @Column(name = "duration_seconds")
    private Integer durationSeconds; // 180초 (3분)

    @Column(name = "status", length = 50)
    @Builder.Default
    private String status = "recording"; // recording, completed, analyzed

    @Column(name = "video_recording_url", length = 500)
    private String videoRecordingUrl;

    @Column(name = "overall_score")
    private Double overallScore;

    @Column(name = "total_questions")
    @Builder.Default
    private Integer totalQuestions = 0;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InterviewQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<QAPair> qaPairs = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EmotionAnalysis> emotionAnalyses = new ArrayList<>();

    // 편의 메서드
    public void addQuestion(InterviewQuestion question) {
        questions.add(question);
        question.setSession(this);
        totalQuestions++;
    }

    public void addQAPair(QAPair qaPair) {
        qaPairs.add(qaPair);
        qaPair.setSession(this);
    }

    public void addEmotionAnalysis(EmotionAnalysis analysis) {
        emotionAnalyses.add(analysis);
        analysis.setSession(this);
    }

    public void complete(String videoUrl) {
        this.status = "completed";
        this.completedAt = LocalDateTime.now();
        this.videoRecordingUrl = videoUrl;
    }

    public void analyze(Double score) {
        this.status = "analyzed";
        this.overallScore = score;
    }
}
