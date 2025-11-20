package com.seungjz.edutech.repository;

import com.seungjz.edutech.domain.AnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerHistoryRepository extends JpaRepository<AnswerHistory, Long> {
    List<AnswerHistory> findByUserIdAndComponentIdOrderBySubmittedAtDesc(Long userId, Long componentId);

    List<AnswerHistory> findByUserIdOrderBySubmittedAtDesc(Long userId);
}
