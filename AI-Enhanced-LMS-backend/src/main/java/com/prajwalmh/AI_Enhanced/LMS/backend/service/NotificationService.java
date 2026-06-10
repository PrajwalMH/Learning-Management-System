package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.NotificationRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.NotificationResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Notification;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.NotificationRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public NotificationResponse createNotification(NotificationRequest request) {

        User recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found with id: " + request.getRecipientId()));

        Course course = null;

        if (request.getCourseId() != null) {
            course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.getCourseId()));
        }

        Notification notification = Notification.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .recipient(recipient)
                .course(course)
                .readStatus(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        return mapToResponse(savedNotification);
    }

    public List<NotificationResponse> getNotificationsByUser(Long userId) {

        User recipient = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return notificationRepository.findByRecipientOrderByCreatedAtDesc(recipient)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<NotificationResponse> getUnreadNotificationsByUser(Long userId) {

        User recipient = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return notificationRepository.findByRecipientAndReadStatusFalseOrderByCreatedAtDesc(recipient)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Long getUnreadCount(Long userId) {

        User recipient = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return notificationRepository.countByRecipientAndReadStatusFalse(recipient);
    }

    public NotificationResponse markAsRead(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        notification.setReadStatus(true);
        notification.setReadAt(LocalDateTime.now());

        Notification updatedNotification = notificationRepository.save(notification);

        return mapToResponse(updatedNotification);
    }

    public List<NotificationResponse> markAllAsRead(Long userId) {

        User recipient = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<Notification> unreadNotifications =
                notificationRepository.findByRecipientAndReadStatusFalseOrderByCreatedAtDesc(recipient);

        for (Notification notification : unreadNotifications) {
            notification.setReadStatus(true);
            notification.setReadAt(LocalDateTime.now());
        }

        List<Notification> savedNotifications = notificationRepository.saveAll(unreadNotifications);

        return savedNotifications.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteNotification(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        notificationRepository.delete(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {

        User recipient = notification.getRecipient();
        Course course = notification.getCourse();

        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .recipientId(recipient != null ? recipient.getId() : null)
                .recipientName(recipient != null ? recipient.getFullName() : null)
                .courseId(course != null ? course.getId() : null)
                .courseTitle(course != null ? course.getTitle() : null)
                .readStatus(notification.isReadStatus())
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .build();
    }
}