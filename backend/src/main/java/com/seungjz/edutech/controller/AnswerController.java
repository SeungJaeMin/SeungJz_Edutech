package com.seungjz.edutech.controller;

import com.seungjz.edutech.domain.AnswerHistory;
import com.seungjz.edutech.dto.AnswerSubmitRequest;
import com.seungjz.edutech.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<AnswerHistory> submitAnswer(
            @RequestBody AnswerSubmitRequest request,
            @AuthenticationPrincipal Long userId
    ) {
        AnswerHistory result = answerService.submitAnswer(userId, request);
        return ResponseEntity.ok(result);
    }
}
