package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class QuizAttemptRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    // Example:
    // {
    //   "1": "B",
    //   "2": "A",
    //   "3": "C"
    // }
    @NotNull(message = "Submitted answers are required")
    private Map<Long, String> submittedAnswers;
}