package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionRequest {

    private String answerText;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private String fileUrl;

    private String s3Key;

    @NotNull(message = "Student ID is required")
    private Long studentId;
}