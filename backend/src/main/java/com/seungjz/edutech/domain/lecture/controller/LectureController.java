package com.seungjz.edutech.domain.lecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seungjz.edutech.domain.lecture.dto.LectureRequest;
import com.seungjz.edutech.domain.lecture.dto.LectureResponse;
import com.seungjz.edutech.domain.lecture.entity.Lecture;
import com.seungjz.edutech.domain.lecture.service.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
@Slf4j
public class LectureController {

    private final LectureService lectureService;
    private final ObjectMapper objectMapper;

    /**
     * 새 강의 생성 (파일 업로드 포함)
     */
    @PostMapping
    public ResponseEntity<LectureResponse> createLecture(
            @RequestPart("lecture") String lectureJson,
            @RequestPart("videoFile") MultipartFile videoFile,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile
    ) throws IOException {
        log.info("Creating new lecture with video file: {}", videoFile.getOriginalFilename());

        // JSON을 LectureRequest로 변환
        LectureRequest request = objectMapper.readValue(lectureJson, LectureRequest.class);

        LectureResponse response = lectureService.createLecture(request, videoFile, thumbnailFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 모든 강의 조회
     */
    @GetMapping
    public ResponseEntity<List<LectureResponse>> getAllLectures() {
        log.info("Fetching all lectures");
        List<LectureResponse> lectures = lectureService.getAllLectures();
        return ResponseEntity.ok(lectures);
    }

    /**
     * 특정 강의 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<LectureResponse> getLecture(@PathVariable Long id) {
        log.info("Fetching lecture: id={}", id);
        LectureResponse response = lectureService.getLecture(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 타입별 강의 조회
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<LectureResponse>> getLecturesByType(@PathVariable Lecture.LectureType type) {
        log.info("Fetching lectures by type: {}", type);
        List<LectureResponse> lectures = lectureService.getLecturesByType(type);
        return ResponseEntity.ok(lectures);
    }

    /**
     * 레벨별 강의 조회
     */
    @GetMapping("/level/{level}")
    public ResponseEntity<List<LectureResponse>> getLecturesByLevel(@PathVariable Integer level) {
        log.info("Fetching lectures by level: {}", level);
        List<LectureResponse> lectures = lectureService.getLecturesByLevel(level);
        return ResponseEntity.ok(lectures);
    }

    /**
     * 강의 수정 (파일 업로드 포함)
     */
    @PutMapping("/{id}")
    public ResponseEntity<LectureResponse> updateLecture(
            @PathVariable Long id,
            @RequestPart("lecture") String lectureJson,
            @RequestPart(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile
    ) throws IOException {
        log.info("Updating lecture: id={}", id);

        // JSON을 LectureRequest로 변환
        LectureRequest request = objectMapper.readValue(lectureJson, LectureRequest.class);

        LectureResponse response = lectureService.updateLecture(id, request, videoFile, thumbnailFile);
        return ResponseEntity.ok(response);
    }

    /**
     * 강의 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLecture(@PathVariable Long id) throws IOException {
        log.info("Deleting lecture: id={}", id);
        lectureService.deleteLecture(id);
        return ResponseEntity.noContent().build();
    }
}
