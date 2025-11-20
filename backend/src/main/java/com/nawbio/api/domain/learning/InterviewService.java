package com.nawbio.api.domain.learning;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nawbio.api.domain.learning.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterviewService {

    private final InterviewSessionRepository sessionRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public SessionResponse startSession(SessionStartRequest request) {
        // Check if there's already an active session
        Optional<InterviewSession> activeSession = sessionRepository
                .findByUserIdAndStatus(request.getUserId(), InterviewSession.SessionStatus.IN_PROGRESS);

        if (activeSession.isPresent()) {
            throw new IllegalStateException("There is already an active session for this user");
        }

        InterviewSession session = InterviewSession.builder()
                .userId(request.getUserId())
                .stage(request.getStage())
                .status(InterviewSession.SessionStatus.STARTED)
                .startedAt(LocalDateTime.now())
                .webcamEnabled(true)
                .build();

        session = sessionRepository.save(session);

        SessionResponse response = SessionResponse.from(session);
        response.setMessage("Session started successfully. Webcam is now enabled.");

        return response;
    }

    @Transactional
    public EmotionAnalysisResponse analyzeEmotion(EmotionAnalysisRequest request) {
        InterviewSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        if (session.getStatus() == InterviewSession.SessionStatus.COMPLETED) {
            throw new IllegalStateException("This session has already been completed");
        }

        // Update session status to IN_PROGRESS if it's STARTED
        if (session.getStatus() == InterviewSession.SessionStatus.STARTED) {
            session.setStatus(InterviewSession.SessionStatus.IN_PROGRESS);
        }

        // Simulate emotion analysis (in real implementation, this would call an AI service)
        Map<String, Double> emotions = simulateEmotionAnalysis(request.getImageData());

        // Find dominant emotion
        Map.Entry<String, Double> dominantEntry = emotions.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        String dominantEmotion = dominantEntry.getKey();
        Double confidence = dominantEntry.getValue();

        // Store emotion data
        try {
            List<Map<String, Object>> emotionHistory = new ArrayList<>();
            if (session.getEmotionData() != null && !session.getEmotionData().isEmpty()) {
                emotionHistory = objectMapper.readValue(session.getEmotionData(), List.class);
            }

            Map<String, Object> emotionEntry = new HashMap<>();
            emotionEntry.put("timestamp", request.getTimestamp());
            emotionEntry.put("emotions", emotions);
            emotionEntry.put("dominantEmotion", dominantEmotion);
            emotionEntry.put("confidence", confidence);

            emotionHistory.add(emotionEntry);

            session.setEmotionData(objectMapper.writeValueAsString(emotionHistory));
            session.setDominantEmotion(dominantEmotion);
            session.setAverageConfidence(confidence);

        } catch (JsonProcessingException e) {
            log.error("Error processing emotion data", e);
        }

        sessionRepository.save(session);

        return EmotionAnalysisResponse.builder()
                .sessionId(session.getId())
                .dominantEmotion(dominantEmotion)
                .confidence(confidence)
                .emotions(emotions)
                .timestamp(request.getTimestamp())
                .message("Emotion analyzed successfully")
                .build();
    }

    @Transactional
    public SessionResponse endSession(Long sessionId) {
        InterviewSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        session.setStatus(InterviewSession.SessionStatus.COMPLETED);
        session.setEndedAt(LocalDateTime.now());
        session.setWebcamEnabled(false);

        session = sessionRepository.save(session);

        SessionResponse response = SessionResponse.from(session);
        response.setMessage("Session ended successfully");

        return response;
    }

    public SessionResponse getSession(Long sessionId) {
        InterviewSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        return SessionResponse.from(session);
    }

    public List<SessionResponse> getUserSessions(String userId) {
        List<InterviewSession> sessions = sessionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return sessions.stream()
                .map(SessionResponse::from)
                .toList();
    }

    // Simulated emotion analysis - replace with actual AI service
    private Map<String, Double> simulateEmotionAnalysis(String imageData) {
        Map<String, Double> emotions = new HashMap<>();
        Random random = new Random();

        emotions.put("happy", random.nextDouble() * 100);
        emotions.put("sad", random.nextDouble() * 100);
        emotions.put("neutral", random.nextDouble() * 100);
        emotions.put("angry", random.nextDouble() * 100);
        emotions.put("surprised", random.nextDouble() * 100);
        emotions.put("fear", random.nextDouble() * 100);
        emotions.put("disgust", random.nextDouble() * 100);

        // Normalize to sum to 100
        double sum = emotions.values().stream().mapToDouble(Double::doubleValue).sum();
        emotions.replaceAll((k, v) -> (v / sum) * 100);

        return emotions;
    }
}
