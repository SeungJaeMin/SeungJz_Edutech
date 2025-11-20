package com.seungjz.edutech.controller;

import com.seungjz.edutech.domain.Lecture;
import com.seungjz.edutech.dto.ComponentResponse;
import com.seungjz.edutech.dto.LectureResponse;
import com.seungjz.edutech.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;

    @GetMapping
    public ResponseEntity<List<LectureResponse>> getAllLectures(@AuthenticationPrincipal Long userId) {
        if (userId != null) {
            return ResponseEntity.ok(lectureService.getUserLecturesWithProgress(userId));
        }
        return ResponseEntity.ok(lectureService.getAllLectures());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<LectureResponse>> getLecturesByType(@PathVariable String type) {
        Lecture.LectureType lectureType = Lecture.LectureType.valueOf(type.toUpperCase());
        return ResponseEntity.ok(lectureService.getLecturesByType(lectureType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectureResponse> getLectureById(@PathVariable Long id) {
        return ResponseEntity.ok(lectureService.getLectureById(id));
    }

    @GetMapping("/{id}/components")
    public ResponseEntity<List<ComponentResponse>> getLectureComponents(@PathVariable Long id) {
        return ResponseEntity.ok(lectureService.getLectureComponents(id));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Void> startLecture(
            @PathVariable Long id,
            @AuthenticationPrincipal Long userId
    ) {
        lectureService.startLecture(userId, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{lectureId}/progress")
    public ResponseEntity<Void> updateProgress(
            @PathVariable Long lectureId,
            @RequestParam Long componentId,
            @RequestParam Integer percentage,
            @AuthenticationPrincipal Long userId
    ) {
        lectureService.updateProgress(userId, lectureId, componentId, percentage);
        return ResponseEntity.ok().build();
    }
}
