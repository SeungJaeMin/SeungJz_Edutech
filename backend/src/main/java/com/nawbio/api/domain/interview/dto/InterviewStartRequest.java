package com.nawbio.api.domain.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewStartRequest {
    private String prompt;
    private Integer durationSeconds;
}
