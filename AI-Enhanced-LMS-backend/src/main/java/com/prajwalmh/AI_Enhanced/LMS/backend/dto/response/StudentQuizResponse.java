package com.prajwalmh.AI_Enhanced.LMS.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StudentQuizResponse {

    private Long id;

    private String title;
    private String description;
    private String topic;

    private Long courseId;
    private String courseTitle;

    private Long moduleId;
    private String moduleTitle;

    private Integer totalQuestions;

    private LocalDateTime publishedAt;

    private List<StudentQuizQuestionResponse> questions;
}