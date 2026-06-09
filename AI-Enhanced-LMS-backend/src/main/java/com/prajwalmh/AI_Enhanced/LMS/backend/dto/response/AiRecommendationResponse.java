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
public class AiRecommendationResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseTitle;

    private String weakTopic;
    private Double score;
    private String priority;

    private String searchQuery;
    private String resourceTitle;
    private String recommendationText;
    private String resourceUrl;
    private String resourceType;

    private Integer recommendationScore;

    private boolean completed;
    private LocalDateTime generatedAt;
}