package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.QuizGenerateRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.QuizResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.QuizGenerationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.StudentQuizResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class QuizController {

    private final QuizGenerationService quizGenerationService;

    @PostMapping("/ai/quizzes/generate")
    public ResponseEntity<QuizResponse> generateQuiz(
            @Valid @RequestBody QuizGenerateRequest request
    ) {
        QuizResponse response = quizGenerationService.generateQuiz(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/quizzes/course/{courseId}")
    public ResponseEntity<?> getQuizzesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(quizGenerationService.getQuizzesByCourse(courseId));
    }

    @GetMapping("/quizzes/{quizId}")
    public ResponseEntity<QuizResponse> getQuizById(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizGenerationService.getQuizById(quizId));
    }

    @PutMapping("/quizzes/{quizId}/publish")
    public ResponseEntity<QuizResponse> publishQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizGenerationService.publishQuiz(quizId));
    }

    @DeleteMapping("/quizzes/{quizId}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long quizId) {
        quizGenerationService.deleteQuiz(quizId);
        return ResponseEntity.ok("Quiz deleted successfully");
    }


    @GetMapping("/student/quizzes/course/{courseId}")
    public ResponseEntity<List<StudentQuizResponse>> getPublishedQuizzesByCourse(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                quizGenerationService.getPublishedQuizzesByCourse(courseId)
        );
    }

    @GetMapping("/student/quizzes/{quizId}")
    public ResponseEntity<StudentQuizResponse> getPublishedQuizForStudent(
            @PathVariable Long quizId
    ) {
        return ResponseEntity.ok(
                quizGenerationService.getPublishedQuizForStudent(quizId)
        );
    }
}