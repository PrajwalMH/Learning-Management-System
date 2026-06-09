package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AssignmentRequest {

    @NotBlank(message = "Assignment title is required")
    private String title;

    private String description;

    @NotNull(message = "Maximum marks is required")
    private Double maxMarks;

    private LocalDateTime dueDate;

    private boolean published;

    @NotNull(message = "Teacher ID is required")
    private Long createdById;
}