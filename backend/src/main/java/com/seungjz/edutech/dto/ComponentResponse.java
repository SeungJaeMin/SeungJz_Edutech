package com.seungjz.edutech.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.seungjz.edutech.domain.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComponentResponse {
    private Long id;
    private Long lectureId;
    private String componentType;
    private Integer displayOrder;
    private JsonNode content;

    public static ComponentResponse from(Component component) {
        return new ComponentResponse(
                component.getId(),
                component.getLecture().getId(),
                component.getComponentType().name(),
                component.getDisplayOrder(),
                component.getContent()
        );
    }
}
