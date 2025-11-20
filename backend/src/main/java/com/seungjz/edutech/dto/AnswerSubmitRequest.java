package com.seungjz.edutech.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class AnswerSubmitRequest {
    private Long componentId;
    private JsonNode userAnswer;
}
