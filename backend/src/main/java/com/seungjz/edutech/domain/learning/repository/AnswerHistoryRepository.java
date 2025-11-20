package com.seungjz.edutech.domain.learning.repository;

import com.seungjz.edutech.domain.learning.entity.AnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerHistoryRepository extends JpaRepository<AnswerHistory, Long> {

    List<AnswerHistory> findByUserIdAndComponentLectureIdOrderByCreatedAtAsc(Long userId, Long lectureId);
}
