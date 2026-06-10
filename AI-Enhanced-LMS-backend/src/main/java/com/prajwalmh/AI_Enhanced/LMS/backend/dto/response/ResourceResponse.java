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
public class ResourceResponse {

    private Long id;

    private String title;
    private String description;

    private String fileName;
    private String fileType;
    private Long fileSize;
    private String fileUrl;
    private String s3Key;

    private Long moduleId;
    private String moduleTitle;

    private Long courseId;
    private String courseTitle;

    private Long uploadedById;
    private String uploadedByName;

    private LocalDateTime uploadedAt;
}