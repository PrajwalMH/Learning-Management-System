package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseModuleRequest {

    @NotBlank(message = "Module title is required")
    private String title;

    private String description;

    private Integer orderIndex;

    private boolean published;
}