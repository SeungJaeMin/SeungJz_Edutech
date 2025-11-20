package com.seungjz.edutech.dto;

import com.seungjz.edutech.domain.Lecture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureResponse {
    private Long id;
    private String title;
    private String description;
    private String type;
    private Integer level;
    private String thumbnailUrl;
    private String videoUrl;
    private LocalDateTime createdAt;
    private Integer completionPercentage;
    private String progressStatus;

    public static LectureResponse from(Lecture lecture) {
        return new LectureResponse(
                lecture.getId(),
                lecture.getTitle(),
                lecture.getDescription(),
                lecture.getType().name(),
                lecture.getLevel(),
                lecture.getThumbnailUrl(),
                lecture.getVideoUrl(),
                lecture.getCreatedAt(),
                null,
                null
        );
    }

    public static LectureResponse fromWithProgress(Lecture lecture, Integer completionPercentage, String progressStatus) {
        return new LectureResponse(
                lecture.getId(),
                lecture.getTitle(),
                lecture.getDescription(),
                lecture.getType().name(),
                lecture.getLevel(),
                lecture.getThumbnailUrl(),
                lecture.getVideoUrl(),
                lecture.getCreatedAt(),
                completionPercentage,
                progressStatus
        );
    }
}
