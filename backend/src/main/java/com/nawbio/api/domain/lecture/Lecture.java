package com.nawbio.api.domain.lecture;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lectures")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", nullable = false, length = 50)
    private String category; // music, movie, talk

    @Column(name = "level", nullable = false)
    private Integer level; // 1=Beginner, 2=Intermediate, 3=Advanced

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "views")
    @Builder.Default
    private Integer views = 0;

    @Column(name = "completion_count")
    @Builder.Default
    private Integer completionCount = 0;

    @Column(name = "artist", length = 100)
    private String artist;

    @Column(name = "is_published")
    @Builder.Default
    private Boolean isPublished = false;

    @Column(name = "processing_status", length = 50)
    @Builder.Default
    private String processingStatus = "completed"; // processing, completed, failed

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Quiz> quizzes = new ArrayList<>();

    // 편의 메서드
    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
        quiz.setLecture(this);
    }

    public void removeQuiz(Quiz quiz) {
        quizzes.remove(quiz);
        quiz.setLecture(null);
    }

    public void incrementViews() {
        this.views++;
    }

    public void incrementCompletionCount() {
        this.completionCount++;
    }

    public void publish() {
        this.isPublished = true;
    }

    public void unpublish() {
        this.isPublished = false;
    }

    public void markAsProcessing() {
        this.processingStatus = "processing";
    }

    public void markAsCompleted() {
        this.processingStatus = "completed";
    }

    public void markAsFailed() {
        this.processingStatus = "failed";
    }
}
