package com.prajwalmh.AI_Enhanced.LMS.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CourseModuleResponse {

    private Long id;
    private String title;
    private String description;
    private Integer orderIndex;
    private boolean published;

    private Long courseId;
    private String courseTitle;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}