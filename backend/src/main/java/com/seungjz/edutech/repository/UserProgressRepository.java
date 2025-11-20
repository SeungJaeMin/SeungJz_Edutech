package com.seungjz.edutech.repository;

import com.seungjz.edutech.domain.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserIdAndLectureId(Long userId, Long lectureId);

    List<UserProgress> findByUserIdOrderByStartedAtDesc(Long userId);

    List<UserProgress> findByUserIdAndStatus(Long userId, UserProgress.ProgressStatus status);
}
