package com.nawbio.api.domain.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDto {
    @NotBlank(message = "Category is required")
    private String category;

    private String imageUrl;
    private String bgColor;
    private Integer displayOrder;

    // 번역 데이터 (Map<언어코드, 번역내용>)
    private Map<String, TranslationData> translations;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TranslationData {
        @NotBlank(message = "Name is required")
        private String name;
        private String subtitle;
        private String description;
        private String usage;
        private List<String> features;
        private List<String> benefits;
        private Map<String, String> specifications;
    }
}
