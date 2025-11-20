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

    // 편의 메서드
    public void addComponent(Component component) {
        components.add(component);
        component.setLecture(this);
    }

    public void removeComponent(Component component) {
        components.remove(component);
        component.setLecture(null);
    }

    public void clearComponents() {
        components.forEach(component -> component.setLecture(null));
        components.clear();
    }

    // 비즈니스 메서드
    public void updateBasicInfo(String title, String artist, Integer level, LectureType type) {
        this.title = title;
        this.artist = artist;
        this.level = level;
        this.type = type;
    }

    public void updateVideoInfo(String videoUrl, String thumbnailUrl, Integer duration) {
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
    }
}
