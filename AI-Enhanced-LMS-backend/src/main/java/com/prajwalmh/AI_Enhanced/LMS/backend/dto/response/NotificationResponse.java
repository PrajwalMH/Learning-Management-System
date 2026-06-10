package com.prajwalmh.AI_Enhanced.LMS.backend.dto.response;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;

    private String title;
    private String message;
    private NotificationType type;

    private Long recipientId;
    private String recipientName;

    private Long courseId;
    private String courseTitle;

    private boolean readStatus;

    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}