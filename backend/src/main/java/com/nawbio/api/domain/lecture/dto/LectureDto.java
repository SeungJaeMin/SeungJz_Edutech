package com.nawbio.api.domain.lecture.dto;

import com.nawbio.api.domain.lecture.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Integer level;
    private String videoUrl;
    private String thumbnailUrl;
    private Integer durationSeconds;
    private Integer views;
    private Integer completionCount;
    private String artist;
    private LocalDateTime createdAt;
    private List<QuizDto> quizzes;

    public static LectureDto fromEntity(Lecture lecture) {
        return LectureDto.builder()
                .id(lecture.getId())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .category(lecture.getCategory())
                .level(lecture.getLevel())
                .videoUrl(lecture.getVideoUrl())
                .thumbnailUrl(lecture.getThumbnailUrl())
                .durationSeconds(lecture.getDurationSeconds())
                .views(lecture.getViews())
                .completionCount(lecture.getCompletionCount())
                .artist(lecture.getArtist())
                .createdAt(lecture.getCreatedAt())
                .quizzes(lecture.getQuizzes() != null
                    ? lecture.getQuizzes().stream()
                        .map(QuizDto::fromEntity)
                        .collect(Collectors.toList())
                    : null)
                .build();
    }

    public static LectureDto fromEntityWithoutQuizzes(Lecture lecture) {
        return LectureDto.builder()
                .id(lecture.getId())
                .title(lecture.getTitle())
                .description(lecture.getDescription())
                .category(lecture.getCategory())
                .level(lecture.getLevel())
                .videoUrl(lecture.getVideoUrl())
                .thumbnailUrl(lecture.getThumbnailUrl())
                .durationSeconds(lecture.getDurationSeconds())
                .views(lecture.getViews())
                .completionCount(lecture.getCompletionCount())
                .artist(lecture.getArtist())
                .createdAt(lecture.getCreatedAt())
                .build();
    }
}
