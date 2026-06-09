package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequest {

    @NotBlank(message = "Course title is required")
    private String title;

    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    private String level;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;
}