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
public class GradeResponse {

    private Long id;

    private Double marksObtained;
    private Double maxMarks;
    private Double percentage;
    private String feedback;

    private Long submissionId;

    private Long assignmentId;
    private String assignmentTitle;

    private Long courseId;
    private String courseTitle;

    private Long studentId;
    private String studentName;
    private String studentEmail;

    private Long gradedById;
    private String gradedByName;

    private LocalDateTime gradedAt;
    private LocalDateTime updatedAt;
}