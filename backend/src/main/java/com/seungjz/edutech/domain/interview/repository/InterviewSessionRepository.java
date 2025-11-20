package com.seungjz.edutech.domain.interview.repository;

import com.seungjz.edutech.domain.interview.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {

    Optional<InterviewSession> findBySessionId(String sessionId);

    List<InterviewSession> findByUserIdOrderByCreatedAtDesc(Long userId);
}
