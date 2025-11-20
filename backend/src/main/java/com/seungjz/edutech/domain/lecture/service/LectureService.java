package com.seungjz.edutech.domain.lecture.service;

import com.seungjz.edutech.common.service.FileStorageService;
import com.seungjz.edutech.domain.lecture.dto.ComponentRequest;
import com.seungjz.edutech.domain.lecture.dto.LectureRequest;
import com.seungjz.edutech.domain.lecture.dto.LectureResponse;
import com.seungjz.edutech.domain.lecture.entity.Component;
import com.seungjz.edutech.domain.lecture.entity.Lecture;
import com.seungjz.edutech.domain.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LectureService {

    private final LectureRepository lectureRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public LectureResponse createLecture(
            LectureRequest request,
            MultipartFile videoFile,
            MultipartFile thumbnailFile
    ) throws IOException {
        // 비디오 파일 저장
        String videoUrl = fileStorageService.storeVideoFile(videoFile);

        // 썸네일 파일 저장 (옵션)
        String thumbnailUrl = null;
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            thumbnailUrl = fileStorageService.storeThumbnailFile(thumbnailFile);
        }

        // Lecture 엔티티 생성
        Lecture lecture = request.toEntity();
        lecture.updateVideoInfo(videoUrl, thumbnailUrl, request.getDuration());

        // Component 추가 (양방향 관계 설정)
        for (ComponentRequest componentReq : request.getComponents()) {
            Component component = componentReq.toEntity();
            lecture.addComponent(component);
        }

        // 저장
        Lecture savedLecture = lectureRepository.save(lecture);
        log.info("Created new lecture: id={}, title={}", savedLecture.getId(), savedLecture.getTitle());

        return LectureResponse.from(savedLecture);
    }

    public LectureResponse getLecture(Long id) {
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found: " + id));
        return LectureResponse.from(lecture);
    }

    public List<LectureResponse> getAllLectures() {
        return lectureRepository.findAll().stream()
                .map(LectureResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public LectureResponse updateLecture(
            Long id,
            LectureRequest request,
            MultipartFile videoFile,
            MultipartFile thumbnailFile
    ) throws IOException {
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found: " + id));

        // 기본 정보 업데이트
        lecture.updateBasicInfo(
                request.getTitle(),
                request.getArtist(),
                request.getLevel(),
                request.getType()
        );

        // 비디오 파일이 새로 업로드된 경우
        if (videoFile != null && !videoFile.isEmpty()) {
            // 기존 비디오 파일 삭제
            if (lecture.getVideoUrl() != null) {
                fileStorageService.deleteFile(lecture.getVideoUrl());
            }
            String videoUrl = fileStorageService.storeVideoFile(videoFile);
            lecture.updateVideoInfo(videoUrl, lecture.getThumbnailUrl(), request.getDuration());
        }

        // 썸네일 파일이 새로 업로드된 경우
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            // 기존 썸네일 파일 삭제
            if (lecture.getThumbnailUrl() != null) {
                fileStorageService.deleteFile(lecture.getThumbnailUrl());
            }
            String thumbnailUrl = fileStorageService.storeThumbnailFile(thumbnailFile);
            lecture.updateVideoInfo(lecture.getVideoUrl(), thumbnailUrl, lecture.getDuration());
        }

        // Component 업데이트 (기존 것 제거 후 새로 추가)
        lecture.clearComponents();
        for (ComponentRequest componentReq : request.getComponents()) {
            Component component = componentReq.toEntity();
            lecture.addComponent(component);
        }

        log.info("Updated lecture: id={}, title={}", lecture.getId(), lecture.getTitle());
        return LectureResponse.from(lecture);
    }

    @Transactional
    public void deleteLecture(Long id) throws IOException {
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lecture not found: " + id));

        // 파일 삭제
        if (lecture.getVideoUrl() != null) {
            fileStorageService.deleteFile(lecture.getVideoUrl());
        }
        if (lecture.getThumbnailUrl() != null) {
            fileStorageService.deleteFile(lecture.getThumbnailUrl());
        }

        // Lecture 삭제 (Component는 cascade로 자동 삭제)
        lectureRepository.delete(lecture);
        log.info("Deleted lecture: id={}", id);
    }

    public List<LectureResponse> getLecturesByType(Lecture.LectureType type) {
        return lectureRepository.findAll().stream()
                .filter(lecture -> lecture.getType() == type)
                .map(LectureResponse::from)
                .collect(Collectors.toList());
    }

    public List<LectureResponse> getLecturesByLevel(Integer level) {
        return lectureRepository.findAll().stream()
                .filter(lecture -> lecture.getLevel().equals(level))
                .map(LectureResponse::from)
                .collect(Collectors.toList());
    }
}
