package com.seungjz.edutech.domain.lecture.dto;

import com.seungjz.edutech.domain.lecture.entity.Component;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComponentRequest {

    @NotNull(message = "Component type is required")
    private Component.ComponentType type;

    @NotNull(message = "Start time is required")
    private Integer startTime;

    @NotBlank(message = "Question is required")
    private String question;

    @NotBlank(message = "Expected answer is required")
    private String expectedAnswer;

    private String keywords;

    public Component toEntity() {
        return Component.builder()
                .type(type)
                .startTime(startTime)
                .question(question)
                .expectedAnswer(expectedAnswer)
                .keywords(keywords)
                .build();
    }
}
