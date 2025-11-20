package com.nawbio.api.domain.news.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsCreateDto {
    @NotBlank(message = "Category is required")
    private String category;

    private String thumbnailUrl;
    private List<Map<String, Object>> contentImages;
    private Boolean isPublished = false;

    // 번역 데이터 (Map<언어코드, 번역내용>)
    private Map<String, TranslationData> translations;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TranslationData {
        @NotBlank(message = "Title is required")
        private String title;
        private String excerpt;
        private String content;
    }
}
