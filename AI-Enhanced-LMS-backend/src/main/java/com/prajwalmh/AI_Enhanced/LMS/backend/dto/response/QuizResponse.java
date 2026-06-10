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
public class QuizResponse {

    private Long id;

    private String title;
    private String description;
    private String topic;

    private boolean aiGenerated;
    private boolean published;

    private Long courseId;
    private String courseTitle;

    private Long moduleId;
    private String moduleTitle;

    private Long createdById;
    private String createdByName;

    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    private List<QuizQuestionResponse> questions;
}