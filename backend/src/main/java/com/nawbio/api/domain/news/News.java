package com.nawbio.api.domain.news;

import com.nawbio.api.domain.admin.Admin;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "news")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content_images", columnDefinition = "jsonb")
    private List<Map<String, Object>> contentImages;

    @Column(name = "views")
    @Builder.Default
    private Integer views = 0;

    @Column(name = "is_published")
    @Builder.Default
    private Boolean isPublished = false;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Admin author;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<NewsTranslation> translations = new ArrayList<>();

    // 편의 메서드
    public NewsTranslation getTranslation(String languageCode) {
        // 요청된 언어의 번역을 먼저 찾음
        NewsTranslation translation = translations.stream()
                .filter(t -> t.getLanguageCode().equals(languageCode))
                .findFirst()
                .orElse(null);

        // 번역이 없거나 필수 필드(title)가 비어있으면 한국어로 fallback
        if (translation == null || translation.getTitle() == null || translation.getTitle().isEmpty()) {
            translation = translations.stream()
                    .filter(t -> t.getLanguageCode().equals("ko"))
                    .findFirst()
                    .orElse(null);
        }

        return translation;
    }

    public void addTranslation(NewsTranslation translation) {
        translations.add(translation);
        translation.setNews(this);
    }

    public void removeTranslation(NewsTranslation translation) {
        translations.remove(translation);
        translation.setNews(null);
    }

    public void incrementViews() {
        this.views++;
    }

    public void publish() {
        this.isPublished = true;
        if (this.publishedAt == null) {
            this.publishedAt = LocalDateTime.now();
        }
    }

    public void unpublish() {
        this.isPublished = false;
    }
}
