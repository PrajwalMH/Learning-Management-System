package com.prajwalmh.AI_Enhanced.LMS.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class QuizAttemptResponse {

    private Long id;

    private Long quizId;
    private String quizTitle;

    private Long courseId;
    private String courseTitle;

    private Long studentId;
    private String studentName;
    private String studentEmail;

    private Double score;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Double percentage;

    private Map<Long, String> submittedAnswers;

    private LocalDateTime attemptedAt;
}