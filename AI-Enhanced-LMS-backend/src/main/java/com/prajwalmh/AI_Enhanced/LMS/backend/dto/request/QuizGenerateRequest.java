package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizGenerateRequest {

    @NotNull(message = "Course ID is required")
    private Long courseId;

    private Long moduleId;

    @NotNull(message = "Teacher ID is required")
    private Long createdById;

    @NotBlank(message = "Quiz title is required")
    private String title;

    private String description;

    @NotBlank(message = "Topic is required")
    private String topic;

    private Integer numberOfQuestions;
}