package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscussionPostRequest {

    @NotBlank(message = "Discussion title is required")
    private String title;

    @NotBlank(message = "Discussion content is required")
    private String content;

    @NotNull(message = "Posted by user ID is required")
    private Long postedById;
}