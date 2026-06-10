package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceRequest {

    @NotBlank(message = "Resource title is required")
    private String title;

    private String description;

    @NotBlank(message = "File name is required")
    private String fileName;

    private String fileType;

    private Long fileSize;

    private String fileUrl;

    private String s3Key;

    @NotNull(message = "Uploaded by user ID is required")
    private Long uploadedById;
}