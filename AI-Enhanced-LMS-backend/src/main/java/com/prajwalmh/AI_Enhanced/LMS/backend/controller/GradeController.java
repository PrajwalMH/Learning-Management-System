package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.GradeRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.GradeResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class GradeController {

    private final GradeService gradeService;

    @PostMapping("/submissions/{submissionId}/grade")
    public ResponseEntity<GradeResponse> gradeSubmission(
            @PathVariable Long submissionId,
            @Valid @RequestBody GradeRequest request
    ) {
        GradeResponse response = gradeService.gradeSubmission(submissionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/students/{studentId}/grades")
    public ResponseEntity<?> getGradesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getGradesByStudent(studentId));
    }

    @GetMapping("/courses/{courseId}/grades")
    public ResponseEntity<?> getGradesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(gradeService.getGradesByCourse(courseId));
    }

    @GetMapping("/grades/{gradeId}")
    public ResponseEntity<GradeResponse> getGradeById(@PathVariable Long gradeId) {
        return ResponseEntity.ok(gradeService.getGradeById(gradeId));
    }

    @PutMapping("/grades/{gradeId}")
    public ResponseEntity<GradeResponse> updateGrade(
            @PathVariable Long gradeId,
            @Valid @RequestBody GradeRequest request
    ) {
        return ResponseEntity.ok(gradeService.updateGrade(gradeId, request));
    }
}