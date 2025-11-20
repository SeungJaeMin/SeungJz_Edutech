package com.nawbio.api.domain.interview;

import com.nawbio.api.common.dto.ApiResponse;
import com.nawbio.api.domain.interview.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Interview", description = "3rd Stage Interview APIs")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/start")
    @Operation(summary = "Start interview session", description = "Start a new real-time interview session")
    public ResponseEntity<ApiResponse<InterviewStartResponse>> startInterview(
            @RequestBody InterviewStartRequest request,
            @RequestParam(defaultValue = "1") Long userId) { // TODO: Get from authentication

        InterviewStartResponse response = interviewService.startInterview(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{sessionId}/frame")
    @Operation(summary = "Analyze emotion from video frame", description = "Send video frame for real-time emotion analysis")
    public ResponseEntity<ApiResponse<EmotionResponse>> analyzeFrame(
            @PathVariable String sessionId,
            @RequestParam Integer timelineSec) {
            // TODO: Add MultipartFile frame parameter for actual implementation

        EmotionResponse response = interviewService.analyzeEmotion(sessionId, timelineSec);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{sessionId}/answer")
    @Operation(summary = "Submit answer and get next question", description = "Submit voice answer and receive LLM-generated next question")
    public ResponseEntity<ApiResponse<AnswerSubmitResponse>> submitAnswer(
            @PathVariable String sessionId,
            @RequestParam Long questionId,
            @RequestParam String transcription,
            @RequestParam Integer timelineSec) {
            // TODO: Add MultipartFile audio parameter for actual STT

        AnswerSubmitResponse response = interviewService.submitAnswer(sessionId, questionId, transcription, timelineSec);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{sessionId}/complete")
    @Operation(summary = "Complete interview session", description = "Mark interview as completed")
    public ResponseEntity<ApiResponse<InterviewCompleteResponse>> completeInterview(@PathVariable String sessionId) {
        InterviewCompleteResponse response = interviewService.completeInterview(sessionId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
