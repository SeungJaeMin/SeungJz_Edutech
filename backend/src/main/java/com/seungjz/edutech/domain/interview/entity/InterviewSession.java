package com.seungjz.edutech.domain.interview.entity;

import com.seungjz.edutech.common.entity.BaseEntity;
import com.seungjz.edutech.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InterviewSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column
    private Integer level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SessionStatus status = SessionStatus.ACTIVE;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime endedAt;

    @Column
    private Integer duration; // seconds

    @Column(columnDefinition = "TEXT")
    private String feedbackSummary;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InterviewQAPair> qaPairs = new ArrayList<>();

    public enum SessionStatus {
        ACTIVE, COMPLETED, CANCELLED
    }

    public void end(String feedbackSummary) {
        this.status = SessionStatus.COMPLETED;
        this.endedAt = LocalDateTime.now();
        this.duration = (int) java.time.Duration.between(startedAt, endedAt).getSeconds();
        this.feedbackSummary = feedbackSummary;
    }
}
