package com.seungjz.edutech.domain.interview.repository;

import com.seungjz.edutech.domain.interview.entity.InterviewQAPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewQAPairRepository extends JpaRepository<InterviewQAPair, Long> {

    List<InterviewQAPair> findBySessionIdOrderByOrderIndexAsc(Long sessionId);
}
