package com.nawbio.api.domain.lecture;

import com.nawbio.api.common.dto.ApiResponse;
import com.nawbio.api.domain.lecture.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lecture", description = "Lecture APIs")
public class LectureController {

    private final LectureService lectureService;

    @GetMapping
    @Operation(summary = "Get recommended lectures", description = "Get list of recommended lectures")
    public ResponseEntity<ApiResponse<Page<LectureDto>>> getRecommendedLectures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<LectureDto> lectures = lectureService.getRecommendedLectures(page, size);
        return ResponseEntity.ok(ApiResponse.success(lectures));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get lectures by category", description = "Get lectures filtered by category and level")
    public ResponseEntity<ApiResponse<Page<LectureDto>>> getLecturesByCategory(
            @PathVariable String category,
            @RequestParam(required = false) Integer level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<LectureDto> lectures = lectureService.getLecturesByCategory(category, level, page, size);
        return ResponseEntity.ok(ApiResponse.success(lectures));
    }

    @GetMapping("/{lectureId}")
    @Operation(summary = "Get lecture detail", description = "Get lecture detail with quizzes")
    public ResponseEntity<ApiResponse<LectureDto>> getLectureDetail(@PathVariable Long lectureId) {
        LectureDto lecture = lectureService.getLectureDetail(lectureId);
        return ResponseEntity.ok(ApiResponse.success(lecture));
    }

    @PostMapping("/{lectureId}/start")
    @Operation(summary = "Start lecture", description = "Start a new lecture session")
    public ResponseEntity<ApiResponse<LectureSessionDto>> startLecture(@PathVariable Long lectureId) {
        LectureSessionDto session = lectureService.startLecture(lectureId);
        return ResponseEntity.ok(ApiResponse.success(session));
    }

    @PostMapping("/answer")
    @Operation(summary = "Submit answer", description = "Submit voice answer for a quiz")
    public ResponseEntity<ApiResponse<AnswerSubmitResponse>> submitAnswer(
            @RequestParam String sessionId,
            @RequestParam Long quizId,
            @RequestParam String transcription) {

        // 실제로는 MultipartFile로 음성 파일을 받아서 STT 처리
        // 여기서는 테스트를 위해 transcription을 직접 받음
        AnswerSubmitResponse response = lectureService.submitAnswer(sessionId, quizId, transcription);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{lectureId}/complete")
    @Operation(summary = "Complete lecture", description = "Mark lecture as completed")
    public ResponseEntity<ApiResponse<LectureSessionDto>> completeLecture(
            @PathVariable Long lectureId,
            @RequestBody LectureCompleteRequest request) {

        LectureSessionDto session = lectureService.completeLecture(lectureId, request);
        return ResponseEntity.ok(ApiResponse.success(session));
    }
}
