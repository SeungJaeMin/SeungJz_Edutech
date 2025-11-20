package com.nawbio.api.domain.product;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "bg_color", length = 20)
    @Builder.Default
    private String bgColor = "#6B8E23";

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductTranslation> translations = new ArrayList<>();

    // 편의 메서드
    public ProductTranslation getTranslation(String languageCode) {
        // 요청된 언어의 번역을 먼저 찾음
        ProductTranslation translation = translations.stream()
                .filter(t -> t.getLanguageCode().equals(languageCode))
                .findFirst()
                .orElse(null);

        // 번역이 없거나 필수 필드(name)가 비어있으면 한국어로 fallback
        if (translation == null || translation.getName() == null || translation.getName().isEmpty()) {
            translation = translations.stream()
                    .filter(t -> t.getLanguageCode().equals("ko"))
                    .findFirst()
                    .orElse(null);
        }

        return translation;
    }

    public void addTranslation(ProductTranslation translation) {
        translations.add(translation);
        translation.setProduct(this);
    }

    public void removeTranslation(ProductTranslation translation) {
        translations.remove(translation);
        translation.setProduct(null);
    }
}
