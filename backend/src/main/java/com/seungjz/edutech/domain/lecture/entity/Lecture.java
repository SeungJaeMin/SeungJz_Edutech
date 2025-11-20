package com.seungjz.edutech.domain.lecture.entity;

import com.seungjz.edutech.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lectures")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 100)
    private String artist;

    @Column(nullable = false)
    private Integer level; // 1: Music, 2: Drama, 3: Talk

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LectureType type;

    @Column(length = 500)
    private String videoUrl;

    @Column(length = 500)
    private String thumbnailUrl;

    @Column
    private Integer duration; // seconds

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Component> components = new ArrayList<>();

    public enum LectureType {
        MUSIC, DRAMA, TALK
    }
}
