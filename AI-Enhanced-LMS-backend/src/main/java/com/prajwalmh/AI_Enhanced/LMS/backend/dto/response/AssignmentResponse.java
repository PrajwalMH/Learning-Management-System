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
public class AssignmentResponse {

    private Long id;

    private String title;
    private String description;
    private Double maxMarks;
    private LocalDateTime dueDate;
    private boolean published;

    private Long courseId;
    private String courseTitle;

    private Long createdById;
    private String createdByName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}