package com.seungjz.edutech.domain.learning.entity;

import com.seungjz.edutech.common.entity.BaseEntity;
import com.seungjz.edutech.domain.auth.entity.User;
import com.seungjz.edutech.domain.lecture.entity.Component;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answer_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AnswerHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false)
    private Component component;

    @Column(columnDefinition = "TEXT")
    private String userAnswer;

    @Column(columnDefinition = "TEXT")
    private String transcription;

    @Column
    private Boolean isCorrect;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column
    private Integer score;

    @Column(length = 500)
    private String missingKeywords;

    @Column(length = 20)
    private String emotion;

    @Column
    private Double emotionConfidence;
}
