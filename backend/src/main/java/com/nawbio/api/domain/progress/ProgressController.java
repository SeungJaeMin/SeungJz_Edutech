package com.nawbio.api.domain.progress;

import com.nawbio.api.common.dto.ApiResponse;
import com.nawbio.api.domain.progress.dto.LectureProgressResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Progress", description = "Learning Progress APIs")
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/lectures")
    @Operation(summary = "Get completed lectures", description = "Get list of completed lectures with scores")
    public ResponseEntity<ApiResponse<LectureProgressResponse>> getCompletedLectures(
            @RequestParam(required = false) Integer level,
            @RequestParam(defaultValue = "1") Long userId) { // TODO: Get from authentication

        LectureProgressResponse response = progressService.getCompletedLectures(userId, level);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all progress", description = "Get overall learning progress statistics")
    public ResponseEntity<ApiResponse<LectureProgressResponse>> getAllProgress(
            @RequestParam(defaultValue = "1") Long userId) { // TODO: Get from authentication

        LectureProgressResponse response = progressService.getAllProgress(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
