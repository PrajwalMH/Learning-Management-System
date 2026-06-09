package com.prajwalmh.AI_Enhanced.LMS.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StudentTrendResponse {

    private Long assignmentId;
    private String assignmentTitle;

    private Double marksObtained;
    private Double maxMarks;
    private Double percentage;

    private String trendDirection;
    private String arrow;
}