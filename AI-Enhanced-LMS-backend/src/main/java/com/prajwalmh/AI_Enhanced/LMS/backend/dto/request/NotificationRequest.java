package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {

    @NotBlank(message = "Notification title is required")
    private String title;

    private String message;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotNull(message = "Recipient ID is required")
    private Long recipientId;

    private Long courseId;
}