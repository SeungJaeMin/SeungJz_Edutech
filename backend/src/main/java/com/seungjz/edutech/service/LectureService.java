package com.seungjz.edutech.service;

import com.seungjz.edutech.domain.Lecture;
import com.seungjz.edutech.domain.UserProgress;
import com.seungjz.edutech.dto.ComponentResponse;
import com.seungjz.edutech.dto.LectureResponse;
import com.seungjz.edutech.repository.ComponentRepository;
import com.seungjz.edutech.repository.LectureRepository;
import com.seungjz.edutech.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {
    private final LectureRepository lectureRepository;
    private final ComponentRepository componentRepository;
    private final UserProgressRepository userProgressRepository;

    public List<LectureResponse> getAllLectures() {
        return lectureRepository.findAll().stream()
                .map(LectureResponse::from)
                .collect(Collectors.toList());
    }

    public List<LectureResponse> getLecturesByType(Lecture.LectureType type) {
        return lectureRepository.findByTypeOrderByLevelAsc(type).stream()
                .map(LectureResponse::from)
                .collect(Collectors.toList());
    }

    public List<LectureResponse> getUserLecturesWithProgress(Long userId) {
        List<Lecture> lectures = lectureRepository.findAll();
        return lectures.stream()
                .map(lecture -> {
                    UserProgress progress = userProgressRepository
                            .findByUserIdAndLectureId(userId, lecture.getId())
                            .orElse(null);

                    if (progress != null) {
                        return LectureResponse.fromWithProgress(
                                lecture,
                                progress.getCompletionPercentage(),
                                progress.getStatus().name()
                        );
                    }
                    return LectureResponse.fromWithProgress(lecture, 0, "NOT_STARTED");
                })
                .collect(Collectors.toList());
    }

    public LectureResponse getLectureById(Long id) {
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));
        return LectureResponse.from(lecture);
    }

    public List<ComponentResponse> getLectureComponents(Long lectureId) {
        return componentRepository.findByLectureIdOrderByDisplayOrderAsc(lectureId).stream()
                .map(ComponentResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void startLecture(Long userId, Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        userProgressRepository.findByUserIdAndLectureId(userId, lectureId)
                .orElseGet(() -> {
                    UserProgress progress = new UserProgress();
                    progress.setUser(new com.seungjz.edutech.domain.User());
                    progress.getUser().setId(userId);
                    progress.setLecture(lecture);
                    progress.setStatus(UserProgress.ProgressStatus.IN_PROGRESS);
                    return userProgressRepository.save(progress);
                });
    }

    @Transactional
    public void updateProgress(Long userId, Long lectureId, Long componentId, Integer percentage) {
        UserProgress progress = userProgressRepository.findByUserIdAndLectureId(userId, lectureId)
                .orElseThrow(() -> new RuntimeException("Progress not found"));

        progress.setLastComponentId(componentId);
        progress.setCompletionPercentage(percentage);

        if (percentage >= 100) {
            progress.setStatus(UserProgress.ProgressStatus.COMPLETED);
            progress.setCompletedAt(java.time.LocalDateTime.now());
        }

        userProgressRepository.save(progress);
    }
}
