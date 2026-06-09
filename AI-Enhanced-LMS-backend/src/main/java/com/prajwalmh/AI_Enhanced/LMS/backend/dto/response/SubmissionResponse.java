package com.prajwalmh.AI_Enhanced.LMS.backend.dto.response;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SubmissionResponse {

    private Long id;

    private String answerText;

    private String fileName;
    private String fileType;
    private Long fileSize;
    private String fileUrl;
    private String s3Key;

    private SubmissionStatus status;

    private Long assignmentId;
    private String assignmentTitle;

    private Long studentId;
    private String studentName;
    private String studentEmail;

    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
}