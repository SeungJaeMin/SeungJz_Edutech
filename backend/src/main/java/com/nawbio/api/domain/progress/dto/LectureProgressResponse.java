package com.nawbio.api.domain.progress.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureProgressResponse {
    private List<CompletedLectureDto> completedLectures;
    private Integer totalCompleted;
    private Double averageScore;
}
