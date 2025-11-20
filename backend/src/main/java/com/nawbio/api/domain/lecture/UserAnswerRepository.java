package com.nawbio.api.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    // Session ID로 답변 조회
    List<UserAnswer> findBySessionIdOrderByCreatedAtAsc(Long sessionId);

    // Session + Quiz로 답변 조회
    List<UserAnswer> findBySessionIdAndQuizIdOrderByAttemptNumberAsc(Long sessionId, Long quizId);

    // Session + Quiz의 총 시도 횟수
    Long countBySessionIdAndQuizId(Long sessionId, Long quizId);
}
