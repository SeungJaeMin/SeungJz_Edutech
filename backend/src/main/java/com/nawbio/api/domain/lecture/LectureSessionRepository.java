package com.nawbio.api.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureSessionRepository extends JpaRepository<LectureSession, Long> {

    // Session ID로 조회
    Optional<LectureSession> findBySessionId(String sessionId);

    // User ID로 세션 조회 (최신순)
    List<LectureSession> findByUserIdOrderByCreatedAtDesc(Long userId);

    // User + Lecture로 완료된 세션 조회
    List<LectureSession> findByUserIdAndLectureIdAndIsCompletedTrue(Long userId, Long lectureId);

    // User ID로 완료된 세션 개수
    Long countByUserIdAndIsCompletedTrue(Long userId);
}
