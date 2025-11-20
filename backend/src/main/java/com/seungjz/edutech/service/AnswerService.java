package com.seungjz.edutech.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.seungjz.edutech.domain.AnswerHistory;
import com.seungjz.edutech.domain.Component;
import com.seungjz.edutech.domain.User;
import com.seungjz.edutech.dto.AnswerSubmitRequest;
import com.seungjz.edutech.repository.AnswerHistoryRepository;
import com.seungjz.edutech.repository.ComponentRepository;
import com.seungjz.edutech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {
    private final AnswerHistoryRepository answerHistoryRepository;
    private final ComponentRepository componentRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public AnswerHistory submitAnswer(Long userId, AnswerSubmitRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Component component = componentRepository.findById(request.getComponentId())
                .orElseThrow(() -> new RuntimeException("Component not found"));

        AnswerHistory answerHistory = new AnswerHistory();
        answerHistory.setUser(user);
        answerHistory.setComponent(component);
        answerHistory.setUserAnswer(request.getUserAnswer());

        // Simple answer checking logic
        boolean isCorrect = checkAnswer(component, request.getUserAnswer());
        answerHistory.setIsCorrect(isCorrect);
        answerHistory.setScore(isCorrect ? 100 : 0);

        // Generate feedback
        ObjectNode feedback = objectMapper.createObjectNode();
        feedback.put("message", isCorrect ? "정답입니다!" : "다시 한번 시도해보세요.");
        answerHistory.setFeedback(feedback);

        return answerHistoryRepository.save(answerHistory);
    }

    private boolean checkAnswer(Component component, JsonNode userAnswer) {
        JsonNode content = component.getContent();

        if (component.getComponentType() == Component.ComponentType.QUIZ) {
            if (content.has("correctAnswer") && userAnswer.has("answer")) {
                String correctAnswer = content.get("correctAnswer").asText();
                String userAnswerText = userAnswer.get("answer").asText();
                return correctAnswer.equalsIgnoreCase(userAnswerText);
            }
        }

        // Default to true for non-quiz components
        return true;
    }
}
