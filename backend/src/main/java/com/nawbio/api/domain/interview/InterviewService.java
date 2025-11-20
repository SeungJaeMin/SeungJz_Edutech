package com.nawbio.api.domain.interview;

import com.nawbio.api.common.exception.ResourceNotFoundException;
import com.nawbio.api.domain.interview.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InterviewService {

    private final InterviewSessionRepository sessionRepository;
    private final InterviewQuestionRepository questionRepository;
    private final QAPairRepository qaPairRepository;
    private final EmotionAnalysisRepository emotionRepository;

    // 1. 면접 세션 시작
    @Transactional
    public InterviewStartResponse startInterview(InterviewStartRequest request, Long userId) {
        // 세션 생성
        InterviewSession session = InterviewSession.builder()
                .sessionId("interview_" + UUID.randomUUID().toString())
                .userId(userId)
                .prompt(request.getPrompt())
                .durationSeconds(request.getDurationSeconds())
                .status("recording")
                .startedAt(LocalDateTime.now())
                .build();

        // 초기 질문 생성 (실제로는 LLM 호출)
        InterviewQuestion initialQuestion = InterviewQuestion.builder()
                .session(session)
                .questionText("자기소개를 해주세요")
                .ttsAudioUrl("/api/tts/question_init.mp3") // Mock TTS URL
                .sequence(1)
                .isInitialQuestion(true)
                .build();

        session.addQuestion(initialQuestion);
        session = sessionRepository.save(session);

        return InterviewStartResponse.builder()
                .sessionId(session.getSessionId())
                .status(session.getStatus())
                .durationSeconds(session.getDurationSeconds())
                .startedAt(session.getStartedAt())
                .initialQuestion(QuestionDto.fromEntity(initialQuestion))
                .build();
    }

    // 2. 실시간 비디오 프레임 전송 (감정 분석)
    @Transactional
    public EmotionResponse analyzeEmotion(String sessionId, Integer timelineSec) {
        InterviewSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));

        // Mock 감정 분석 (실제로는 DeepFace API 호출)
        Map<String, Double> emotionScores = new HashMap<>();
        emotionScores.put("happy", 0.2);
        emotionScores.put("neutral", 0.7);
        emotionScores.put("anxious", 0.1);

        EmotionAnalysis analysis = EmotionAnalysis.builder()
                .session(session)
                .timelineSec(timelineSec)
                .dominantEmotion("neutral")
                .confidence(0.85)
                .emotionScores(emotionScores)
                .gazeDirection("camera")
                .smileIntensity(0.3)
                .build();

        emotionRepository.save(analysis);

        return EmotionResponse.builder()
                .timelineSec(timelineSec)
                .emotion(EmotionResponse.EmotionData.builder()
                        .dominantEmotion(analysis.getDominantEmotion())
                        .confidence(analysis.getConfidence())
                        .emotionScores(analysis.getEmotionScores())
                        .gazeDirection(analysis.getGazeDirection())
                        .smileIntensity(analysis.getSmileIntensity())
                        .build())
                .build();
    }

    // 3. 실시간 음성 답변 제출 및 질문 생성
    @Transactional
    public AnswerSubmitResponse submitAnswer(String sessionId, Long questionId, String transcription, Integer timelineSec) {
        InterviewSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));

        InterviewQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found: " + questionId));

        // QA 쌍 저장
        QAPair qaPair = QAPair.builder()
                .session(session)
                .question(question)
                .transcribedText(transcription)
                .answerDurationSec(25) // Mock
                .timelineSec(timelineSec)
                .hasKeywords(true)
                .isCoherent(true)
                .confidenceScore(0.9)
                .build();

        qaPairRepository.save(qaPair);

        // 다음 질문 생성 (실제로는 LLM 호출)
        InterviewQuestion nextQuestion = InterviewQuestion.builder()
                .session(session)
                .questionText("팀 프로젝트 경험을 말씀해주세요")
                .ttsAudioUrl("/api/tts/question_" + (session.getTotalQuestions() + 1) + ".mp3")
                .sequence(session.getTotalQuestions() + 1)
                .isInitialQuestion(false)
                .build();

        session.addQuestion(nextQuestion);

        return AnswerSubmitResponse.builder()
                .qaPairId(qaPair.getId())
                .questionId(questionId)
                .transcribedText(transcription)
                .answerDurationSec(25)
                .analysisResult(AnswerSubmitResponse.AnalysisResult.builder()
                        .hasKeywords(true)
                        .missingKeywords(new ArrayList<>())
                        .isCoherent(true)
                        .confidence(0.9)
                        .build())
                .nextQuestion(QuestionDto.fromEntity(nextQuestion))
                .build();
    }

    // 4. 면접 세션 종료
    @Transactional
    public InterviewCompleteResponse completeInterview(String sessionId) {
        InterviewSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));

        String videoUrl = "/api/videos/" + sessionId + ".mp4"; // Mock
        session.complete(videoUrl);

        return InterviewCompleteResponse.builder()
                .sessionId(sessionId)
                .status("completed")
                .completedAt(session.getCompletedAt())
                .videoRecordingUrl(videoUrl)
                .totalQuestions(session.getTotalQuestions())
                .totalDurationSec(session.getDurationSeconds())
                .message("면접이 완료되었습니다. 결과를 분석 중입니다...")
                .build();
    }
}
