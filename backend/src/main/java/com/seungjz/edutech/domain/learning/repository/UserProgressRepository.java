package com.seungjz.edutech.domain.learning.repository;

import com.seungjz.edutech.domain.learning.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    Optional<UserProgress> findByUserIdAndLectureId(Long userId, Long lectureId);

    List<UserProgress> findByUserIdOrderByUpdatedAtDesc(Long userId);

    List<UserProgress> findByUserIdAndCompletedTrue(Long userId);
}
