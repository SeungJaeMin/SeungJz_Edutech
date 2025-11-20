package com.seungjz.edutech.domain.learning.entity;

import com.seungjz.edutech.common.entity.BaseEntity;
import com.seungjz.edutech.domain.auth.entity.User;
import com.seungjz.edutech.domain.lecture.entity.Lecture;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_progress")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserProgress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(nullable = false)
    private Integer lastPosition; // seconds

    @Column(nullable = false)
    private Integer completionRate; // 0-100

    @Column
    private Integer totalScore;

    @Column(nullable = false)
    private Boolean completed;

    public void updateProgress(Integer lastPosition, Integer completionRate) {
        this.lastPosition = lastPosition;
        this.completionRate = completionRate;
    }

    public void complete(Integer totalScore) {
        this.completed = true;
        this.completionRate = 100;
        this.totalScore = totalScore;
    }
}
