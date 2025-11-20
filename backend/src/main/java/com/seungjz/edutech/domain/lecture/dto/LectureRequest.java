package com.seungjz.edutech.domain.lecture.dto;

import com.seungjz.edutech.domain.lecture.entity.Lecture;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String artist;

    @NotNull(message = "Level is required")
    private Integer level;

    @NotNull(message = "Type is required")
    private Lecture.LectureType type;

    @NotNull(message = "Duration is required")
    private Integer duration;

    @Builder.Default
    private List<ComponentRequest> components = new ArrayList<>();

    public Lecture toEntity() {
        return Lecture.builder()
                .title(title)
                .artist(artist)
                .level(level)
                .type(type)
                .duration(duration)
                .components(new ArrayList<>())
                .build();
    }
}
