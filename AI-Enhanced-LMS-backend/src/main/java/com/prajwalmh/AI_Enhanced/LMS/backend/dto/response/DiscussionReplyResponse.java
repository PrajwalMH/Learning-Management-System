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
public class DiscussionReplyResponse {

    private Long id;

    private String content;

    private Long postId;

    private Long repliedById;
    private String repliedByName;
    private String repliedByRole;

    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}