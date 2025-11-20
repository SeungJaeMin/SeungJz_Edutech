package com.nawbio.api.domain.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureCompleteRequest {
    private String sessionId;
    private Integer totalQuizzes;
    private Integer correctAnswers;
}
