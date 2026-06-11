package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.ProgressUpdateRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.ProgressResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.ProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ProgressResponse>> getProgressByStudent(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(progressService.getProgressByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<ProgressResponse> getProgressByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(progressService.getProgressByStudentAndCourse(studentId, courseId));
    }

    @PutMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<ProgressResponse> updateProgress(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            @Valid @RequestBody ProgressUpdateRequest request
    ) {
        return ResponseEntity.ok(progressService.updateProgress(studentId, courseId, request));
    }
}