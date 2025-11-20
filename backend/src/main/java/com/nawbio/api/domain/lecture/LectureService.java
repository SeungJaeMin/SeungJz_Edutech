package com.nawbio.api.domain.lecture;

import com.nawbio.api.common.exception.ResourceNotFoundException;
import com.nawbio.api.domain.lecture.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LectureService {

    private final LectureRepository lectureRepository;
    private final QuizRepository quizRepository;
    private final LectureSessionRepository sessionRepository;
    private final UserAnswerRepository answerRepository;

    // 1. Lecture 목록 조회 (추천)
    public Page<LectureDto> getRecommendedLectures(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "completionCount"));
        return lectureRepository.findRecommendedLectures(pageable)
                .map(LectureDto::fromEntityWithoutQuizzes);
    }

    // 2. 카테고리별 Lecture 조회
    public Page<LectureDto> getLecturesByCategory(String category, Integer level, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Lecture> lectures;
        if (level != null) {
            lectures = lectureRepository.findByCategoryAndLevelAndIsPublishedTrue(category, level, pageable);
        } else {
            lectures = lectureRepository.findByCategoryAndIsPublishedTrue(category, pageable);
        }

        return lectures.map(LectureDto::fromEntityWithoutQuizzes);
    }

    // 3. Lecture 상세 조회 (퀴즈 포함)
    @Transactional
    public LectureDto getLectureDetail(Long lectureId) {
        Lecture lecture = lectureRepository.findByIdWithQuizzes(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture not found with id: " + lectureId));

        // 조회수 증가
        lecture.incrementViews();

        return LectureDto.fromEntity(lecture);
    }

    // 4. Lecture 시작
    @Transactional
    public LectureSessionDto startLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture not found with id: " + lectureId));

        Long totalQuizzes = quizRepository.countByLectureId(lectureId);

        // 세션 생성
        LectureSession session = LectureSession.builder()
                .sessionId("session_" + UUID.randomUUID().toString())
                .lecture(lecture)
                .currentQuizSequence(1)
                .totalQuizzes(totalQuizzes.intValue())
                .correctAnswers(0)
                .isCompleted(false)
                .startedAt(LocalDateTime.now())
                .build();

        session = sessionRepository.save(session);

        // 첫 번째 퀴즈의 trigger time 조회
        Quiz firstQuiz = quizRepository.findByLectureIdAndSequence(lectureId, 1)
                .orElse(null);

        LectureSessionDto dto = LectureSessionDto.fromEntity(session);
        if (firstQuiz != null) {
            dto.setNextQuizTriggerSec(firstQuiz.getTriggerTimeSec());
        }

        return dto;
    }

    // 5. 답변 제출 (STT 및 유사도 검사는 mock 처리)
    @Transactional
    public AnswerSubmitResponse submitAnswer(String sessionId, Long quizId, String mockTranscription) {
        LectureSession session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found: " + quizId));

        // 기존 시도 횟수 확인
        Long attemptCount = answerRepository.countBySessionIdAndQuizId(session.getId(), quizId);
        int attemptNumber = attemptCount.intValue() + 1;

        // 유사도 계산 (간단한 문자열 비교 - 실제로는 STT + 유사도 API 호출)
        double similarityScore = calculateSimilarity(mockTranscription, quiz.getCorrectAnswer());
        boolean isCorrect = similarityScore >= 0.8;

        // 답변 저장
        UserAnswer answer = UserAnswer.builder()
                .session(session)
                .quiz(quiz)
                .transcribedText(mockTranscription)
                .isCorrect(isCorrect)
                .similarityScore(similarityScore)
                .attemptNumber(attemptNumber)
                .feedback(isCorrect ? quiz.getCorrectFeedback() : quiz.getIncorrectFeedback())
                .build();

        answer = answerRepository.save(answer);

        // 정답이면 다음 퀴즈로 이동
        Integer nextQuizSequence = session.getCurrentQuizSequence();
        Integer nextQuizTriggerSec = null;

        if (isCorrect) {
            session.incrementCorrectAnswers();
            session.moveToNextQuiz();
            nextQuizSequence = session.getCurrentQuizSequence();

            // 다음 퀴즈의 trigger time 조회
            Quiz nextQuiz = quizRepository.findByLectureIdAndSequence(session.getLecture().getId(), nextQuizSequence)
                    .orElse(null);
            if (nextQuiz != null) {
                nextQuizTriggerSec = nextQuiz.getTriggerTimeSec();
            }
        }

        // 응답 구성
        AnswerSubmitResponse.AnswerSubmitResponseBuilder responseBuilder = AnswerSubmitResponse.builder()
                .answerId(answer.getId())
                .isCorrect(isCorrect)
                .transcribedText(mockTranscription)
                .expectedAnswer(quiz.getCorrectAnswer())
                .similarityScore(similarityScore)
                .feedback(answer.getFeedback())
                .attemptNumber(attemptNumber)
                .nextQuizSequence(nextQuizSequence);

        if (nextQuizTriggerSec != null) {
            responseBuilder.nextQuizTriggerSec(nextQuizTriggerSec);
        }

        if (!isCorrect) {
            int remainingAttempts = quiz.getMaxAttempts() - attemptNumber;
            responseBuilder.remainingAttempts(remainingAttempts);
            if (remainingAttempts > 0 && quiz.getHint() != null) {
                responseBuilder.hint(quiz.getHint());
            }
        }

        return responseBuilder.build();
    }

    // 6. Lecture 완료
    @Transactional
    public LectureSessionDto completeLecture(Long lectureId, LectureCompleteRequest request) {
        LectureSession session = sessionRepository.findBySessionId(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + request.getSessionId()));

        Lecture lecture = session.getLecture();

        // 최종 점수 계산
        double finalScore = (request.getCorrectAnswers().doubleValue() / request.getTotalQuizzes().doubleValue()) * 100;

        // 세션 완료 처리
        session.complete(finalScore);

        // Lecture 완료 횟수 증가
        lecture.incrementCompletionCount();

        return LectureSessionDto.fromEntity(session);
    }

    // 간단한 문자열 유사도 계산 (실제로는 Levenshtein distance 등 사용)
    private double calculateSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0.0;
        }

        text1 = text1.trim().toLowerCase();
        text2 = text2.trim().toLowerCase();

        if (text1.equals(text2)) {
            return 1.0;
        }

        // 간단한 포함 여부 체크
        if (text1.contains(text2) || text2.contains(text1)) {
            return 0.85;
        }

        return 0.5; // 기본값
    }
}
