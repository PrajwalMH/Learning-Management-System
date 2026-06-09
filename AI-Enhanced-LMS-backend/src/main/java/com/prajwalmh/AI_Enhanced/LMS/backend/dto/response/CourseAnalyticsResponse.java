package com.prajwalmh.AI_Enhanced.LMS.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CourseAnalyticsResponse {

    private Long courseId;
    private String courseTitle;

    private Long totalAssignments;
    private Long totalSubmissions;
    private Long totalGradedSubmissions;

    private Double classAverage;
    private Double highestScore;
    private Double lowestScore;

    private Long excellentCount;
    private Long goodCount;
    private Long averageCount;
    private Long weakCount;
}