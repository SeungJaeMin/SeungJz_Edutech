package com.nawbio.api.domain.lecture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    // 카테고리별 강의 조회
    Page<Lecture> findByCategoryAndIsPublishedTrue(String category, Pageable pageable);

    // 레벨별 강의 조회
    Page<Lecture> findByLevelAndIsPublishedTrue(Integer level, Pageable pageable);

    // 카테고리 + 레벨별 강의 조회
    Page<Lecture> findByCategoryAndLevelAndIsPublishedTrue(String category, Integer level, Pageable pageable);

    // 퀴즈와 함께 조회 (N+1 방지)
    @Query("SELECT l FROM Lecture l LEFT JOIN FETCH l.quizzes WHERE l.id = :lectureId AND l.isPublished = true")
    Optional<Lecture> findByIdWithQuizzes(@Param("lectureId") Long lectureId);

    // 인기 강의 (조회수 기준)
    @Query("SELECT l FROM Lecture l WHERE l.isPublished = true ORDER BY l.views DESC")
    Page<Lecture> findPopularLectures(Pageable pageable);

    // 추천 강의 (완료 수 기준)
    @Query("SELECT l FROM Lecture l WHERE l.isPublished = true ORDER BY l.completionCount DESC")
    Page<Lecture> findRecommendedLectures(Pageable pageable);
}
