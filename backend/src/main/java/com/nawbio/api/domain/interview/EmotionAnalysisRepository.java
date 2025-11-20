package com.nawbio.api.domain.interview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmotionAnalysisRepository extends JpaRepository<EmotionAnalysis, Long> {

    List<EmotionAnalysis> findBySessionIdOrderByTimelineSecAsc(Long sessionId);
}
