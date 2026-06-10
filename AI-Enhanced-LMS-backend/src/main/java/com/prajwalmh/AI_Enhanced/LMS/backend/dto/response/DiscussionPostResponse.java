package com.prajwalmh.AI_Enhanced.LMS.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DiscussionPostResponse {

    private Long id;

    private String title;
    private String content;

    private Long courseId;
    private String courseTitle;

    private Long postedById;
    private String postedByName;
    private String postedByRole;

    private boolean active;

    private Long replyCount;

    private List<DiscussionReplyResponse> replies;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}