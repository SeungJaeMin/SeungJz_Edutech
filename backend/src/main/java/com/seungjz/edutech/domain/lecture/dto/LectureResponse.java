package com.seungjz.edutech.domain.lecture.dto;

import com.seungjz.edutech.domain.lecture.entity.Component;
import com.seungjz.edutech.domain.lecture.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureResponse {

    private Long id;
    private String title;
    private String artist;
    private Integer level;
    private Lecture.LectureType type;
    private String videoUrl;
    private String thumbnailUrl;
    private Integer duration;
    private List<ComponentResponse> components;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LectureResponse from(Lecture lecture) {
        return LectureResponse.builder()
                .id(lecture.getId())
                .title(lecture.getTitle())
                .artist(lecture.getArtist())
                .level(lecture.getLevel())
                .type(lecture.getType())
                .videoUrl(lecture.getVideoUrl())
                .thumbnailUrl(lecture.getThumbnailUrl())
                .duration(lecture.getDuration())
                .components(lecture.getComponents().stream()
                        .map(ComponentResponse::from)
                        .collect(Collectors.toList()))
                .createdAt(lecture.getCreatedAt())
                .updatedAt(lecture.getUpdatedAt())
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ComponentResponse {
        private Long id;
        private Component.ComponentType type;
        private Integer startTime;
        private String question;
        private String expectedAnswer;
        private String keywords;

        public static ComponentResponse from(Component component) {
            return ComponentResponse.builder()
                    .id(component.getId())
                    .type(component.getType())
                    .startTime(component.getStartTime())
                    .question(component.getQuestion())
                    .expectedAnswer(component.getExpectedAnswer())
                    .keywords(component.getKeywords())
                    .build();
        }
    }
}
