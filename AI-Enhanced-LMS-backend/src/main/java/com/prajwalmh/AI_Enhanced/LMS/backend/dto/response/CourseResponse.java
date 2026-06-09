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
public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private String level;
    private boolean active;

    private Long teacherId;
    private String teacherName;
    private String teacherEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}