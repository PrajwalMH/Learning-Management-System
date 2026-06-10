package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscussionReplyRequest {

    @NotBlank(message = "Reply content is required")
    private String content;

    @NotNull(message = "Replied by user ID is required")
    private Long repliedById;
}