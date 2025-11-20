package com.nawbio.api.domain.news;

import com.nawbio.api.common.dto.ApiResponse;
import com.nawbio.api.domain.news.dto.NewsCreateDto;
import com.nawbio.api.domain.news.dto.NewsDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NewsDto>>> getAllNews(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean published,
            @RequestParam(defaultValue = "ko") String lang
    ) {
        List<NewsDto> newsList = newsService.getAllNews(category, published, lang);
        return ResponseEntity.ok(ApiResponse.success(newsList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsDto>> getNewsById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "ko") String lang
    ) {
        NewsDto news = newsService.getNewsById(id, lang);
        return ResponseEntity.ok(ApiResponse.success(news));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NewsDto>> createNews(@Valid @RequestBody NewsCreateDto dto) {
        NewsDto created = newsService.createNews(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("News created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsDto>> updateNews(
            @PathVariable Long id,
            @Valid @RequestBody NewsCreateDto dto
    ) {
        NewsDto updated = newsService.updateNews(id, dto);
        return ResponseEntity.ok(ApiResponse.success("News updated successfully", updated));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<NewsDto>> togglePublish(@PathVariable Long id) {
        NewsDto updated = newsService.togglePublish(id);
        return ResponseEntity.ok(ApiResponse.success("News publish status toggled", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok(ApiResponse.success("News deleted successfully", null));
    }
}
