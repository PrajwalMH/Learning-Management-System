package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.SubmissionRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.SubmissionResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping("/assignments/{assignmentId}/submissions")
    public ResponseEntity<SubmissionResponse> submitAssignment(
            @PathVariable Long assignmentId,
            @Valid @RequestBody SubmissionRequest request
    ) {
        SubmissionResponse response = submissionService.submitAssignment(assignmentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/assignments/{assignmentId}/submissions")
    public ResponseEntity<?> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByAssignment(assignmentId));
    }

    @GetMapping("/students/{studentId}/submissions")
    public ResponseEntity<?> getSubmissionsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByStudent(studentId));
    }

    @GetMapping("/submissions/{submissionId}")
    public ResponseEntity<SubmissionResponse> getSubmissionById(@PathVariable Long submissionId) {
        return ResponseEntity.ok(submissionService.getSubmissionById(submissionId));
    }

    @PutMapping("/submissions/{submissionId}")
    public ResponseEntity<SubmissionResponse> updateSubmission(
            @PathVariable Long submissionId,
            @Valid @RequestBody SubmissionRequest request
    ) {
        return ResponseEntity.ok(submissionService.updateSubmission(submissionId, request));
    }

    @DeleteMapping("/submissions/{submissionId}")
    public ResponseEntity<String> deleteSubmission(@PathVariable Long submissionId) {
        submissionService.deleteSubmission(submissionId);
        return ResponseEntity.ok("Submission deleted successfully");
    }
}