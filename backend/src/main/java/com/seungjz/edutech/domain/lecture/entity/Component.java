package com.seungjz.edutech.domain.lecture.entity;

import com.seungjz.edutech.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "components")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Component extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ComponentType type;

    @Column(nullable = false)
    private Integer startTime; // seconds

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String expectedAnswer;

    @Column(length = 500)
    private String keywords; // comma-separated

    public enum ComponentType {
        QUESTION, INFO
    }
}
