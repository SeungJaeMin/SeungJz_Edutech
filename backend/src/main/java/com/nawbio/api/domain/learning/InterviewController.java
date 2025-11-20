package com.nawbio.api.domain.learning;

import com.nawbio.api.common.dto.ApiResponse;
import com.nawbio.api.domain.learning.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Interview", description = "3rd Stage Interview API - Real-time emotion analysis")
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/start")
    @Operation(summary = "Start interview session", description = "Starts a new interview session and enables webcam")
    public ResponseEntity<ApiResponse<SessionResponse>> startSession(@RequestBody SessionStartRequest request) {
        log.info("Starting interview session for user: {}, stage: {}", request.getUserId(), request.getStage());

        SessionResponse response = interviewService.startSession(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze emotion", description = "Analyzes emotion from webcam frame")
    public ResponseEntity<ApiResponse<EmotionAnalysisResponse>> analyzeEmotion(
            @RequestBody EmotionAnalysisRequest request) {
        log.info("Analyzing emotion for session: {}", request.getSessionId());

        EmotionAnalysisResponse response = interviewService.analyzeEmotion(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{sessionId}/end")
    @Operation(summary = "End interview session", description = "Ends the interview session and disables webcam")
    public ResponseEntity<ApiResponse<SessionResponse>> endSession(@PathVariable Long sessionId) {
        log.info("Ending interview session: {}", sessionId);

        SessionResponse response = interviewService.endSession(sessionId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{sessionId}")
    @Operation(summary = "Get session details", description = "Retrieves interview session details")
    public ResponseEntity<ApiResponse<SessionResponse>> getSession(@PathVariable Long sessionId) {
        log.info("Getting session details: {}", sessionId);

        SessionResponse response = interviewService.getSession(sessionId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user sessions", description = "Retrieves all interview sessions for a user")
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getUserSessions(@PathVariable String userId) {
        log.info("Getting sessions for user: {}", userId);

        List<SessionResponse> sessions = interviewService.getUserSessions(userId);

        return ResponseEntity.ok(ApiResponse.success(sessions));
    }
}
