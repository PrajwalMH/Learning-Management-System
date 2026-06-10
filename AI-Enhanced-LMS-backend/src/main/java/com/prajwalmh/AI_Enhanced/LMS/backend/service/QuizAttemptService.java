package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.QuizAttemptRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.QuizAttemptResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.*;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.QuizAttemptRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.QuizQuestionRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.QuizRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QuizAttemptResponse submitAttempt(Long quizId, QuizAttemptRequest request) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        if (!quiz.isPublished()) {
            throw new RuntimeException("Quiz is not published yet");
        }

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        if (student.getRole() != Role.STUDENT) {
            throw new RuntimeException("Selected user is not a student");
        }

        if (quizAttemptRepository.existsByQuizAndStudent(quiz, student)) {
            throw new RuntimeException("Student has already attempted this quiz");
        }

        List<QuizQuestion> questions = quizQuestionRepository.findByQuizOrderByQuestionOrderAsc(quiz);

        int totalQuestions = questions.size();
        int correctAnswers = 0;

        for (QuizQuestion question : questions) {
            String submittedAnswer = request.getSubmittedAnswers().get(question.getId());

            if (submittedAnswer != null &&
                    submittedAnswer.trim().equalsIgnoreCase(question.getCorrectAnswer())) {
                correctAnswers++;
            }
        }

        double score = correctAnswers;
        double percentage = totalQuestions > 0
                ? ((double) correctAnswers / totalQuestions) * 100.0
                : 0.0;

        String answersJson = convertAnswersToJson(request.getSubmittedAnswers());

        QuizAttempt attempt = QuizAttempt.builder()
                .quiz(quiz)
                .student(student)
                .score(score)
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .submittedAnswersJson(answersJson)
                .build();

        QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

        return mapToResponse(savedAttempt);
    }

    public List<QuizAttemptResponse> getAttemptsByQuiz(Long quizId) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        return quizAttemptRepository.findByQuiz(quiz)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<QuizAttemptResponse> getAttemptsByStudent(Long studentId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return quizAttemptRepository.findByStudent(student)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public QuizAttemptResponse getAttemptById(Long attemptId) {

        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Quiz attempt not found with id: " + attemptId));

        return mapToResponse(attempt);
    }

    private String convertAnswersToJson(Map<Long, String> answers) {
        try {
            return objectMapper.writeValueAsString(answers);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert submitted answers to JSON");
        }
    }

    private Map<Long, String> convertJsonToAnswers(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<Long, String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to read submitted answers JSON");
        }
    }

    private QuizAttemptResponse mapToResponse(QuizAttempt attempt) {

        Quiz quiz = attempt.getQuiz();
        Course course = quiz.getCourse();
        User student = attempt.getStudent();

        double percentage = attempt.getTotalQuestions() != null && attempt.getTotalQuestions() > 0
                ? (attempt.getScore() / attempt.getTotalQuestions()) * 100.0
                : 0.0;

        Map<Long, String> submittedAnswers = attempt.getSubmittedAnswersJson() != null
                ? convertJsonToAnswers(attempt.getSubmittedAnswersJson())
                : null;

        return QuizAttemptResponse.builder()
                .id(attempt.getId())
                .quizId(quiz.getId())
                .quizTitle(quiz.getTitle())
                .courseId(course != null ? course.getId() : null)
                .courseTitle(course != null ? course.getTitle() : null)
                .studentId(student.getId())
                .studentName(student.getFullName())
                .studentEmail(student.getEmail())
                .score(attempt.getScore())
                .totalQuestions(attempt.getTotalQuestions())
                .correctAnswers(attempt.getCorrectAnswers())
                .percentage(Math.round(percentage * 100.0) / 100.0)
                .submittedAnswers(submittedAnswers)
                .attemptedAt(attempt.getAttemptedAt())
                .build();
    }
}