package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.EnrollmentRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.EnrollmentResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentResponse> enrollStudent(
            @Valid @RequestBody EnrollmentRequest request
    ) {
        EnrollmentResponse response = enrollmentService.enrollStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<String> deactivateEnrollment(@PathVariable Long enrollmentId) {
        enrollmentService.deactivateEnrollment(enrollmentId);
        return ResponseEntity.ok("Enrollment deactivated successfully");
    }
}