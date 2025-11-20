package com.nawbio.api.domain.learning;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {

    List<InterviewSession> findByUserIdOrderByCreatedAtDesc(String userId);

    Optional<InterviewSession> findByUserIdAndStatus(String userId, InterviewSession.SessionStatus status);

    List<InterviewSession> findByUserIdAndStageOrderByCreatedAtDesc(String userId, Integer stage);
}
