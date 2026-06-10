package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.QuizAttemptRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.QuizAttemptResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.QuizAttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    @PostMapping("/quizzes/{quizId}/attempt")
    public ResponseEntity<QuizAttemptResponse> submitAttempt(
            @PathVariable Long quizId,
            @Valid @RequestBody QuizAttemptRequest request
    ) {
        QuizAttemptResponse response = quizAttemptService.submitAttempt(quizId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/quizzes/{quizId}/attempts")
    public ResponseEntity<?> getAttemptsByQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizAttemptService.getAttemptsByQuiz(quizId));
    }

    @GetMapping("/students/{studentId}/quiz-attempts")
    public ResponseEntity<?> getAttemptsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(quizAttemptService.getAttemptsByStudent(studentId));
    }

    @GetMapping("/quiz-attempts/{attemptId}")
    public ResponseEntity<QuizAttemptResponse> getAttemptById(@PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getAttemptById(attemptId));
    }
}