package com.nawbio.api.domain.news.dto;

import com.nawbio.api.domain.news.News;
import com.nawbio.api.domain.news.NewsTranslation;
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
public class NewsDto {
    private Long id;
    private String category;
    private String thumbnailUrl;
    private List<Map<String, Object>> contentImages;
    private Integer views;
    private Boolean isPublished;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 번역된 필드 (요청된 언어)
    private String title;
    private String excerpt;
    private String content;

    public static NewsDto from(News news, String languageCode) {
        NewsTranslation translation = news.getTranslation(languageCode);

        NewsDtoBuilder builder = NewsDto.builder()
                .id(news.getId())
                .category(news.getCategory())
                .thumbnailUrl(news.getThumbnailUrl())
                .contentImages(news.getContentImages())
                .views(news.getViews())
                .isPublished(news.getIsPublished())
                .publishedAt(news.getPublishedAt())
                .createdAt(news.getCreatedAt())
                .updatedAt(news.getUpdatedAt());

        if (translation != null) {
            builder.title(translation.getTitle())
                   .excerpt(translation.getExcerpt())
                   .content(translation.getContent());
        }

        return builder.build();
    }
}
