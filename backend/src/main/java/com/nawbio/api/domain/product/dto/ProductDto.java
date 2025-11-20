package com.nawbio.api.domain.product.dto;

import com.nawbio.api.domain.product.Product;
import com.nawbio.api.domain.product.ProductTranslation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String category;
    private String imageUrl;
    private String bgColor;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 번역된 필드 (요청된 언어)
    private String name;
    private String subtitle;
    private String excerpt;
    private String description;
    private String usage;
    private List<String> features;
    private List<String> benefits;
    private Map<String, String> specifications;

    public static ProductDto from(Product product, String languageCode) {
        ProductTranslation translation = product.getTranslation(languageCode);

        ProductDtoBuilder builder = ProductDto.builder()
                .id(product.getId())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .bgColor(product.getBgColor())
                .displayOrder(product.getDisplayOrder())
                .isActive(product.getIsActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt());

        if (translation != null) {
            builder.name(translation.getName())
                   .subtitle(translation.getSubtitle())
                   .excerpt(translation.getExcerpt())
                   .description(translation.getDescription())
                   .usage(translation.getUsage())
                   .features(translation.getFeatures())
                   .benefits(translation.getBenefits())
                   .specifications(translation.getSpecifications());
        }

        return builder.build();
    }
}
