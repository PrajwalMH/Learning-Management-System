package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.NotificationRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.NotificationResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationRequest request
    ) {
        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getNotificationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<?> getUnreadNotificationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByUser(userId));
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }

    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<?> markAllAsRead(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.markAllAsRead(userId));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok("Notification deleted successfully");
    }
}