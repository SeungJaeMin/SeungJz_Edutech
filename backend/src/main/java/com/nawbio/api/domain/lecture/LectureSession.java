package com.nawbio.api.domain.lecture;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lecture_sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LectureSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, unique = true, length = 100)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(name = "user_id")
    private Long userId; // 추후 User 엔티티 추가 시 연결

    @Column(name = "current_quiz_sequence")
    @Builder.Default
    private Integer currentQuizSequence = 1;

    @Column(name = "total_quizzes")
    private Integer totalQuizzes;

    @Column(name = "correct_answers")
    @Builder.Default
    private Integer correctAnswers = 0;

    @Column(name = "is_completed")
    @Builder.Default
    private Boolean isCompleted = false;

    @Column(name = "final_score")
    private Double finalScore;

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

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserAnswer> answers = new ArrayList<>();

    // 편의 메서드
    public void addAnswer(UserAnswer answer) {
        answers.add(answer);
        answer.setSession(this);
    }

    public void incrementCorrectAnswers() {
        this.correctAnswers++;
    }

    public void moveToNextQuiz() {
        this.currentQuizSequence++;
    }

    public void complete(Double finalScore) {
        this.isCompleted = true;
        this.finalScore = finalScore;
        this.completedAt = LocalDateTime.now();
    }
}
