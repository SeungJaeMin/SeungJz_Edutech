package com.nawbio.api.domain.learning.dto;

import lombok.Data;

@Data
public class SessionStartRequest {
    private String userId;
    private Integer stage; // 3 for 3rd stage
}
