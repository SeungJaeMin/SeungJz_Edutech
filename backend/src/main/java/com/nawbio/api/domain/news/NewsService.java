package com.nawbio.api.domain.news;

import com.nawbio.api.common.exception.ResourceNotFoundException;
import com.nawbio.api.domain.news.dto.NewsCreateDto;
import com.nawbio.api.domain.news.dto.NewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

    private final NewsRepository newsRepository;

    public List<NewsDto> getAllNews(String category, Boolean published, String lang) {
        List<News> newsList;
        String languageCode = lang != null ? lang : "ko";

        if (published != null && published) {
            if (category != null && !category.isEmpty()) {
                newsList = newsRepository.findByCategoryAndIsPublishedTrueOrderByPublishedAtDesc(category);
            } else {
                newsList = newsRepository.findByIsPublishedTrueOrderByPublishedAtDesc();
            }
        } else {
            newsList = newsRepository.findAllByOrderByCreatedAtDesc();
        }

        return newsList.stream()
                .map(news -> NewsDto.from(news, languageCode))
                .collect(Collectors.toList());
    }

    @Transactional
    public NewsDto getNewsById(Long id, String lang) {
        String languageCode = lang != null ? lang : "ko";
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + id));

        // 조회수 증가
        news.incrementViews();
        newsRepository.save(news);

        return NewsDto.from(news, languageCode);
    }

    @Transactional
    public NewsDto createNews(NewsCreateDto dto) {
        News news = News.builder()
                .category(dto.getCategory())
                .thumbnailUrl(dto.getThumbnailUrl())
                .contentImages(dto.getContentImages())
                .isPublished(dto.getIsPublished() != null ? dto.getIsPublished() : false)
                .views(0)
                .build();

        // 번역 데이터 추가
        if (dto.getTranslations() != null) {
            for (Map.Entry<String, NewsCreateDto.TranslationData> entry : dto.getTranslations().entrySet()) {
                String langCode = entry.getKey();
                NewsCreateDto.TranslationData transData = entry.getValue();

                NewsTranslation translation = NewsTranslation.builder()
                        .languageCode(langCode)
                        .title(transData.getTitle())
                        .excerpt(transData.getExcerpt())
                        .content(transData.getContent())
                        .build();

                news.addTranslation(translation);
            }
        }

        if (news.getIsPublished()) {
            news.publish();
        }

        News saved = newsRepository.save(news);
        return NewsDto.from(saved, "ko");
    }

    @Transactional
    public NewsDto updateNews(Long id, NewsCreateDto dto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + id));

        news.setCategory(dto.getCategory());
        news.setThumbnailUrl(dto.getThumbnailUrl());
        news.setContentImages(dto.getContentImages());

        // 번역 업데이트 (기존 번역 찾아서 수정, 없으면 추가)
        if (dto.getTranslations() != null) {
            for (Map.Entry<String, NewsCreateDto.TranslationData> entry : dto.getTranslations().entrySet()) {
                String langCode = entry.getKey();
                NewsCreateDto.TranslationData transData = entry.getValue();

                // 기존 번역 찾기
                NewsTranslation existingTranslation = news.getTranslation(langCode);

                if (existingTranslation != null) {
                    // 기존 번역 업데이트
                    existingTranslation.setTitle(transData.getTitle());
                    existingTranslation.setExcerpt(transData.getExcerpt());
                    existingTranslation.setContent(transData.getContent());
                } else {
                    // 새 번역 추가
                    NewsTranslation translation = NewsTranslation.builder()
                            .languageCode(langCode)
                            .title(transData.getTitle())
                            .excerpt(transData.getExcerpt())
                            .content(transData.getContent())
                            .build();
                    news.addTranslation(translation);
                }
            }
        }

        if (dto.getIsPublished() != null && dto.getIsPublished() && !news.getIsPublished()) {
            news.publish();
        } else if (dto.getIsPublished() != null && !dto.getIsPublished()) {
            news.unpublish();
        }

        News updated = newsRepository.save(news);
        return NewsDto.from(updated, "ko");
    }

    @Transactional
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + id));
        newsRepository.delete(news);
    }

    @Transactional
    public NewsDto togglePublish(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + id));

        if (news.getIsPublished()) {
            news.unpublish();
        } else {
            news.publish();
        }

        News updated = newsRepository.save(news);
        return NewsDto.from(updated, "ko");
    }
}
