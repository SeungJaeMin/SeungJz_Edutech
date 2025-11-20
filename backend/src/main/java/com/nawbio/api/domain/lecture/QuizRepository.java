package com.nawbio.api.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // Lecture ID로 퀴즈 조회 (순서대로 정렬)
    List<Quiz> findByLectureIdOrderBySequenceAsc(Long lectureId);

    // Lecture + Sequence로 특정 퀴즈 조회
    Optional<Quiz> findByLectureIdAndSequence(Long lectureId, Integer sequence);

    // Lecture의 총 퀴즈 개수
    Long countByLectureId(Long lectureId);
}
